package solipingen.progressivearchery.mixin.entity.projectile;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;


@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntity implements FlyingItemEntity {
    @Shadow @Final private static TrackedData<ItemStack> ITEM;
    @Shadow @Nullable private LivingEntity shooter;


    public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void injectedExplode(CallbackInfo cbi) {
        float f = 0.0f;
        ItemStack itemStack = this.dataTracker.get(ITEM);
        NbtCompound nbtCompound = itemStack.isEmpty() ? null : itemStack.getSubNbt("Fireworks");
        NbtList nbtList = nbtCompound != null ? nbtCompound.getList("Explosions", NbtElement.COMPOUND_TYPE) : null;
        if (nbtList != null && !nbtList.isEmpty()) {
            f = 5.0f + 4.0f*nbtList.size();
        }
        if (f > 0.0f && nbtList.size() > 0) {
            if (this.shooter != null) {
                this.shooter.damage(this.getDamageSources().fireworks(((FireworkRocketEntity)(Object)this), this.getOwner()), f);
            }
            Vec3d vec3d = this.getPos();
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(5.0 + 0.5*(nbtList.size() - 1)));
            for (LivingEntity livingEntity : list) {
                if (livingEntity == this.shooter || this.squaredDistanceTo(livingEntity) > (5.0 + 0.5*(nbtList.size() - 1))*(5.0 + 0.5*(nbtList.size() - 1))) continue;
                boolean reachBl = false;
                for (int i = 0; i < MathHelper.ceil(livingEntity.getHeight() + 1.0f); ++i) {
                    Vec3d vec3d2 = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(1.0/MathHelper.ceil(livingEntity.getHeight())*i), livingEntity.getZ());
                    BlockHitResult hitResult = this.world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
                    if (((HitResult)hitResult).getType() != HitResult.Type.MISS) continue;
                    reachBl = true;
                    break;
                }
                if (!reachBl) continue;
                int flameLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.FLAME, (LivingEntity)this.getOwner());
                if (flameLevel > 0) {
                    livingEntity.setOnFireFor(5);
                }
                float g = f*((5.0f + 0.5f*(nbtList.size() - 1) - this.distanceTo(livingEntity)) / (5.0f + 0.5f*(nbtList.size() - 1)));
                if (EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, (LivingEntity)this.getOwner()) > 0) {
                    g *= (1.0f + 0.2f*EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, (LivingEntity)this.getOwner()));
                }
                livingEntity.damage(this.getDamageSources().fireworks(((FireworkRocketEntity)(Object)this), this.getOwner()), g);
                int punchLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, (LivingEntity)this.getOwner());
                double d = 0.8*Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                Vec3d vec3d2 = this.getVelocity().normalize().multiply(punchLevel*d);
                if (vec3d2.lengthSquared() > 0.0) {
                    livingEntity.addVelocity(vec3d2.x, vec3d2.y, vec3d2.z);
                }
            }
        }
        cbi.cancel();
    }
    

}
