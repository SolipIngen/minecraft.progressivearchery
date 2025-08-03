package solipingen.progressivearchery.mixin.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.world.World;


@Mixin(ThrowablePotionItem.class)
public abstract class ThrowablePotionItemMixin extends PotionItem {


    public ThrowablePotionItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"), cancellable = true)
    private void injectedUseCooldown(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        if (!user.isCreative()) {
            ItemStack stack = user.getStackInHand(hand);
            stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().ifPresent(potionEntry -> {
                if (potionEntry != Potions.WATER) {
                    user.getItemCooldownManager().set(stack, 30);
                }
            });
        }
    }

    
}
