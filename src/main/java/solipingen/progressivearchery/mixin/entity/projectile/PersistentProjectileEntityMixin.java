package solipingen.progressivearchery.mixin.entity.projectile;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
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
    @Shadow private double damage = 2.0;
    @Shadow @Nullable private List<Entity> piercingKilledEntities;
    @Shadow @Nullable private ItemStack weapon;

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
            this.invokeClearPiercingStatus();
        }
    }

    @ModifyVariable(method = "onEntityHit", at = @At("STORE"), index = 9)
    private long modifiedCriticalBonus(long l, EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = (float)this.getVelocity().length();
        double d = this.damage;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getDamageSources().arrow((PersistentProjectileEntity)(Object)this, entity2 != null ? entity2 : this);
        if (this.getWeaponStack() != null && this.getWorld() instanceof ServerWorld serverWorld) {
            d = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, (float)d);
        }
        int i = MathHelper.ceil(MathHelper.clamp((double)f*d, 0.0, 2.147483647E9));
        return Math.round(0.25f*Math.abs((float)this.random.nextGaussian())*i);
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
        if (!world.isClient() && this.piercingKilledEntities != null && entity2 instanceof ServerPlayerEntity) {
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

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void injectedKnockback(LivingEntity target, DamageSource source, CallbackInfo cbi) {
        double d = 0.0;
        if (this.weapon != null && this.getWorld() instanceof ServerWorld serverWorld) {
            d = EnchantmentHelper.modifyKnockback(serverWorld, this.weapon, target, source, 0.0f);
        }
        if (d > 0.0) {
            double e = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            Vec3d vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply(d * 0.6 * e);
            if (vec3d.lengthSquared() > 0.0) {
                target.addVelocity(vec3d.x, vec3d.y, vec3d.z);
            }
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
