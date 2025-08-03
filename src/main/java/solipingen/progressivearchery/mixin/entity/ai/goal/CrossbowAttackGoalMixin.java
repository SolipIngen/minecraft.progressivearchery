package solipingen.progressivearchery.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Final;
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
    @Shadow @Final private T actor;


    @Inject(method = "isEntityHoldingCrossbow", at = @At("HEAD"), cancellable = true)
    private void injectedIsEntityHoldingCrossbow(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.actor.isHolding(stack -> stack.isOf(Items.CROSSBOW) || stack.getItem() instanceof ModCrossbowItem)) {
            cbireturn.setReturnValue(true);
        }
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
    private int redirectedBaseChargedTicksLeft(int originalI) {
        return 20 - 5*this.actor.getWorld().getDifficulty().getId();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"))
    private Hand redirectedGetHandPossiblyHolding(LivingEntity entity, Item item) {
        ItemStack itemStack = entity.getMainHandStack().getItem() instanceof ModCrossbowItem ? entity.getMainHandStack() : entity.getOffHandStack();
        if (itemStack.getItem() instanceof ModCrossbowItem) {
            return ProjectileUtil.getHandPossiblyHolding(entity, itemStack.getItem());
        }
        return ProjectileUtil.getHandPossiblyHolding(entity, item);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)I"))
    private int redirectedGetPullTime(ItemStack itemStack, LivingEntity entity) {
        if (itemStack.getItem() instanceof ModCrossbowItem) {
            return ModCrossbowItem.getPullTime(itemStack, entity);
        }
        return CrossbowItem.getPullTime(itemStack, entity);
    }


    
}
