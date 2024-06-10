package solipingen.progressivearchery.mixin.entity.projectile;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
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

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.99f))
    private float modifiedDragInAir(float dragInAir) {
        return 0.999f;
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
                        BlockState state = this.getWorld().getBlockState(blockPos);
                        bl &= state.isSolidBlock(this.getWorld(), blockPos);
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

    @ModifyArg(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float redirectedDamage(float originalf) {
        float amount = MathHelper.clamp((float)(this.damage*this.getVelocity().length()), 0.0f, 2.147483647E9f);
        if (((PersistentProjectileEntity)(Object)this).isCritical()) {
            float criticalBonus = 1.0f + 0.25f*Math.abs((float)this.random.nextGaussian());
            amount *= criticalBonus;
        }
        return amount;
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addVelocity(DDD)V"))
    private void redirectedPunchVec3d(LivingEntity livingEntity, double x, double y, double z) {
        double d = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
        Vec3d punchVec3d = this.getVelocity().normalize().multiply((double)this.punch*0.6*d);
        livingEntity.addVelocity(punchVec3d);
    }

    @Inject(method = "onEntityHit", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z", opcode = Opcodes.GETFIELD), 
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V")))
    private void injectedKilledByBow(EntityHitResult entityHitResult, CallbackInfo cbi) {
        World world = this.getWorld();
        Entity entity2 = this.getOwner();
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            if (this.piercingKilledEntities == null) {
                this.piercingKilledEntities = Lists.newArrayListWithCapacity(5);
                if (!livingEntity.isAlive()) {
                    this.piercingKilledEntities.add(livingEntity);
                }
            }
        }
        if (!world.isClient && this.piercingKilledEntities != null && entity2 instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity2;
            ItemStack mainHandStack = serverPlayerEntity.getMainHandStack();
            ItemStack offHandStack = serverPlayerEntity.getOffHandStack();
            if (mainHandStack.isOf(Items.BOW) || offHandStack.isOf(Items.BOW)) {
                ModCriteria.KILLED_BY_BOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
            }
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
            else if (offHandStack.getItem() instanceof ModBowItem || offHandStack.isOf(Items.BOW)) {
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

    @Inject(method = "applyEnchantmentEffects", at = @At("HEAD"), cancellable = true)
    private void injectedApplyEnchantmentEffects(LivingEntity entity, float damageModifier, CallbackInfo cbi) {
        int i = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, entity);
        int j = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, entity);
        ((PersistentProjectileEntity)(Object)this).setDamage((double)(damageModifier*2.0f) + this.random.nextTriangular((double)this.getWorld().getDifficulty().getId()*0.11, 0.57425));
        if (i > 0) {
            ((PersistentProjectileEntity)(Object)this).setDamage(((PersistentProjectileEntity)(Object)this).getDamage() + 0.5*i);
        }
        if (j > 0) {
            ((PersistentProjectileEntity)(Object)this).setPunch(j);
        }
        if (EnchantmentHelper.getEquipmentLevel(Enchantments.FLAME, entity) > 0) {
            this.setOnFireFor(100);
        }
        cbi.cancel();
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
        return 2.0f/3.0f;
    }

    
}
