package solipingen.progressivearchery.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import solipingen.progressivearchery.item.ModCrossbowItem;


@Mixin(CrossbowAttackGoal.class)
public abstract class CrossbowAttackGoalMixin<T extends HostileEntity & CrossbowUser> extends Goal {
    @Shadow private T actor;


    @Inject(method = "isEntityHoldingCrossbow", at = @At("HEAD"), cancellable = true)
    private void injectedIsEntityHoldingCrossbow(CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(this.actor.isHolding(Items.CROSSBOW) || (this.actor.getMainHandStack().getItem() instanceof ModCrossbowItem || this.actor.getOffHandStack().getItem() instanceof ModCrossbowItem));
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
    private int redirectedBaseChargedTicksLeft(int originalI) {
        return 20 - 5*this.actor.world.getDifficulty().getId();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"))
    private Hand redirectedGetHandPossiblyHolding(LivingEntity entity, Item item) {
        ItemStack itemStack = entity.getMainHandStack().getItem() instanceof ModCrossbowItem ? entity.getMainHandStack() : entity.getOffHandStack();
        if (itemStack.getItem() instanceof ModCrossbowItem) {
            return ProjectileUtil.getHandPossiblyHolding(entity, itemStack.getItem());
        }
        return ProjectileUtil.getHandPossiblyHolding(entity, item);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I"))
    private int redirectedGetPullTime(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ModCrossbowItem) {
            return ModCrossbowItem.getPullTime(itemStack);
        }
        return CrossbowItem.getPullTime(itemStack);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;setCharged(Lnet/minecraft/item/ItemStack;Z)V"))
    private void redirectedSetCharged(ItemStack itemStack, boolean charged) {
        ItemStack itemStack2 = this.actor.getMainHandStack().getItem() instanceof ModCrossbowItem ? this.actor.getMainHandStack() : this.actor.getOffHandStack();
        if (itemStack2.getItem() instanceof ModCrossbowItem) {
            ModCrossbowItem.setCharged(itemStack2, charged);
        }
        else {
            CrossbowItem.setCharged(itemStack, charged);
        }
    }


    
}
