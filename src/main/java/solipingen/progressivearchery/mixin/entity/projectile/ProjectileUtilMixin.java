package solipingen.progressivearchery.mixin.entity.projectile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;


@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin {


    @Inject(method = "createArrowProjectile", at = @At("HEAD"), cancellable = true)
    private static void injectedCreateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier, CallbackInfoReturnable<PersistentProjectileEntity> cbireturn) {
        ModArrowItem arrowItem = (ModArrowItem)(stack.getItem() instanceof ModArrowItem ? stack.getItem() : ModItems.WOODEN_ARROW);
        PersistentProjectileEntity persistentProjectileEntity = arrowItem.createModArrow(entity.world, stack, entity);
        if (stack.getItem() instanceof KidArrowItem) {
            KidArrowItem kidarrowItem = (KidArrowItem)(stack.getItem() instanceof KidArrowItem ? stack.getItem() : ModItems.WOODEN_KID_ARROW);
            persistentProjectileEntity = kidarrowItem.createKidArrow(entity.world, stack, entity);
        }
        persistentProjectileEntity.applyEnchantmentEffects(entity, damageModifier);
        if (stack.isOf(ModItems.TIPPED_ARROW) && persistentProjectileEntity instanceof GoldenArrowEntity) {
            ((GoldenArrowEntity)persistentProjectileEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.TIPPED_KID_ARROW) && persistentProjectileEntity instanceof GoldenKidArrowEntity) {
            ((GoldenKidArrowEntity)persistentProjectileEntity).initFromStack(stack);
        }
        cbireturn.setReturnValue(persistentProjectileEntity);
    }

    
}
