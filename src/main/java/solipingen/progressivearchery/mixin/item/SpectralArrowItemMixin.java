package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.arrow.SpectralArrowEntity;


@Mixin(SpectralArrowItem.class)
public abstract class SpectralArrowItemMixin extends ArrowItem {


    public SpectralArrowItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createArrow", at = @At("HEAD"), cancellable = true)
    private void injectedCreateArrow(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cbireturn) {
        PersistentProjectileEntity arrowEntity = new SpectralArrowEntity(world, shooter);
        cbireturn.setReturnValue(arrowEntity);
    }


    
}
