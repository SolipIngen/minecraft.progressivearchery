package solipingen.progressivearchery.mixin.item;

import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.potion.Potions;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
    @Shadow @Final private Item item;


    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void injectedGetMaxCount(CallbackInfoReturnable<Integer> cbireturn) {
        if (this.item instanceof PotionItem && this.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().isPresent()) {
            if (this.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().get() == Potions.WATER) {
                ((ItemStack)(Object)this).set(DataComponentTypes.MAX_STACK_SIZE, 64);
            }
            else {
                if (this.item instanceof SplashPotionItem) {
                    ((ItemStack)(Object)this).set(DataComponentTypes.MAX_STACK_SIZE, 4);
                }
                else if (this.item instanceof LingeringPotionItem) {
                    ((ItemStack)(Object)this).set(DataComponentTypes.MAX_STACK_SIZE, 2);
                }
                else {
                    ((ItemStack)(Object)this).set(DataComponentTypes.MAX_STACK_SIZE, 16);
                }
            }
            cbireturn.setReturnValue(this.getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 1));
        }
    }



}
