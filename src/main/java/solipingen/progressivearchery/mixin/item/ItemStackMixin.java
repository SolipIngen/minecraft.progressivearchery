package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void injectedGetMaxCount(CallbackInfoReturnable<Integer> cbireturn) {
        if (PotionUtil.getPotion(((ItemStack)(Object)this)) == Potions.WATER) {
            cbireturn.setReturnValue(64);
        }
    }



}
