package solipingen.progressivearchery.block.cauldron;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.block.enums.BlockPotionType;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.state.property.ModProperties;


public class PotionCauldronBlock extends AbstractCauldronBlock {
    public static final Map<Item, CauldronBehavior> POTION_CAULDRON_BEHAVIOR = new HashMap<>();
    public static final Map<Potion, BlockPotionType> BLOCK_POTION_TYPE = new HashMap<>();
    public static final Map<BlockPotionType, Potion> POTION_TYPE_IN_BLOCK = new HashMap<>();
    public static final int field_31107 = 1;
    public static final int field_31108 = 3;
    public static final IntProperty LEVEL = Properties.LEVEL_3;
    public static final IntProperty TIPPING_NUMBER = IntProperty.of("tipping_number", 1, 21);
    public static final ToIntFunction<BlockState> LEVEL_TO_LUMINANCE = state -> 4*state.get(LEVEL);
    public static final EnumProperty<BlockPotionType> POTION_TYPE = ModProperties.BLOCK_POTION_TYPE;


    public PotionCauldronBlock(AbstractBlock.Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, behaviorMap);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(LEVEL, 1).with(TIPPING_NUMBER, 21));
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
        int i = state.get(LEVEL) - 1;
        BlockState blockState = i == 0 ? Blocks.CAULDRON.getDefaultState() : (BlockState)state.with(LEVEL, i).with(TIPPING_NUMBER, 21);
        world.setBlockState(pos, blockState);
        world.playSound(null, pos, ModSoundEvents.ARROW_TIPPED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
    }

    protected void onFireCollision(BlockState state, World world, BlockPos pos) {
        PotionCauldronBlock.decrementFluidLevel(state, world, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
        builder.add(TIPPING_NUMBER);
        builder.add(POTION_TYPE);
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.get(LEVEL) == 3;
    }

    @Override
    protected double getFluidHeight(BlockState state) {
        return (6.0 + (double)state.get(LEVEL).intValue() * 3.0) / 16.0;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LEVEL);
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && this.isEntityTouchingFluid(state, pos, entity)) {
            int level = state.get(LEVEL);
            BlockPotionType blockpotionType = state.get(POTION_TYPE);
            if (entity instanceof LivingEntity) {
                for (StatusEffectInstance statusEffectInstance : POTION_TYPE_IN_BLOCK.get(blockpotionType).getEffects()) {
                    ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(), 
                        Math.max(level*statusEffectInstance.getDuration() / 8, 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
                }
            }
            if (entity.isOnFire()) {
                entity.extinguish();
                if (entity.canModifyAt(world, pos)) {
                    this.onFireCollision(state, world, pos);
                }
            }
        }
    }

    
}

