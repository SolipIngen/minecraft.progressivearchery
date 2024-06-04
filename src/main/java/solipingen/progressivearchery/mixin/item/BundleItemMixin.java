package solipingen.progressivearchery.mixin.item;


import net.minecraft.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(BundleItem.class)
public abstract class BundleItemMixin {

    @ModifyConstant(method = "appendTooltip", constant = @Constant(intValue = 64))
    private int modifiedBundleMaxCapacity(int oldCapacity) {
        return 12*oldCapacity;
    }

}
