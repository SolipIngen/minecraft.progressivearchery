package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SnowballItem;


@Mixin(SnowballItem.class)
public abstract class SnowballItemMixin extends Item {
 
    
    public SnowballItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/SnowballEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void redirectedSetVelocity(SnowballEntity snowballEntity, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        snowballEntity.setVelocity(shooter, pitch, yaw, roll, speed, divergence);
        snowballEntity.addVelocity(shooter.getVelocity());
    }


}
