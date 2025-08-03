package solipingen.progressivearchery.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;


@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    
    
    @Inject(method = "isHoldingCrossbow", at = @At(value = "HEAD"), cancellable = true)
    private static void injectedIsHoldingCrossbow(LivingEntity piglin, CallbackInfoReturnable<Boolean> cbireturn) {
        if (piglin.isHolding(stack -> stack.getItem() instanceof ModCrossbowItem)) {
            cbireturn.setReturnValue(true);
        }
    }
    
}
