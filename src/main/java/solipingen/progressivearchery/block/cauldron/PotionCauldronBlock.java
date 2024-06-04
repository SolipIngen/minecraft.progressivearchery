package solipingen.progressivearchery.block.cauldron;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.state.property.ModProperties;


public class PotionCauldronBlock extends AbstractCauldronBlock {
    public static final MapCodec<PotionCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> 
        instance.group((Biome.Precipitation.CODEC.fieldOf("precipitation")).forGetter(block -> block.precipitation),
            (CauldronBehavior.CODEC.fieldOf("interactions")).forGetter(block -> block.behaviorMap), PotionCauldronBlock.createSettingsCodec())
        .apply(instance, PotionCauldronBlock::new));
    public static final CauldronBehavior.CauldronBehaviorMap POTION_CAULDRON_BEHAVIOR = CauldronBehavior.createMap("potion");
    public static final Map<Potion, Integer> BLOCK_POTION_TYPE = new HashMap<>();
    public static final Map<Integer, Potion> POTION_TYPE_IN_BLOCK = new HashMap<>();
    public static final IntProperty LEVEL = Properties.LEVEL_3;
    public static final int TIPPING_PER_LEVEL = 8;
    public static final IntProperty TIPPING_NUMBER = IntProperty.of("tipping_number", 0, TIPPING_PER_LEVEL);
    public static final ToIntFunction<BlockState> LEVEL_TO_LUMINANCE = state -> 4*state.get(LEVEL);
    public static final IntProperty POTION_TYPE = ModProperties.BLOCK_POTION_TYPE;
    private final Biome.Precipitation precipitation;


    public PotionCauldronBlock(Biome.Precipitation precipitation, CauldronBehavior.CauldronBehaviorMap behaviorMap, AbstractBlock.Settings settings) {
        super(settings, behaviorMap);
        this.precipitation = precipitation;
        this.setDefaultState((this.stateManager.getDefaultState()).with(LEVEL, 1).with(TIPPING_NUMBER, 8).with(POTION_TYPE, 1));
        int index = 1;
        for (Potion potion : Registries.POTION) {
            PotionCauldronBlock.BLOCK_POTION_TYPE.putIfAbsent(potion, index);
            PotionCauldronBlock.POTION_TYPE_IN_BLOCK.putIfAbsent(index, potion);
            index++;
        }
    }

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> getCodec() {
        return CODEC;
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
        int i = state.get(LEVEL) - 1;
        BlockState blockState = i == 0 ? Blocks.CAULDRON.getDefaultState() : (BlockState)state.with(LEVEL, i).with(TIPPING_NUMBER, 8);
        world.setBlockState(pos, blockState);
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
        return (6.0 + state.get(LEVEL).intValue()*3.0) / 16.0;
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
            int blockpotionType = state.get(POTION_TYPE);
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

    @Override
    public Item asItem() {
        return Items.CAULDRON;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int blockpotionType = state.get(POTION_TYPE);
        RegistryEntry<Potion> potionEntry = Registries.POTION.getEntry(POTION_TYPE_IN_BLOCK.get(blockpotionType));
        int color = PotionContentsComponent.getColor(potionEntry);
        int level = state.get(LEVEL);
        if (color == -1 || level <= 0 || random.nextInt(8 - 2*level) > 0) {
            return;
        }
        double d = (double)(color >> 16 & 0xFF) / 255.0;
        double e = (double)(color >> 8 & 0xFF) / 255.0;
        double f = (double)(color >> 0 & 0xFF) / 255.0;
        for (int j = 0; j < level; ++j) {
            world.addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, color), pos.getX() + 0.5, pos.getY() + 15.0/16.0, pos.getZ() + 0.5, d, e, f);
        }
    }

    
}

