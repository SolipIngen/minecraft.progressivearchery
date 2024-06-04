package solipingen.progressivearchery.mixin.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.BoggedEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.item.ModBowItem;


@Mixin(BoggedEntity.class)
public abstract class BoggedEntityMixin extends AbstractSkeletonEntity {


    protected BoggedEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createArrowProjectile", at = @At("HEAD"), cancellable = true)
    private void injectedCreateArrowProjectile(ItemStack arrow, float damageModifier, CallbackInfoReturnable<PersistentProjectileEntity> cbireturn) {
        if (this.getMainHandStack().getItem() instanceof BowItem) {
            PersistentProjectileEntity persistentProjectileEntity = new GoldenArrowEntity(this.getWorld(), this, arrow);
            if (persistentProjectileEntity instanceof GoldenArrowEntity) {
                ((GoldenArrowEntity)persistentProjectileEntity).addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600));
            }
            else if (persistentProjectileEntity instanceof GoldenKidArrowEntity) {
                ((GoldenKidArrowEntity)persistentProjectileEntity).addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600));
            }
            cbireturn.setReturnValue(persistentProjectileEntity);
        }
        if (this.getMainHandStack().getItem() instanceof ModBowItem) {
            PersistentProjectileEntity persistentProjectileEntity = new GoldenArrowEntity(this.getWorld(), this, arrow);
            ModBowItem modBowItem = (ModBowItem)this.getMainHandStack().getItem();
            if (modBowItem.getBowType() == 3) {
                persistentProjectileEntity = new GoldenKidArrowEntity(this.getWorld(), this, arrow);
            }
            if (persistentProjectileEntity instanceof GoldenArrowEntity) {
                ((GoldenArrowEntity)persistentProjectileEntity).addEffect(new StatusEffectInstance(StatusEffects.POISON, 100));
            }
            else if (persistentProjectileEntity instanceof GoldenKidArrowEntity) {
                ((GoldenKidArrowEntity)persistentProjectileEntity).addEffect(new StatusEffectInstance(StatusEffects.POISON, 100));
            }
            cbireturn.setReturnValue(persistentProjectileEntity);
        }
    }

    
}
