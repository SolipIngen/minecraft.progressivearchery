package solipingen.progressivearchery.mixin.block.cauldron;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.sound.ModSoundEvents;


@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {


    @Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void injectedRegisterBehavior(CallbackInfo cbi) {
        // Empty Cauldron
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().replace(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                Potion potionType = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().get().value();
                if (potionType == Potions.WATER.value()) {
                    world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState());
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                }
                else {
                    int blockpotionType = PotionCauldronBlock.BLOCK_POTION_TYPE.get(potionType);
                    world.setBlockState(pos, ModBlocks.POTION_CAULDRON.getDefaultState().with(PotionCauldronBlock.POTION_TYPE, blockpotionType));
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                }
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.SUCCESS;
        });
        // Potion Cauldron
        // Take
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.map().put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                int blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
                Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType);
                PotionCauldronBlock.decrementFluidLevel(state, world, pos);
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionContentsComponent.createStack(Items.POTION, Registries.POTION.getEntry(potionType))));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.SUCCESS;
        });

        // Fill
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.map().put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            int blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
            Potion potionType = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().get().value();
            if (state.get(PotionCauldronBlock.LEVEL) == 3 || potionType != PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType)) {
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            }
            if (!world.isClient()) {
                world.setBlockState(pos, (BlockState)state.cycle(PotionCauldronBlock.LEVEL));
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.SUCCESS;
        });

        // Make Tipped Arrows
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.map().put(ModItems.GOLDEN_ARROW, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                int blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
                Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType);
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionContentsComponent.createStack(Items.TIPPED_ARROW, Registries.POTION.getEntry(potionType))));
                int currentTippingNumber = state.get(PotionCauldronBlock.TIPPING_NUMBER);
                world.setBlockState(pos, state.with(PotionCauldronBlock.TIPPING_NUMBER, currentTippingNumber - 1));
                if (state.get(PotionCauldronBlock.TIPPING_NUMBER) <= 1) {
                    PotionCauldronBlock.decrementFluidLevel(state, world, pos);
                }
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, ModSoundEvents.ARROW_TIPPED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.SUCCESS;
        });

        // Make Tipped Kid Arrows
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.map().put(ModItems.GOLDEN_KID_ARROW, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                int blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
                Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType);
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionContentsComponent.createStack(ModItems.TIPPED_KID_ARROW, Registries.POTION.getEntry(potionType))));
                int currentTippingNumber = state.get(PotionCauldronBlock.TIPPING_NUMBER);
                world.setBlockState(pos, state.with(PotionCauldronBlock.TIPPING_NUMBER, currentTippingNumber - 1));
                if (state.get(PotionCauldronBlock.TIPPING_NUMBER) <= 1) {
                    PotionCauldronBlock.decrementFluidLevel(state, world, pos);
                }
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, ModSoundEvents.ARROW_TIPPED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.SUCCESS;
        });
    }



}
