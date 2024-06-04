package solipingen.progressivearchery.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import solipingen.progressivearchery.item.ModBowItem;


@Mixin(BowAttackGoal.class)
public abstract class BowAttackGoalMixin<T extends HostileEntity> extends Goal {
    @Shadow @Final private T actor;
    @Shadow private int cooldown;
    @Shadow private int targetSeeingTicker;

    
    @Inject(method = "isHoldingBow", at = @At("HEAD"), cancellable = true)
    private void injectedIsHoldingBow(CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(((LivingEntity)this.actor).isHolding((stack) -> stack.isOf(Items.BOW) || stack.getItem() instanceof ModBowItem));
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/RangedAttackMob;isUsingItem()Z"), to = @At("TAIL")))
    private int modifiedMaxPullTicks(int originalInt) {
        ItemStack itemStack = ((LivingEntity)this.actor).getActiveItem();
        if (itemStack.getItem() instanceof ModBowItem) {
            return 20 + 5*((ModBowItem)itemStack.getItem()).getBowType();
        }
        return 20;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/HostileEntity;clearActiveItem()V"))
    private void redirectedClearActiveItemTick(HostileEntity actorHostileEntity) {
        // Empty replacing method removing the cleaActiveItem() to be moved to the end of the redirectedAttackTick below.
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/RangedAttackMob;shootAt(Lnet/minecraft/entity/LivingEntity;F)V"))
    private void redirectedAttackTick(RangedAttackMob rangedAttackMob, LivingEntity livingEntity, float pullProgress) {
        ItemStack stack = ((LivingEntity)this.actor).getMainHandStack();
        Item item = stack.getItem();
        int time = ((LivingEntity)this.actor).getItemUseTime();
        if (item instanceof ModBowItem) {
            ((RangedAttackMob)this.actor).shootAt(livingEntity, ((ModBowItem)item).getPullProgress(time, stack));
        }
        else {
            ((RangedAttackMob)this.actor).shootAt(livingEntity, ModBowItem.getVanillaBowPullProgress(time, stack));
        }
        ((LivingEntity)this.actor).clearActiveItem();
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void injectedTick(CallbackInfo cbi) {
        if (!((LivingEntity)this.actor).isUsingItem() && (--this.cooldown <= 0 && this.targetSeeingTicker >= -60)) {
            ItemStack handStack = this.actor.getMainHandStack();
            if (handStack.isEmpty()) {
                handStack = this.actor.getOffHandStack();
                if (handStack.isEmpty()) {
                    cbi.cancel();
                }
            }
            ((LivingEntity)this.actor).setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, handStack.getItem()));
        }
    }

}
