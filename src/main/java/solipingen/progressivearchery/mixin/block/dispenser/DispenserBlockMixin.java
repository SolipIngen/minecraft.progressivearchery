package solipingen.progressivearchery.mixin.block.dispenser;

import java.util.Map;

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
import net.minecraft.block.PowderSnowCauldronBlock;
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
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;
import solipingen.progressivearchery.block.enums.BlockPotionType;


@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin extends BlockWithEntity {
    @Shadow @Final private static Map<Item, DispenserBehavior> BEHAVIORS;


    protected DispenserBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void injectedRegisterBehavior(ItemConvertible provider, DispenserBehavior behavior, CallbackInfo cbi) {
        
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.EMPTY, BlockPotionType.EMPTY);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.WATER, BlockPotionType.WATER);
        PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(Potions.MUNDANE, BlockPotionType.THICK);
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

        BEHAVIORS.replace(Items.FIREWORK_ROCKET, new ItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
                FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity((World)pointer.getWorld(), stack, pointer.getX(), pointer.getY(), pointer.getX(), true);
                int dispenserPowerLevel = pointer.getWorld().getReceivedRedstonePower(pointer.getPos());
                DispenserBehavior.setEntityPosition(pointer, fireworkRocketEntity, direction);
                fireworkRocketEntity.setVelocity(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ(), Math.min(0.2f*dispenserPowerLevel, 3.0f), 1.0f);
                pointer.getWorld().spawnEntity(fireworkRocketEntity);
                stack.decrement(1);
                return stack;
            }

            @Override
            protected void playSound(BlockPointer pointer) {
                pointer.getWorld().syncWorldEvent(WorldEvents.FIREWORK_ROCKET_SHOOTS, pointer.getPos(), 0);
            }
        });
        BEHAVIORS.replace(Items.BUCKET, new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ItemStack itemStack;
                BlockPos blockPos;
                ServerWorld worldAccess = pointer.getWorld();
                BlockState blockState = worldAccess.getBlockState(blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING)));
                Block block = blockState.getBlock();
                if (block instanceof FluidDrainable) {
                    itemStack = ((FluidDrainable)((Object)block)).tryDrainFluid(worldAccess, blockPos, blockState);
                    if (itemStack.isEmpty()) {
                        return super.dispenseSilently(pointer, stack);
                    }
                }
                else if (block instanceof LeveledCauldronBlock && ((LeveledCauldronBlock)block).isFull(blockState)) {
                    if (block instanceof PowderSnowCauldronBlock) {
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
                if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(new ItemStack(item)) < 0) {
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
                    pointer.getWorld().emitGameEvent(null, GameEvent.FLUID_PICKUP, pointer.getPos());
                    return filledBottleStack.copy();
                }
                if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(filledBottleStack.copy()) < 0) {
                    this.fallbackBehavior.dispense(pointer, filledBottleStack.copy());
                }
                return emptyBottleStack;
            }

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                this.setSuccess(false);
                ServerWorld serverWorld = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
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
                    return this.tryPutFilledBottle(pointer, stack, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                }
                
                if (blockState.getBlock() instanceof LeveledCauldronBlock && !(blockState.getBlock() instanceof PowderSnowCauldronBlock)) {
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
                    return this.tryPutFilledBottle(pointer, stack, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
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
                    BlockPotionType blockPotionType = blockState.get(PotionCauldronBlock.POTION_TYPE);
                    Potion potionType = PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(blockPotionType);
                    return this.tryPutFilledBottle(pointer, stack, PotionUtil.setPotion(new ItemStack(Items.POTION), potionType));
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
        BEHAVIORS.replace(Items.POTION, new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallback = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ServerWorld serverWorld = pointer.getWorld();
                BlockPos blockPos = pointer.getPos();
                BlockPos blockPos2 = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                if (PotionUtil.getPotion(stack) == Potions.WATER && serverWorld.getBlockState(blockPos2).isIn(BlockTags.CONVERTABLE_TO_MUD)) {
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
                else if (PotionUtil.getPotion(stack) == Potions.WATER && serverWorld.getBlockState(blockPos2).isOf(Blocks.CAULDRON)) {
                    serverWorld.setBlockState(blockPos2, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 1));
                    serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                else if (PotionUtil.getPotion(stack) == Potions.WATER && serverWorld.getBlockState(blockPos2).isOf(Blocks.WATER_CAULDRON)) {
                    int waterCauldronLevel = serverWorld.getBlockState(blockPos2).get(LeveledCauldronBlock.LEVEL);
                    if (waterCauldronLevel < 3) {
                        serverWorld.setBlockState(blockPos2, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, waterCauldronLevel + 1));
                        serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                        return new ItemStack(Items.GLASS_BOTTLE);
                    }
                }
                Potion potionType = PotionUtil.getPotion(stack);
                BlockPotionType blockPotionType = PotionCauldronBlock.BLOCK_POTION_TYPE.get(potionType);
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
                    BlockPotionType containedBlockPotionType = currentBlockState.get(PotionCauldronBlock.POTION_TYPE);
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


    
}
