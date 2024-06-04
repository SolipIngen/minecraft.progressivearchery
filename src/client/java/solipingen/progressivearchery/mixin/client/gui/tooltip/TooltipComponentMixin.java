package solipingen.progressivearchery.mixin.client.gui.tooltip;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.client.gui.tooltip.QuiverTooltipComponent;
import solipingen.progressivearchery.client.item.QuiverTooltipData;


@Mixin(TooltipComponent.class)
public interface TooltipComponentMixin {


    @Inject(method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;", at = @At("HEAD"), cancellable = true)
    private static void injectedOf(TooltipData data, CallbackInfoReturnable<TooltipComponent> cbireturn) {
        if (data instanceof QuiverTooltipData quiverTooltipData) {
            cbireturn.setReturnValue(new QuiverTooltipComponent(quiverTooltipData.contents()));
        }
    }

}
