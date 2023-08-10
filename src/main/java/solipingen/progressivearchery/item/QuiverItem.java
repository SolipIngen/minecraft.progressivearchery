package solipingen.progressivearchery.item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.sound.ModSoundEvents;


public class QuiverItem extends Item {
    public static final String ITEMS_KEY = "Items";
    public static final int MAX_STORAGE = 384;
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.53f, 0.81f, 0.92f);

    
    public QuiverItem(Settings settings) {
        super(settings);
    }

    public static float getAmountFilled(ItemStack stack) {
        return (float)QuiverItem.getQuiverOccupancy(stack) / (float)MAX_STORAGE;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        }
        ItemStack itemStack = slot.getStack();
        Item item = itemStack.getItem();
        if (!(item instanceof ArrowItem || item instanceof ModArrowItem || item instanceof KidArrowItem || itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.FIREWORK_ROCKET) || itemStack.isEmpty())) {
            return false;
        }
        if (itemStack.isEmpty()) {
            QuiverItem.removeFirstStack(stack).ifPresent(removedStack -> QuiverItem.addToQuiver(stack, slot.insertStack((ItemStack)removedStack)));
            this.playRemoveOneSound(player);
        } else if (itemStack.getItem().canBeNested()) {
            int i = (MAX_STORAGE - QuiverItem.getQuiverOccupancy(stack)) / QuiverItem.getItemOccupancy(itemStack);
            int j = QuiverItem.addToQuiver(stack, slot.takeStackRange(itemStack.getCount(), i, player));
            if (j > 0) {
                this.playInsertSound(player);
            }
        }
        return true;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT || !slot.canTakePartial(player)) {
            return false;
        }
        if (otherStack.isEmpty()) {
            QuiverItem.removeFirstStack(stack).ifPresent(itemStack -> {
                cursorStackReference.set((ItemStack)itemStack);
                this.playRemoveOneSound(player);
            });
        } else {
            int i = QuiverItem.addToQuiver(stack, otherStack);
            if (i > 0) {
                otherStack.decrement(i);
                this.playInsertSound(player);
            }
        }
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (QuiverItem.dropAllStoredItems(itemStack, user)) {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            this.playDropContentsSound(user);
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return QuiverItem.getQuiverOccupancy(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12*QuiverItem.getQuiverOccupancy(stack) / MAX_STORAGE, 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    private static int addToQuiver(ItemStack quiver, ItemStack stack) {
        if (stack.isEmpty() || !stack.getItem().canBeNested()) {
            return 0;
        }
        if (!(stack.getItem() instanceof ArrowItem || stack.getItem() instanceof ModArrowItem || stack.getItem() instanceof KidArrowItem || stack.isOf(Items.FIREWORK_ROCKET))) {
            return 0;
        }
        NbtCompound nbtCompound = quiver.getOrCreateNbt();
        if (!nbtCompound.contains(ITEMS_KEY)) {
            nbtCompound.put(ITEMS_KEY, new NbtList());
        }
        int i = QuiverItem.getQuiverOccupancy(quiver);
        int j = QuiverItem.getItemOccupancy(stack);
        int k = Math.min(stack.getCount(), (MAX_STORAGE - i) / j);
        if (k == 0) {
            return 0;
        }
        NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        Optional<NbtCompound> optional = QuiverItem.canMergeStack(stack, nbtList);
        if (optional.isPresent()) {
            NbtCompound nbtCompound2 = optional.get();
            ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
            itemStack.increment(k);
            if (itemStack.getCount() <= 64) {
                itemStack.writeNbt(nbtCompound2);
                int index = nbtList.indexOf(nbtCompound2);
                nbtList.remove(nbtCompound2);
                nbtList.add(index, nbtCompound2);
            }
            else {
                int r = itemStack.getCount() - 64;
                itemStack.setCount(64);
                itemStack.writeNbt(nbtCompound2);
                int index = nbtList.indexOf(nbtCompound2);
                nbtList.remove(nbtCompound2);
                nbtList.add(index, nbtCompound2);
                itemStack.setCount(r);
                NbtCompound extraNbtCompound = new NbtCompound();
                itemStack.writeNbt(extraNbtCompound);
                nbtList.add(index++, extraNbtCompound);
            }
        }
        else {
            ItemStack itemStack2 = stack.copy();
            itemStack2.setCount(k);
            NbtCompound nbtCompound3 = new NbtCompound();
            itemStack2.writeNbt(nbtCompound3);
            nbtList.add(0, nbtCompound3);
        }
        return k;
    }

    public static Optional<NbtCompound> canMergeStack(ItemStack stack, NbtList items) {
        if (stack.isOf(ModItems.QUIVER)) {
            return Optional.empty();
        }
        return items.stream().filter(NbtCompound.class::isInstance).map(NbtCompound.class::cast).filter(item -> ItemStack.canCombine(ItemStack.fromNbt(item), stack)).findFirst();
    }

    public static int getItemOccupancy(ItemStack stack) {
        if (stack.isOf(ModItems.QUIVER)) {
            return 4 + QuiverItem.getQuiverOccupancy(stack);
        }
        return 64 / stack.getMaxCount();
    }

    public static int getQuiverOccupancy(ItemStack stack) {
        return QuiverItem.getStoredStacks(stack).mapToInt(itemStack -> QuiverItem.getItemOccupancy(itemStack)*itemStack.getCount()).sum();
    }

    private static Optional<ItemStack> removeFirstStack(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!nbtCompound.contains(ITEMS_KEY)) {
            return Optional.empty();
        }
        NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        if (nbtList.isEmpty()) {
            return Optional.empty();
        }
        NbtCompound nbtCompound2 = nbtList.getCompound(0);
        ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
        nbtList.remove(0);
        if (nbtList.isEmpty()) {
            stack.removeSubNbt(ITEMS_KEY);
        }
        return Optional.of(itemStack);
    }

    private static boolean dropAllStoredItems(ItemStack stack, PlayerEntity player) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!nbtCompound.contains(ITEMS_KEY)) {
            return false;
        }
        if (player instanceof ServerPlayerEntity) {
            NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound2 = nbtList.getCompound(i);
                ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
                player.dropItem(itemStack, true);
            }
        }
        stack.removeSubNbt(ITEMS_KEY);
        return true;
    }

    public static Stream<ItemStack> getStoredStacks(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return Stream.empty();
        }
        NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
        QuiverItem.getStoredStacks(stack).forEach(defaultedList::add);
        return Optional.of(new BundleTooltipData(defaultedList, QuiverItem.getQuiverOccupancy(stack)));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        String occupancy = String.valueOf(QuiverItem.getQuiverOccupancy(stack));
        tooltip.add(Text.of(occupancy + "/" + MAX_STORAGE));
        if (QuiverItem.getQuiverOccupancy(stack) < MAX_STORAGE) {
            tooltip.add(Text.translatable("item.progressivearchery.quiver_add_tooltip").formatted(Formatting.GRAY));
        }
        if (QuiverItem.getQuiverOccupancy(stack) > 0) {
            tooltip.add(Text.translatable("item.progressivearchery.quiver_take_tooltip").formatted(Formatting.GRAY));
        }
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ItemUsage.spawnItemContents(entity, QuiverItem.getStoredStacks(entity.getStack()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(ModSoundEvents.DRAW_FROM_QUIVER, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(ModSoundEvents.PUT_INTO_QUIVER, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(ModSoundEvents.DRAW_FROM_QUIVER, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }


}

