package solipingen.progressivearchery.item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;
import solipingen.progressivearchery.item.tooltip.QuiverTooltipData;
import solipingen.progressivearchery.component.QuiverContentsComponent;
import solipingen.progressivearchery.component.type.ModDataComponentTypes;
import solipingen.progressivearchery.sound.ModSoundEvents;


public class QuiverItem extends Item implements Equipment {
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.53f, 0.81f, 0.92f);

    
    public QuiverItem(Settings settings) {
        super(settings);
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
        if (clickType != ClickType.RIGHT) {
            return false;
        }
        else {
            QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
            if (quiverContentsComponent == null) {
                return false;
            }
            else {
                ItemStack itemStack = slot.getStack();
                QuiverContentsComponent.Builder builder = new QuiverContentsComponent.Builder(quiverContentsComponent);
                if (itemStack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack itemStack2 = builder.removeFirst();
                    if (itemStack2 != null) {
                        ItemStack itemStack3 = slot.insertStack(itemStack2);
                        builder.add(itemStack3);
                    }
                }
                else if (!(ModCrossbowItem.MOD_CROSSBOW_PROJECTILES.test(itemStack) || ModBowItem.TUBULAR_BOW_PROJECTILES.test(itemStack))) {
                    return false;
                }
                else if (itemStack.getItem().canBeNested()) {
                    int i = builder.add(slot, player);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }
                stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
                return true;
            }
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
            if (quiverContentsComponent == null) {
                return false;
            }
            else {
                QuiverContentsComponent.Builder builder = new QuiverContentsComponent.Builder(quiverContentsComponent);
                if (otherStack.isEmpty()) {
                    ItemStack itemStack = builder.removeFirst();
                    if (itemStack != null) {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    }
                }
                else if (!(ModCrossbowItem.MOD_CROSSBOW_PROJECTILES.test(otherStack) || ModBowItem.TUBULAR_BOW_PROJECTILES.test(otherStack))) {
                    return false;
                }
                else {
                    int i = builder.add(otherStack);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }
                stack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
                return true;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (dropAllQuiveredItems(itemStack, user)) {
            this.playDropContentsSound(user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient());
        }
        else {
            return TypedActionResult.fail(itemStack);
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
        return ITEM_BAR_COLOR;
    }

    private static boolean dropAllQuiveredItems(ItemStack stack, PlayerEntity player) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (quiverContentsComponent != null && !quiverContentsComponent.isEmpty()) {
            stack.set(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
            if (player instanceof ServerPlayerEntity) {
                quiverContentsComponent.iterateCopy().forEach((stackx) -> {
                    player.dropItem(stackx, true);
                });
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return !stack.contains(DataComponentTypes.HIDE_TOOLTIP) && !stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP) ? Optional.ofNullable(stack.get(ModDataComponentTypes.QUIVER_CONTENTS)).map(QuiverTooltipData::new) : Optional.empty();
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        QuiverContentsComponent quiverContentsComponent = stack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
        if (quiverContentsComponent != null) {
            int i = MathHelper.multiplyFraction(quiverContentsComponent.getOccupancy(), 384);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", new Object[]{i, 384}).formatted(Formatting.GRAY));
        }

    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        QuiverContentsComponent quiverContentsComponent = entity.getStack().get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (quiverContentsComponent != null) {
            entity.getStack().set(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
            ItemUsage.spawnItemContents(entity, quiverContentsComponent.iterateCopy());
        }
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.CHEST;
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

