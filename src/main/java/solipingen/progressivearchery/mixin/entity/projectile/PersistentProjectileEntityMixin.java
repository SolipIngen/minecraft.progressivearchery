package solipingen.progressivearchery.mixin.entity.projectile;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
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
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;
import solipingen.progressivearchery.item.ModBowItem;


@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    @Shadow private double damage = 1.0;
    @Shadow private int punch;
    @Shadow @Nullable private List<Entity> piercingKilledEntities;

    @Invoker("clearPiercingStatus")
    public abstract void invokeClearPiercingStatus();


    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectedTick(CallbackInfo cbi) {
        if (this.isInsideWall() && this.getVelocity().length() > 0.0) {
            boolean bl = true;
            Box box = this.getBoundingBox();
            for (int y = MathHelper.floor(box.minY); y <= box.maxY; y++) {
                for (int x = MathHelper.floor(box.minX); x <= box.maxX; x++) {
                    for (int z = MathHelper.floor(box.minZ); z <= box.maxZ; z++) {
                        BlockPos blockPos = new BlockPos(x, y, z);
                        BlockState state = this.world.getBlockState(blockPos);
                        bl &= state.isSolidBlock(this.world, blockPos);
                        if (!bl) break;
                    }
                }
            }
            if (bl) {
                this.setVelocityClient(this.getVelocity().getX(), this.getVelocity().getY(), this.getVelocity().getZ());
                this.setVelocity(0.0, 0.0, 0.0);
            }
            ((PersistentProjectileEntity)(Object)this).setCritical(false);
            ((PersistentProjectileEntity)(Object)this).setPierceLevel((byte)0);
            this.invokeClearPiercingStatus();
        }
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

    @ModifyConstant(method = "age", constant = @Constant(intValue = 1200))
    private int modifiedConstant(int originalInt) {
        if (((PersistentProjectileEntity)(Object)this).pickupType == PickupPermission.DISALLOWED) {
            return 300;
        }
        return originalInt;
    }

    @ModifyConstant(method = "getDragInWater", constant = @Constant(floatValue = 0.6f))
    private float modifiedWaterDrag(float originalf) {
        return 1.0f/3.0f;
    }

    
}
