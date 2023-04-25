package solipingen.progressivearchery.mixin.entity.projectile;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
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
import solipingen.progressivearchery.util.interfaces.mixin.entity.projectile.FireworkRocketEntityInterface;


@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntity implements FlyingItemEntity, FireworkRocketEntityInterface {
    @Shadow @Final private static TrackedData<ItemStack> ITEM;
    @Shadow @Nullable private LivingEntity shooter;
    private boolean flameBl = false;
    private int powerLevel = 0;
    private int punchLevel = 0;


    public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void injectedInit(World world, @Nullable Entity entity, double x, double y, double z, ItemStack itemStack, CallbackInfo cbi) {
        if (entity != null) {
            this.addVelocity(entity.getVelocity());
        }
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
                float g = f*((5.0f + 0.5f*(nbtList.size() - 1) - this.distanceTo(livingEntity)) / (5.0f + 0.5f*(nbtList.size() - 1)))*(1.0f + 0.2f*this.powerLevel);
                if (this.flameBl) {
                    livingEntity.setOnFireFor(5);
                }
                double d = 0.8*Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                Vec3d vec3d2 = this.getVelocity().normalize().multiply(this.punchLevel*d);
                if (vec3d2.lengthSquared() > 0.0) {
                    livingEntity.addVelocity(vec3d2.x, vec3d2.y, vec3d2.z);
                }
                livingEntity.damage(this.getDamageSources().fireworks(((FireworkRocketEntity)(Object)this), this.getOwner()), g);   
            }
        }
        cbi.cancel();
    }

    @Override
    public void setFlame(boolean flame) {
        this.flameBl = flame;
    }

    @Override
    public void setPower(int power) {
        this.powerLevel = power;
    }

    @Override
    public void setPunch(int punch) {
        this.punchLevel = punch;
    }
    

}
