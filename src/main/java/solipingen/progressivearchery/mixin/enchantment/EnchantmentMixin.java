package solipingen.progressivearchery.mixin.enchantment;


import net.minecraft.enchantment.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Shadow @Final private Enchantment.Properties properties;

    @Shadow
    public abstract String getTranslationKey();


    @Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
    @Final private void injectedGetMaxLevel(CallbackInfoReturnable<Integer> cbireturn) {
        if (this.getTranslationKey().contains("multishot")) {
            cbireturn.setReturnValue(3);
        }
    }

    @Inject(method = "getMinPower", at = @At("HEAD"), cancellable = true)
    @Final private void injectedGetMinPower(int level, CallbackInfoReturnable<Integer> cbireturn) {
        if (this.getTranslationKey().contains("multishot")) {
            cbireturn.setReturnValue(Enchantment.leveledCost(17, 7).forLevel(level));
        }
    }

    @Inject(method = "isTreasure", at = @At("HEAD"), cancellable = true)
    private void injectedIsTreasure(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.getTranslationKey().contains("quick_charge")) {
            cbireturn.setReturnValue(true);
        }
    }

    @Inject(method = "isAvailableForEnchantedBookOffer", at = @At("HEAD"), cancellable = true)
    private void injectedIsAvailableForEnchantedBookOffer(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.getTranslationKey().contains("quick_charge")) {
            cbireturn.setReturnValue(false);
        }
    }

    @Inject(method = "isAvailableForRandomSelection", at = @At("HEAD"), cancellable = true)
    private void injectedIsAvailableForRandomSelection(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.getTranslationKey().contains("quick_charge")) {
            cbireturn.setReturnValue(false);
        }
    }


}
