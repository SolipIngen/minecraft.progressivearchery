package solipingen.progressivearchery.mixin.entity.projectile;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.entity.projectile.arrow.CopperArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.DiamondArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.FlintArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.IronArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.ModArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.WoodenArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.CopperKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.DiamondKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.FlintKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.IronKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.WoodenKidArrowEntity;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    @Shadow private double damage = 1.0;
    @Shadow private int punch;
    @Shadow @Nullable private List<Entity> piercingKilledEntities;


    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean redirectedDamage(Entity entity, DamageSource damageSource, float originalf) {
        int amount = MathHelper.ceil(MathHelper.clamp(0.5*this.getVelocity().lengthSquared()*this.damage, 0.0, 2.147483647E9));
        if (((PersistentProjectileEntity)(Object)this).isCritical()) {
            int criticalBonusLevel = this.random.nextInt(MathHelper.ceil(0.1f*amount));
            amount = Math.min(MathHelper.ceil((1.0f + 0.1f*criticalBonusLevel)*amount), Integer.MAX_VALUE);
        }
        return entity.damage(damageSource, amount);
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V"))
    private void redirectedSetStuckArrowCount(LivingEntity livingEntity, int count) {
        if (!this.world.isClient && ((PersistentProjectileEntity)(Object)this).getPierceLevel() <= 0) {
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            if (((PersistentProjectileEntity)(Object)this) instanceof WoodenArrowEntity) {
                iLivingEntity.setStuckWoodenArrowCount(iLivingEntity.getStuckWoodenArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof WoodenKidArrowEntity) {
                iLivingEntity.setStuckWoodenKidArrowCount(iLivingEntity.getStuckWoodenKidArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof FlintArrowEntity) {
                iLivingEntity.setStuckFlintArrowCount(iLivingEntity.getStuckFlintArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof FlintKidArrowEntity) {
                iLivingEntity.setStuckFlintKidArrowCount(iLivingEntity.getStuckFlintKidArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof CopperArrowEntity) {
                iLivingEntity.setStuckCopperArrowCount(iLivingEntity.getStuckCopperArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof CopperKidArrowEntity) {
                iLivingEntity.setStuckCopperKidArrowCount(iLivingEntity.getStuckCopperKidArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof GoldenArrowEntity) {
                iLivingEntity.setStuckGoldenArrowCount(iLivingEntity.getStuckGoldenArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof GoldenKidArrowEntity) {
                iLivingEntity.setStuckGoldenKidArrowCount(iLivingEntity.getStuckGoldenKidArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof IronArrowEntity) {
                iLivingEntity.setStuckIronArrowCount(iLivingEntity.getStuckIronArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof IronKidArrowEntity) {
                iLivingEntity.setStuckIronKidArrowCount(iLivingEntity.getStuckIronKidArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof DiamondArrowEntity) {
                iLivingEntity.setStuckDiamondArrowCount(iLivingEntity.getStuckDiamondArrowCount() + 1);
            }
            else if (((PersistentProjectileEntity)(Object)this) instanceof DiamondKidArrowEntity) {
                iLivingEntity.setStuckDiamondKidArrowCount(iLivingEntity.getStuckDiamondKidArrowCount() + 1);
            }
            else {
                livingEntity.setStuckArrowCount(livingEntity.getStuckArrowCount() + 1);
            }
        }
    }

    @ModifyVariable(method = "onEntityHit", at = @At("STORE"), ordinal = 0)
    private Vec3d modifiedPunchVec3d(Vec3d originalVec3d, EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            double d = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            return this.getVelocity().normalize().multiply((double)this.punch * 0.6 * d);
        }
        return this.getVelocity().normalize().multiply((double)this.punch * 0.6);
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addVelocity(DDD)V"))
    private void redirectedAddVelocity(LivingEntity livingEntity, double originalX, double originalY, double originalZ) {
        double d = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
        Vec3d vec3d = this.getVelocity().normalize().multiply((double)this.punch * 0.6 * d);
        livingEntity.addVelocity(vec3d.x, vec3d.y, vec3d.z);
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    private void redirectedEntityHitPlaySound(PersistentProjectileEntity persistentProjectileEntity, SoundEvent originalSoundEvent, float originalVolume, float originalPitch) {
        if (persistentProjectileEntity instanceof ModArrowEntity) {
            EntityType<?> entityType = persistentProjectileEntity.getType();
            if (entityType == ModEntityTypes.COPPER_ARROW_ENTITY || entityType == ModEntityTypes.GOLDEN_ARROW_ENTITY || entityType == ModEntityTypes.SPECTRAL_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT, originalVolume, originalPitch);
            }
            else if (entityType == ModEntityTypes.IRON_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.IRON_ARROW_HIT, originalVolume, originalPitch);
            }
            else if (entityType == ModEntityTypes.DIAMOND_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.DIAMOND_ARROW_HIT, originalVolume, originalPitch);
            }
            else {
                persistentProjectileEntity.playSound(originalSoundEvent, originalVolume, originalPitch);
            }
        }
        else if (persistentProjectileEntity instanceof KidArrowEntity) {
            EntityType<?> entityType = persistentProjectileEntity.getType();
            if (entityType == ModEntityTypes.COPPER_KID_ARROW_ENTITY || entityType == ModEntityTypes.GOLDEN_KID_ARROW_ENTITY || entityType == ModEntityTypes.SPECTRAL_KID_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT, originalVolume, 1.4f*originalPitch);
            }
            else if (entityType == ModEntityTypes.IRON_KID_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.IRON_ARROW_HIT, originalVolume, 1.4f*originalPitch);
            }
            else if (entityType == ModEntityTypes.DIAMOND_KID_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.DIAMOND_ARROW_HIT, originalVolume, 1.4f*originalPitch);
            }
            else {
                persistentProjectileEntity.playSound(originalSoundEvent, originalVolume, 1.4f*originalPitch);
            }
        }
        else {
            persistentProjectileEntity.playSound(originalSoundEvent, originalVolume, originalPitch);
        }
    }

    @Inject(method = "onEntityHit", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z", opcode = Opcodes.GETFIELD), 
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V")))
    private void injectedKilledByBow(EntityHitResult entityHitResult, CallbackInfo cbi) {
        LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
        Entity entity2 = this.getOwner();
        if (this.piercingKilledEntities == null) {
            this.piercingKilledEntities = Lists.newArrayListWithCapacity(5);
            if (!livingEntity.isAlive()) {
                this.piercingKilledEntities.add(livingEntity);
            }
        }
        if (!this.world.isClient && this.piercingKilledEntities != null && entity2 instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity2;
            ItemStack mainHandStack = serverPlayerEntity.getMainHandStack();
            ItemStack offHandStack = serverPlayerEntity.getOffHandStack();
            if (mainHandStack.getItem() instanceof ModBowItem) {
                ModBowItem mainHandBow = (ModBowItem)mainHandStack.getItem();
                if (mainHandBow.getBowType() == 0) {
                    ModCriteria.KILLED_BY_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                }
                else if (mainHandBow.getBowType() == 1) {
                    ModCriteria.KILLED_BY_HORN_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                    if (serverPlayerEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                        ModCriteria.KILLED_BY_BLINDED_HORN_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                    }
                }
                else if (mainHandBow.getBowType() == 2) {
                    ModCriteria.KILLED_BY_LONGBOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                }
                else if (mainHandBow.getBowType() == 3) {
                    ModCriteria.KILLED_BY_TUBULAR_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                }
            }
            else if (offHandStack.getItem() instanceof ModBowItem) {
                ModBowItem offHandBow = (ModBowItem)offHandStack.getItem();
                if (offHandBow.getBowType() == 0) {
                    ModCriteria.KILLED_BY_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                }
                else if (offHandBow.getBowType() == 1) {
                    ModCriteria.KILLED_BY_HORN_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                    if (serverPlayerEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                        ModCriteria.KILLED_BY_BLINDED_HORN_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                    }
                }
                else if (offHandBow.getBowType() == 2) {
                    ModCriteria.KILLED_BY_LONGBOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                }
                else if (offHandBow.getBowType() == 3) {
                    ModCriteria.KILLED_BY_TUBULAR_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                }
            }
        }
    }

    @Redirect(method = "onBlockHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    private void redirectedBlockHitPlaySound(PersistentProjectileEntity persistentProjectileEntity, SoundEvent originalSoundEvent, float originalVolume, float originalPitch) {
        if (persistentProjectileEntity instanceof ModArrowEntity) {
            EntityType<?> entityType = persistentProjectileEntity.getType();
            if (entityType == ModEntityTypes.COPPER_ARROW_ENTITY || entityType == ModEntityTypes.GOLDEN_ARROW_ENTITY || entityType == ModEntityTypes.SPECTRAL_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT, originalVolume, originalPitch);
            }
            else if (entityType == ModEntityTypes.IRON_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.IRON_ARROW_HIT, originalVolume, originalPitch);
            }
            else if (entityType == ModEntityTypes.DIAMOND_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.DIAMOND_ARROW_HIT, originalVolume, 1.1f*originalPitch);
            }
            else {
                persistentProjectileEntity.playSound(originalSoundEvent, originalVolume, originalPitch);
            }
        }
        else if (persistentProjectileEntity instanceof KidArrowEntity) {
            EntityType<?> entityType = persistentProjectileEntity.getType();
            if (entityType == ModEntityTypes.COPPER_KID_ARROW_ENTITY || entityType == ModEntityTypes.GOLDEN_KID_ARROW_ENTITY || entityType == ModEntityTypes.SPECTRAL_KID_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT, originalVolume, 1.2f*originalPitch);
            }
            else if (entityType == ModEntityTypes.IRON_KID_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.IRON_ARROW_HIT, originalVolume, 1.2f*originalPitch);
            }
            else if (entityType == ModEntityTypes.DIAMOND_KID_ARROW_ENTITY) {
                persistentProjectileEntity.playSound(ModSoundEvents.DIAMOND_ARROW_HIT, originalVolume, 1.1f*1.2f*originalPitch);
            }
            else {
                persistentProjectileEntity.playSound(originalSoundEvent, originalVolume, 1.2f*originalPitch);
            }
        }
    }

    @ModifyConstant(method = "age", constant = @Constant(intValue = 1200))
    private int modifiedConstant(int originalInt) {
        if (((PersistentProjectileEntity)(Object)this).pickupType == PickupPermission.DISALLOWED) {
            return 300;
        }
        return originalInt;
    }

    @ModifyConstant(method = "getDragInWater", constant = @Constant(floatValue = 0.6f))
    private float modifiedWaterDrag(float originalf) {
        return 0.67f;
    }

    
}
