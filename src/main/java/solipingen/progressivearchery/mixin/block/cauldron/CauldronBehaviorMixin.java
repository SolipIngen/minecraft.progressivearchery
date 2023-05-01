package solipingen.progressivearchery.mixin.block.cauldron;

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
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;
import solipingen.progressivearchery.block.enums.BlockPotionType;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.sound.ModSoundEvents;


@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {


    @Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void injectedRegisterBehavior(CallbackInfo cbi) {
        CauldronBehaviorMixin.registerCauldronPotionType();
        // Empty Cauldron
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.replace(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                Potion potionType = PotionUtil.getPotion(stack);

                if (potionType == Potions.WATER) {
                    world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState());
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                } else {
                    BlockPotionType blockpotionType = PotionCauldronBlock.BLOCK_POTION_TYPE.get(potionType);
                    world.setBlockState(pos, ModBlocks.POTION_CAULDRON.getDefaultState().with(PotionCauldronBlock.POTION_TYPE, blockpotionType));
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                }

                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.success(world.isClient);
        });
        // Potion Cauldron
        // Take
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                BlockPotionType blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
                Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType);
                
                PotionCauldronBlock.decrementFluidLevel(state, world, pos);
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), potionType)));

                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.success(world.isClient);
        });

        // Fill
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            BlockPotionType blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
            Potion potionType = PotionUtil.getPotion(stack);

            if (state.get(PotionCauldronBlock.LEVEL) == 3 || potionType != PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType)) {
                return ActionResult.PASS;
            }
            if (!world.isClient) {
                world.setBlockState(pos, (BlockState)state.cycle(PotionCauldronBlock.LEVEL));
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));

                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.success(world.isClient);
        });

        // Make Tipped Arrows
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.put(ModItems.GOLDEN_ARROW, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                BlockPotionType blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
                Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType);

                if (state.get(PotionCauldronBlock.TIPPING_NUMBER) <= 1) {
                    PotionCauldronBlock.decrementFluidLevel(state, world, pos);
                }
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.TIPPED_ARROW), potionType)));
                int currentTippingNumber = state.get(PotionCauldronBlock.TIPPING_NUMBER);
                world.setBlockState(pos, state.with(PotionCauldronBlock.TIPPING_NUMBER, currentTippingNumber - 1));

                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, ModSoundEvents.ARROW_TIPPED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.success(world.isClient);
        });

        // Make Tipped Kid Arrows
        PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR.put(ModItems.GOLDEN_KID_ARROW, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                BlockPotionType blockpotionType = state.get(PotionCauldronBlock.POTION_TYPE);
                Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockpotionType);
                
                if (state.get(PotionCauldronBlock.TIPPING_NUMBER) <= 1) {
                    PotionCauldronBlock.decrementFluidLevel(state, world, pos);
                }
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(ModItems.TIPPED_KID_ARROW), potionType)));
                int currentTippingNumber = state.get(PotionCauldronBlock.TIPPING_NUMBER);
                world.setBlockState(pos, state.with(PotionCauldronBlock.TIPPING_NUMBER, currentTippingNumber - 1));

                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.playSound(null, pos, ModSoundEvents.ARROW_TIPPED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.success(world.isClient);
        });
    }

    private static void registerCauldronPotionType() {

        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.EMPTY, BlockPotionType.EMPTY);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.WATER, BlockPotionType.WATER);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.THICK, BlockPotionType.THICK);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.MUNDANE, BlockPotionType.MUNDANE);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.AWKWARD, BlockPotionType.AWKWARD);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.NIGHT_VISION, BlockPotionType.NIGHT_VISION);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_NIGHT_VISION, BlockPotionType.LONG_NIGHT_VISION);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.INVISIBILITY, BlockPotionType.INVISIBILITY);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_INVISIBILITY, BlockPotionType.LONG_INVISIBILITY);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LEAPING, BlockPotionType.LEAPING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_LEAPING, BlockPotionType.LONG_LEAPING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_LEAPING, BlockPotionType.STRONG_LEAPING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.FIRE_RESISTANCE, BlockPotionType.FIRE_RESISTANCE);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_FIRE_RESISTANCE, BlockPotionType.LONG_FIRE_RESISTANCE);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.SWIFTNESS, BlockPotionType.SWIFTNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_SWIFTNESS, BlockPotionType.LONG_SWIFTNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_SWIFTNESS, BlockPotionType.STRONG_SWIFTNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.SLOWNESS, BlockPotionType.SLOWNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_SLOWNESS, BlockPotionType.LONG_SLOWNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_SLOWNESS, BlockPotionType.STRONG_SLOWNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.TURTLE_MASTER, BlockPotionType.TURTLE_MASTER);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_TURTLE_MASTER, BlockPotionType.LONG_TURTLE_MASTER);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_TURTLE_MASTER, BlockPotionType.STRONG_TURTLE_MASTER);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.WATER_BREATHING, BlockPotionType.WATER_BREATHING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_WATER_BREATHING, BlockPotionType.LONG_WATER_BREATHING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.HEALING, BlockPotionType.HEALING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_HEALING, BlockPotionType.STRONG_HEALING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.HARMING, BlockPotionType.HARMING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_HARMING, BlockPotionType.STRONG_HARMING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.POISON, BlockPotionType.POISON);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_POISON, BlockPotionType.LONG_POISON);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_POISON, BlockPotionType.STRONG_POISON);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.REGENERATION, BlockPotionType.REGENERATION);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_REGENERATION, BlockPotionType.LONG_REGENERATION);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_REGENERATION, BlockPotionType.STRONG_REGENERATION);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRENGTH, BlockPotionType.STRENGTH);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_STRENGTH, BlockPotionType.LONG_STRENGTH);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.STRONG_STRENGTH, BlockPotionType.STRONG_STRENGTH);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.WEAKNESS, BlockPotionType.WEAKNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_WEAKNESS, BlockPotionType.LONG_WEAKNESS);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LUCK, BlockPotionType.LUCK);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.SLOW_FALLING, BlockPotionType.SLOW_FALLING);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.LONG_SLOW_FALLING, BlockPotionType.LONG_SLOW_FALLING);

        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.EMPTY, Potions.EMPTY);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.WATER, Potions.WATER);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.MUNDANE, Potions.MUNDANE);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.THICK, Potions.THICK);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.AWKWARD, Potions.AWKWARD);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.NIGHT_VISION, Potions.NIGHT_VISION);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_NIGHT_VISION, Potions.LONG_NIGHT_VISION);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.INVISIBILITY, Potions.INVISIBILITY);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_INVISIBILITY, Potions.LONG_INVISIBILITY);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LEAPING, Potions.LEAPING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_LEAPING, Potions.LONG_LEAPING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_LEAPING, Potions.STRONG_LEAPING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.FIRE_RESISTANCE, Potions.FIRE_RESISTANCE);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.SWIFTNESS, Potions.SWIFTNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_SWIFTNESS, Potions.LONG_SWIFTNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_SWIFTNESS, Potions.STRONG_SWIFTNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.SLOWNESS, Potions.SLOWNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_SLOWNESS, Potions.LONG_SLOWNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_SLOWNESS, Potions.STRONG_SLOWNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.TURTLE_MASTER, Potions.TURTLE_MASTER);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_TURTLE_MASTER, Potions.LONG_TURTLE_MASTER);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_TURTLE_MASTER, Potions.STRONG_TURTLE_MASTER);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.WATER_BREATHING, Potions.WATER_BREATHING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_WATER_BREATHING, Potions.LONG_WATER_BREATHING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.HEALING, Potions.HEALING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_HEALING, Potions.STRONG_HEALING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.HARMING, Potions.HARMING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_HARMING, Potions.STRONG_HARMING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.POISON, Potions.POISON);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_POISON, Potions.LONG_POISON);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_POISON, Potions.STRONG_POISON);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.REGENERATION, Potions.REGENERATION);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_REGENERATION, Potions.LONG_REGENERATION);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_REGENERATION, Potions.STRONG_REGENERATION);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRENGTH, Potions.STRENGTH);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_STRENGTH, Potions.LONG_STRENGTH);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.STRONG_STRENGTH, Potions.STRONG_STRENGTH);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.WEAKNESS, Potions.WEAKNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_WEAKNESS, Potions.LONG_WEAKNESS);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LUCK, Potions.LUCK);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.SLOW_FALLING, Potions.SLOW_FALLING);
        PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(BlockPotionType.LONG_SLOW_FALLING, Potions.LONG_SLOW_FALLING);

    }


}
