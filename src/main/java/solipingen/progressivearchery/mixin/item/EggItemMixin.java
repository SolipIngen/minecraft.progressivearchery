package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.Item;


@Mixin(EggItem.class)
public abstract class EggItemMixin extends Item {


    public EggItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/EggEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void redirectedSetVelocity(EggEntity eggEntity, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        eggEntity.setVelocity(shooter, pitch, yaw, roll, speed, divergence);
        eggEntity.addVelocity(shooter.getVelocity());
    }


    
}
