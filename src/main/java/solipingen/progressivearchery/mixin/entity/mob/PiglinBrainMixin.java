package solipingen.progressivearchery.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import solipingen.progressivearchery.item.ModItems;


@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    
    
    @Redirect(method = "isHoldingCrossbow", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isHolding(Lnet/minecraft/item/Item;)Z"))
    private static boolean redirectedIsHoldingCrossbow(LivingEntity piglin, Item item) {
        return piglin.isHolding(Items.CROSSBOW) || piglin.isHolding(ModItems.GOLD_FUSED_CROSSBOW);
    }
    
}
