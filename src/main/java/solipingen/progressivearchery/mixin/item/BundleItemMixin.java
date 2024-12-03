package solipingen.progressivearchery.mixin.item;


import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(BundleItem.class)
public abstract class BundleItemMixin {

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void injectedBundleMaxCapacity(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo cbi) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent != null) {
            int i = MathHelper.multiplyFraction(bundleContentsComponent.getOccupancy(), 12*64);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", i, 12*64).formatted(Formatting.GRAY));
        }
        cbi.cancel();
    }

}
