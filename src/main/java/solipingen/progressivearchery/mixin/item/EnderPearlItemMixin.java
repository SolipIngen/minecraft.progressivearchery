package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;


@Mixin(EnderPearlItem.class)
public abstract class EnderPearlItemMixin extends Item {


    public EnderPearlItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyConstant(method = "use", constant = @Constant(intValue = 20))
    private int modifiedCooldownTime(int originalI) {
        return 10;
    }

    
    
}
