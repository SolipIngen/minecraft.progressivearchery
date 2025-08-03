package solipingen.progressivearchery.item;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;
import solipingen.progressivearchery.item.tooltip.QuiverTooltipData;
import solipingen.progressivearchery.component.type.QuiverContentsComponent;
import solipingen.progressivearchery.component.ModDataComponentTypes;
import solipingen.progressivearchery.sound.ModSoundEvents;


public class QuiverItem extends Item {
    private static final int FULL_ITEM_BAR_COLOR = 14050304;
    private static final int ITEM_BAR_COLOR = 8966124;

    
    public QuiverItem(Settings settings) {
        super(settings.component(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT));
    }

    public static float getAmountFilled(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
        return quiverContentsComponent.getOccupancy().floatValue();
    }

    public static Stream<ItemStack> getStoredStacks(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
        return quiverContentsComponent.stream();
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        int selectedItemIndex = stack.getOrDefault(ModDataComponentTypes.QUIVER_SELECTED_INDEX, -1);
        if (quiverContentsComponent != null) {
            ItemStack itemStack = slot.getStack();
            QuiverContentsComponent.Builder builder = new QuiverContentsComponent.Builder(quiverContentsComponent);
            if (clickType == ClickType.LEFT && !itemStack.isEmpty()) {
                if (!(ModCrossbowItem.MOD_CROSSBOW_PROJECTILES.test(itemStack) || ModBowItem.TUBULAR_BOW_PROJECTILES.test(itemStack))) {
                    QuiverItem.playInsertFailSound(player);
                    return false;
                }
                if (builder.add(slot, player) > 0) {
                    QuiverItem.playInsertSound(player);
                }
                else {
                    QuiverItem.playInsertFailSound(player);
                }
                stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
                this.onContentChanged(player, stack);
                return true;
            }
            else if (clickType == ClickType.RIGHT && itemStack.isEmpty()) {
                ItemStack itemStack2 = builder.remove(selectedItemIndex);
                stack.remove(ModDataComponentTypes.QUIVER_SELECTED_INDEX);
                if (itemStack2 != null) {
                    ItemStack itemStack3 = slot.insertStack(itemStack2);
                    if (!itemStack3.isEmpty()) {
                        builder.add(itemStack3);
                    }
                    else {
                        QuiverItem.playRemoveOneSound(player);
                    }
                }
                stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
                this.onContentChanged(player, stack);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.LEFT && otherStack.isEmpty()) {
            QuiverItem.setSelectedStackIndex(stack, -1);
            return false;
        }
        else {
            QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
            int selectedItemIndex = stack.getOrDefault(ModDataComponentTypes.QUIVER_SELECTED_INDEX, -1);
            if (quiverContentsComponent == null) {
                return false;
            }
            else {
                QuiverContentsComponent.Builder builder = new QuiverContentsComponent.Builder(quiverContentsComponent);
                if (clickType == ClickType.LEFT && !otherStack.isEmpty()) {
                    if (!(ModCrossbowItem.MOD_CROSSBOW_PROJECTILES.test(otherStack) || ModBowItem.TUBULAR_BOW_PROJECTILES.test(otherStack))) {
                        QuiverItem.playInsertFailSound(player);
                        return false;
                    }
                    if (slot.canTakePartial(player) && builder.add(otherStack) > 0) {
                        QuiverItem.playInsertSound(player);
                    }
                    else {
                        QuiverItem.playInsertFailSound(player);
                    }
                    stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
                    this.onContentChanged(player, stack);
                    return true;
                }
                else if (clickType == ClickType.RIGHT && otherStack.isEmpty()) {
                    if (slot.canTakePartial(player)) {
                        ItemStack itemStack = builder.remove(selectedItemIndex);
                        stack.remove(ModDataComponentTypes.QUIVER_SELECTED_INDEX);
                        if (itemStack != null) {
                            QuiverItem.playRemoveOneSound(player);
                            cursorStackReference.set(itemStack);
                        }
                    }
                    stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
                    this.onContentChanged(player, stack);
                    return true;
                }
                else {
                    QuiverItem.setSelectedStackIndex(stack, -1);
                    return false;
                }
            }
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return ActionResult.SUCCESS;
    }

    private void dropContentsOnUse(World world, PlayerEntity player, ItemStack stack) {
        if (this.dropFirstStoredStack(stack, player)) {
            QuiverItem.playDropContentsSound(player);
            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
        return quiverContentsComponent.getOccupancy().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
        return Math.min(1 + MathHelper.multiplyFraction(quiverContentsComponent.getOccupancy(), 12), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
        return quiverContentsComponent.getOccupancy().compareTo(Fraction.ONE) >= 0 ? FULL_ITEM_BAR_COLOR : ITEM_BAR_COLOR;
    }

    public static void setSelectedStackIndex(ItemStack stack, int selectedStackIndex) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (quiverContentsComponent != null) {
            QuiverContentsComponent.Builder builder = new QuiverContentsComponent.Builder(quiverContentsComponent);
            stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
            if (builder.isWithinBounds(selectedStackIndex)) {
                stack.set(ModDataComponentTypes.QUIVER_SELECTED_INDEX, selectedStackIndex);
            }
            else {
                stack.remove(ModDataComponentTypes.QUIVER_SELECTED_INDEX);
            }
        }
    }

    public static boolean hasSelectedStack(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        return quiverContentsComponent != null
                && stack.getOrDefault(ModDataComponentTypes.QUIVER_SELECTED_INDEX, -1) >= 0;
    }

    public static int getSelectedStackIndex(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.QUIVER_SELECTED_INDEX, -1);
    }

    public static ItemStack getSelectedStack(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        int selectedIndex = stack.getOrDefault(ModDataComponentTypes.QUIVER_SELECTED_INDEX, -1);
        return quiverContentsComponent != null && selectedIndex >= 0 ? quiverContentsComponent.get(selectedIndex) : ItemStack.EMPTY;
    }

    public static int getNumberOfStacksShown(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
        return quiverContentsComponent.getNumberOfStacksShown();
    }

    private boolean dropFirstStoredStack(ItemStack stack, PlayerEntity player) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (quiverContentsComponent != null && !quiverContentsComponent.isEmpty()) {
            Optional<ItemStack> optional = QuiverItem.popFirstStoredStack(stack, player, quiverContentsComponent);
            if (optional.isPresent()) {
                player.dropItem(optional.get(), true);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private static Optional<ItemStack> popFirstStoredStack(ItemStack stack, PlayerEntity player, QuiverContentsComponent contents) {
        QuiverContentsComponent.Builder builder = new QuiverContentsComponent.Builder(contents);
        int selectedItemIndex = stack.getOrDefault(ModDataComponentTypes.QUIVER_SELECTED_INDEX, -1);
        ItemStack itemStack = builder.remove(selectedItemIndex);
        stack.remove(ModDataComponentTypes.QUIVER_SELECTED_INDEX);
        if (itemStack != null) {
            QuiverItem.playRemoveOneSound(player);
            stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
            return Optional.of(itemStack);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack, user);
            boolean bl = remainingUseTicks == i;
            if (bl || remainingUseTicks < i - 10 && remainingUseTicks % 2 == 0) {
                this.dropContentsOnUse(world, playerEntity, stack);
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 200;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BUNDLE;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        TooltipDisplayComponent tooltipDisplayComponent = stack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);
        int selectedItemIndex = stack.getOrDefault(ModDataComponentTypes.QUIVER_SELECTED_INDEX, -1);
        return tooltipDisplayComponent.shouldDisplay(ModDataComponentTypes.QUIVER_CONTENTS) ?
                Optional.ofNullable(stack.get(ModDataComponentTypes.QUIVER_CONTENTS))
                        .map(contents -> new QuiverTooltipData(contents, selectedItemIndex))
                : Optional.empty();
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        QuiverContentsComponent quiverContentsComponent = entity.getStack().get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (quiverContentsComponent != null) {
            entity.getStack().set(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
            ItemUsage.spawnItemContents(entity, quiverContentsComponent.iterateCopy());
        }
    }

    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(ModSoundEvents.DRAW_FROM_QUIVER, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(ModSoundEvents.PUT_INTO_QUIVER, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }

    private static void playInsertFailSound(Entity entity) {
        entity.playSound(ModSoundEvents.QUIVER_INSERT_FAILED, 1.0F, 1.0F);
    }

    private static void playDropContentsSound(Entity entity) {
        entity.playSound(ModSoundEvents.DRAW_FROM_QUIVER, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }

    private void onContentChanged(PlayerEntity user, ItemStack quiverStack) {
        quiverStack.inventoryTick(user.getWorld(), user, null);
        ScreenHandler screenHandler = user.currentScreenHandler;
        if (screenHandler != null) {
            screenHandler.onContentChanged(user.getInventory());
        }
    }

    public static ItemStack getFilledQuiver(PlayerEntity playerEntity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack equippedStack = playerEntity.getEquippedStack(equipmentSlot);
            if (equippedStack.getItem() instanceof QuiverItem && QuiverItem.getAmountFilled(equippedStack) > 0.0f) {
                return equippedStack;
            }
        }
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < playerEntity.getInventory().getMainStacks().size(); i++) {
            if (playerEntity.getInventory().getStack(i).getItem() instanceof QuiverItem && QuiverItem.getAmountFilled(playerEntity.getInventory().getStack(i)) > 0.0f) {
                itemStack = playerEntity.getInventory().getStack(i);
                break;
            }
        }
        return itemStack;
    }


}

