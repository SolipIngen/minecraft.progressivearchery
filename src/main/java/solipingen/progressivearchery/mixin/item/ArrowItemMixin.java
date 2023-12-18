package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;


@Mixin(ArrowItem.class)
public abstract class ArrowItemMixin extends Item {


    public ArrowItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createArrow", at = @At("HEAD"), cancellable = true)
    private void injectedCreateArrow(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cbireturn) {
        if (stack.isOf(Items.TIPPED_ARROW)) {
            GoldenArrowEntity tippedArrowEntity = new GoldenArrowEntity(world, shooter, stack);
            tippedArrowEntity.initFromStack(stack);
            cbireturn.setReturnValue(tippedArrowEntity);
        }
    }


    
}
