package solipingen.progressivearchery.mixin.component;


import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BeesComponent;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.component.ModDataComponentTypes;
import solipingen.progressivearchery.component.type.QuiverContentsComponent;

import java.util.List;


@Mixin(BundleContentsComponent.class)
public abstract class BundleContentsComponentMixin {
    @Unique private static final Fraction NESTED_BUNDLE_OCCUPANCY = Fraction.getFraction(1, 192);


    @Inject(method = "getOccupancy(Lnet/minecraft/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;", at = @At("HEAD"), cancellable = true)
    private static void injectedGetOccupancy(ItemStack stack, CallbackInfoReturnable<Fraction> cbireturn) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (bundleContentsComponent != null) {
            cbireturn.setReturnValue(NESTED_BUNDLE_OCCUPANCY.add(bundleContentsComponent.getOccupancy()));
        }
        else if (quiverContentsComponent != null) {
            cbireturn.setReturnValue(Fraction.getFraction(1, 384).add(quiverContentsComponent.getOccupancy()));
        }
        else {
            List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, BeesComponent.DEFAULT).bees();
            cbireturn.setReturnValue(!list.isEmpty() ? Fraction.getFraction(1, 12)
                    : Fraction.getFraction(1, 12*stack.getMaxCount()));
        }
    }



}
