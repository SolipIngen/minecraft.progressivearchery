package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;


@Mixin(ExperienceBottleItem.class)
public class ExperienceBottleItemMixin extends Item {


    public ExperienceBottleItemMixin(Settings settings) {
        super(settings);
    }
    
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/ExperienceBottleEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void redirectedSetVelocity(ExperienceBottleEntity experienceBottleEntity, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        experienceBottleEntity.setVelocity(shooter, pitch, yaw, roll, speed, divergence);
        experienceBottleEntity.addVelocity(shooter.getVelocity());
    }
    


}

