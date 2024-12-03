package solipingen.progressivearchery.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.EntityInterface;


@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {
    /**
     * Note: {@link net.minecraft.entity.decoration.LeashKnotEntity} always have fireproofLeashing = false.
     * This is simply a default value, as there is no need for the LeashKnotEntity to keep track of how it is leashing every one of its leashed entities.
     * The important part is whether each leashed entity knows it is fireproof leashed in its {@link net.minecraft.entity.Leashable.LeashData}.
     */
    @Unique private boolean fireproofLeashing = false;


    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"), cancellable = true)
    private void injectedInteractLeashing(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ModItems.FIREPROOF_LEAD) && (Entity)(Object)this instanceof Leashable leashable && leashable.canLeashAttachTo()) {
            if (!((Entity)(Object)this).getWorld().isClient()) {
                ((EntityInterface)player).setFireproofLeashing(true);
                leashable.attachLeash(player, true);
            }
            itemStack.decrement(1);
            cbireturn.setReturnValue(ActionResult.success(((Entity)(Object)this).getWorld().isClient()));
        }
    }

    @Override
    public boolean isFireproofLeashing() {
        return this.fireproofLeashing;
    }

    @Override
    public void setFireproofLeashing(boolean fireproofLeashing) {
        this.fireproofLeashing = fireproofLeashing;
    }


}
