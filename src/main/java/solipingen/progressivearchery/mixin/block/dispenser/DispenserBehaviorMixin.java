package solipingen.progressivearchery.mixin.block.dispenser;

import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import solipingen.progressivearchery.item.ModItems;


@Mixin(DispenserBehavior.class)
public interface DispenserBehaviorMixin {
    
    
    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private static void injectedRegisterDefaults(CallbackInfo cbi) {

        DispenserBlock.registerProjectileBehavior(ModItems.WOODEN_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.WOODEN_KID_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.FLINT_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.FLINT_KID_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.COPPER_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.COPPER_KID_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.GOLDEN_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.GOLDEN_KID_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.IRON_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.IRON_KID_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.DIAMOND_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.DIAMOND_KID_ARROW);

        DispenserBlock.registerProjectileBehavior(ModItems.TIPPED_KID_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.SPECTRAL_KID_ARROW);

    }


}
