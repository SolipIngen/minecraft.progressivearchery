package solipingen.progressivearchery.mixin.entity.projectile;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {


    public ProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void injectedWriteCustomData(WriteView view, CallbackInfo cbi) {
        view.putBoolean("OnFire", this.isOnFire());
        if (this.isOnFire()) {
            view.putInt("FireTicks", this.getFireTicks());
        }
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void injectedReadCustomData(ReadView view, CallbackInfo cbi) {
        if (view.getBoolean("OnFire", false)) {
            this.setOnFireFor(MathHelper.floor((float)view.getInt("FireTicks", 0)/20));
        }
    }

    
}
