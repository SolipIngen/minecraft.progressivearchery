package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;


@Mixin(EnderPearlItem.class)
public abstract class EnderPearlItemMixin extends Item {


    public EnderPearlItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/EnderPearlEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void redirectedSetVelocity(EnderPearlEntity enderPearlEntity, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        enderPearlEntity.setVelocity(shooter, pitch, yaw, roll, speed, divergence);
        enderPearlEntity.addVelocity(shooter.getVelocity());
    }



    
}
