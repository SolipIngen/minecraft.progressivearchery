package solipingen.progressivearchery.mixin.block.dispenser;

import java.util.Map;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.LavaCauldronBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;


@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin extends BlockWithEntity {
    @Shadow @Final private static Map<Item, DispenserBehavior> BEHAVIORS;


    protected DispenserBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void injectedRegisterBehavior(ItemConvertible provider, DispenserBehavior behavior, CallbackInfo cbi) {
        BEHAVIORS.replace(Items.BUCKET, new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ItemStack itemStack;
                BlockPos blockPos;
                ServerWorld worldAccess = pointer.world();
                BlockState blockState = worldAccess.getBlockState(blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING)));
                Block block = blockState.getBlock();
                if (block instanceof FluidDrainable) {
                    itemStack = ((FluidDrainable)((Object)block)).tryDrainFluid(null, worldAccess, blockPos, blockState);
                    if (itemStack.isEmpty()) {
                        return super.dispenseSilently(pointer, stack);
                    }
                }
                else if (block instanceof LeveledCauldronBlock && ((LeveledCauldronBlock)block).isFull(blockState)) {
                    if (block == Blocks.POWDER_SNOW_CAULDRON) {
                        itemStack = new ItemStack(Items.POWDER_SNOW_BUCKET);
                        worldAccess.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                        worldAccess.playSound(null, blockPos, SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    }
                    else {
                        itemStack = new ItemStack(Items.WATER_BUCKET);
                        worldAccess.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                        worldAccess.playSound(null, blockPos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    }
                }
                else if (block instanceof LavaCauldronBlock) {
                    itemStack = new ItemStack(Items.LAVA_BUCKET);
                    worldAccess.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                    worldAccess.playSound(null, blockPos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                }
                else {
                    return super.dispenseSilently(pointer, stack);
                }
                worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                Item item = itemStack.getItem();
                stack.decrement(1);
                if (stack.isEmpty()) {
                    return new ItemStack(item);
                }
                if (((DispenserBlockEntity)pointer.blockEntity()).addToFirstFreeSlot(new ItemStack(item)) < 0) {
                    this.fallbackBehavior.dispense(pointer, new ItemStack(item));
                }
                return stack;
            }
        });
        BEHAVIORS.replace(Items.GLASS_BOTTLE, new FallibleItemDispenserBehavior(){
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            private ItemStack tryPutFilledBottle(BlockPointer pointer, ItemStack emptyBottleStack, ItemStack filledBottleStack) {
                emptyBottleStack.decrement(1);
                if (emptyBottleStack.isEmpty()) {
                    pointer.world().emitGameEvent(null, GameEvent.FLUID_PICKUP, pointer.pos());
                    return filledBottleStack.copy();
                }
                if (((DispenserBlockEntity)pointer.blockEntity()).addToFirstFreeSlot(filledBottleStack.copy()) < 0) {
                    this.fallbackBehavior.dispense(pointer, filledBottleStack.copy());
                }
                return emptyBottleStack;
            }

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                this.setSuccess(false);
                ServerWorld serverWorld = pointer.world();
                BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                BlockState blockState = serverWorld.getBlockState(blockPos);
                if (blockState.isIn(BlockTags.BEEHIVES, state -> state.contains(BeehiveBlock.HONEY_LEVEL) && state.getBlock() instanceof BeehiveBlock) && blockState.get(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    ((BeehiveBlock)blockState.getBlock()).takeHoney(serverWorld, blockState, blockPos, null, BeehiveBlockEntity.BeeState.BEE_RELEASED);
                    this.setSuccess(true);
                    serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    serverWorld.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    return this.tryPutFilledBottle(pointer, stack, new ItemStack(Items.HONEY_BOTTLE));
                }
                if (serverWorld.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    this.setSuccess(true);
                    serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    serverWorld.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    return this.tryPutFilledBottle(pointer, stack, PotionContentsComponent.createStack(Items.POTION, Potions.WATER));
                }
                
                if (blockState.getBlock() instanceof LeveledCauldronBlock && !blockState.isOf(Blocks.POWDER_SNOW_CAULDRON)) {
                    int waterCauldronLevel = blockState.get(LeveledCauldronBlock.LEVEL);
                    if (waterCauldronLevel > 1) {
                        this.setSuccess(true);
                        serverWorld.setBlockState(blockPos, blockState.with(LeveledCauldronBlock.LEVEL, waterCauldronLevel - 1));
                        serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        serverWorld.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    }
                    else if (waterCauldronLevel == 1) {
                        this.setSuccess(true);
                        serverWorld.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                        serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        serverWorld.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    }
                    return this.tryPutFilledBottle(pointer, stack, PotionContentsComponent.createStack(Items.POTION, Potions.WATER));
                }
                else if (blockState.getBlock() instanceof PotionCauldronBlock) {
                    int potionCauldronLevel = blockState.get(PotionCauldronBlock.LEVEL);
                    if (potionCauldronLevel > 1) {
                        this.setSuccess(true);
                        serverWorld.setBlockState(blockPos, blockState.with(PotionCauldronBlock.LEVEL, potionCauldronLevel - 1));
                        serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        serverWorld.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    }
                    else if (potionCauldronLevel == 1) {
                        this.setSuccess(true);
                        serverWorld.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                        serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        serverWorld.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    }
                    int blockPotionType = blockState.get(PotionCauldronBlock.POTION_TYPE);
                    Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockPotionType);
                    return this.tryPutFilledBottle(pointer, stack, this.tryPutFilledBottle(pointer, stack, PotionContentsComponent.createStack(Items.POTION, Registries.POTION.getEntry(potionType))));
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
        BEHAVIORS.replace(Items.POTION, new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallback = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ServerWorld serverWorld = pointer.world();
                BlockPos blockPos = pointer.pos();
                BlockPos blockPos2 = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                Potion potionType = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().get().value();
                if (potionType == Potions.WATER.value() && serverWorld.getBlockState(blockPos2).isIn(BlockTags.CONVERTABLE_TO_MUD)) {
                    if (!serverWorld.isClient) {
                        for (int i = 0; i < 5; ++i) {
                            serverWorld.spawnParticles(ParticleTypes.SPLASH, (double)blockPos.getX() + serverWorld.random.nextDouble(), blockPos.getY() + 1, (double)blockPos.getZ() + serverWorld.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                        }
                    }
                    serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                    serverWorld.setBlockState(blockPos2, Blocks.MUD.getDefaultState());
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                else if (potionType == Potions.WATER.value() && serverWorld.getBlockState(blockPos2).isOf(Blocks.CAULDRON)) {
                    serverWorld.setBlockState(blockPos2, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 1));
                    serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                else if (potionType == Potions.WATER.value() && serverWorld.getBlockState(blockPos2).isOf(Blocks.WATER_CAULDRON)) {
                    int waterCauldronLevel = serverWorld.getBlockState(blockPos2).get(LeveledCauldronBlock.LEVEL);
                    if (waterCauldronLevel < 3) {
                        serverWorld.setBlockState(blockPos2, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, waterCauldronLevel + 1));
                        serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                        return new ItemStack(Items.GLASS_BOTTLE);
                    }
                }
                int blockPotionType = PotionCauldronBlock.BLOCK_POTION_TYPE.get(potionType);
                Block facedBlock = serverWorld.getBlockState(blockPos2).getBlock();
                if (facedBlock instanceof CauldronBlock) {
                    serverWorld.setBlockState(blockPos2, ModBlocks.POTION_CAULDRON.getDefaultState().with(PotionCauldronBlock.LEVEL, 1).with(PotionCauldronBlock.POTION_TYPE, blockPotionType));
                    serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                else if (facedBlock instanceof PotionCauldronBlock) {
                    BlockState currentBlockState = serverWorld.getBlockState(blockPos2);
                    int potionLevel = currentBlockState.get(PotionCauldronBlock.LEVEL);
                    int containedBlockPotionType = currentBlockState.get(PotionCauldronBlock.POTION_TYPE);
                    if (blockPotionType == containedBlockPotionType && potionLevel < 3) {
                        serverWorld.setBlockState(blockPos2, currentBlockState.with(PotionCauldronBlock.LEVEL, potionLevel + 1));
                        serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                        return new ItemStack(Items.GLASS_BOTTLE);
                    }
                }
                return this.fallback.dispense(pointer, stack);
            }
        });
    
    }

    private static Vec3d setEntityPosition(BlockPointer pointer, EntityType<?> entityType, Direction direction) {
        return pointer.centerPos().add((double)direction.getOffsetX() * (0.5000099999997474 - (double)entityType.getWidth() / 2.0), (double)direction.getOffsetY() * (0.5000099999997474 - (double)entityType.getHeight() / 2.0) - (double)entityType.getHeight() / 2.0, (double)direction.getOffsetZ() * (0.5000099999997474 - (double)entityType.getWidth() / 2.0));
    }


    
}
