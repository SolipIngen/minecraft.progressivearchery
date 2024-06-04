package solipingen.progressivearchery.mixin.component;


import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(BundleContentsComponent.class)
public abstract class BundleContentsComponentMixin {
    private static final Fraction NESTED_BUNDLE_OCCUPANCY = Fraction.getFraction(1, 192);


    @Inject(method = "getOccupancy(Lnet/minecraft/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;", at = @At("HEAD"), cancellable = true)
    private static void injectedGetOccupancy(ItemStack stack, CallbackInfoReturnable<Fraction> cbireturn) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent != null) {
            cbireturn.setReturnValue(NESTED_BUNDLE_OCCUPANCY.add(bundleContentsComponent.getOccupancy()));
        }
        else {
            List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, List.of());
            cbireturn.setReturnValue(!list.isEmpty() ? Fraction.getFraction(1, 12) : Fraction.getFraction(1, 12*stack.getMaxCount()));
        }
    }



}
