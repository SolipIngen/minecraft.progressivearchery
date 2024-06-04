package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


public class SpectralArrowEntity extends ModArrowEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(Items.SPECTRAL_ARROW);
    private int duration = 200;
    private static final double DAMAGE_AMOUNT = 2.25;

    
    public SpectralArrowEntity(EntityType<? extends SpectralArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world, DEFAULT_STACK);
    }

    public SpectralArrowEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.SPECTRAL_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z, stack);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    public SpectralArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.SPECTRAL_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner, stack);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
        if (world.isClient && !this.inGround) {
            world.addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, this.duration, 0);
        target.addStatusEffect(statusEffectInstance, this.getEffectCause());
    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    @Override
    public SoundEvent getHitSound() {
        return ModSoundEvents.COPPER_GOLDEN_ARROW_HIT;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Duration")) {
            this.duration = nbt.getInt("Duration");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Duration", this.duration);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            iLivingEntity.setStuckSpectralArrowCount(iLivingEntity.getStuckSpectralArrowCount() + 1);
        }
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }



}


