package solipingen.progressivearchery.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.EntityInterface;


@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {
    @Shadow @Final protected DataTracker dataTracker;
    @Unique private static final TrackedData<Boolean> FIREPROOF_LEASHED = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);


    @ModifyVariable(method = "<init>", at = @At("STORE"), ordinal = 0)
    private DataTracker.Builder modifiedDataTrackerBuilder(DataTracker.Builder builder) {
        builder.add(FIREPROOF_LEASHED, false);
        return builder;
    }

//    @ModifyVariable(method = "interact", at = @At("STORE"), name = "leashable2")
//    private Leashable modifiedLeashable2(Leashable leashable2, PlayerEntity player, Hand hand) {
//        if (leashable2.canBeLeashedTo((Entity)(Object)this)) {
//            this.setFireproofLeashed(((EntityInterface)leashable2).isFireproofLeashed());
//        }
//        return leashable2;
//    }

    @Inject(method = "interact", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;LEAD:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC), cancellable = true)
    private void injectedInteractPlayerLeashing(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        ItemStack itemStack2 = player.getStackInHand(hand);
        Leashable leashable3 = (Leashable)this;
        if (itemStack2.isOf(ModItems.FIREPROOF_LEAD) && !(leashable3.getLeashHolder() instanceof PlayerEntity)) {
            if (leashable3.canBeLeashedTo(player)) {
                if (!((Entity)(Object)this).getWorld().isClient()) {
                    if (leashable3.isLeashed()) {
                        leashable3.detachLeash();
                    }
                    leashable3.attachLeash(player, true);
                    ((Entity)(Object)this).playSoundIfNotSilent(SoundEvents.ITEM_LEAD_TIED);
                    itemStack2.decrement(1);
                }
                ((EntityInterface)this).setFireproofLeashed(true);
            }
            cbireturn.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "readData", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomData(Lnet/minecraft/storage/ReadView;)V", shift = At.Shift.AFTER))
    private void injectedReadCustomData(ReadView view, CallbackInfo cbi) {
        if ((Entity)(Object)this instanceof Leashable) {
            this.setFireproofLeashed(view.getBoolean("FireproofLeashed", false));
        }
    }

    @Inject(method = "writeData", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomData(Lnet/minecraft/storage/WriteView;)V", shift = At.Shift.AFTER))
    private void injectedWriteCustomData(WriteView view, CallbackInfo cbi) {
        if ((Entity)(Object)this instanceof Leashable) {
            view.putBoolean("FireproofLeashed", this.isFireproofLeashed());
        }
    }

    @Override
    public boolean isFireproofLeashed() {
        return this.dataTracker.get(FIREPROOF_LEASHED);
    }

    @Override
    public void setFireproofLeashed(boolean fireproofLeashed) {
        this.dataTracker.set(FIREPROOF_LEASHED, fireproofLeashed);
    }


}
