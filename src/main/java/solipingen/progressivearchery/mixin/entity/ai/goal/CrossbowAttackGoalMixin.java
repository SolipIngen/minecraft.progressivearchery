package solipingen.progressivearchery.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.util.entity.crossbowattack.Stage;


@Mixin(CrossbowAttackGoal.class)
public abstract class CrossbowAttackGoalMixin<T extends HostileEntity & CrossbowUser> extends Goal {
    @Shadow public static UniformIntProvider COOLDOWN_RANGE = TimeHelper.betweenSeconds(1, 2);
    @Shadow private T actor;
    private Stage stage = Stage.UNCHARGED;
    @Shadow private double speed;
    @Shadow private float squaredRange;
    @Shadow private int seeingTargetTicker;
    @Shadow private int chargedTicksLeft;
    @Shadow private int cooldown;


    @Inject(method = "isEntityHoldingCrossbow", at = @At("HEAD"), cancellable = true)
    private void injectedIsEntityHoldingCrossbow(CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(((LivingEntity)this.actor).isHolding(Items.CROSSBOW) || (((LivingEntity)this.actor).getMainHandStack().getItem() instanceof ModCrossbowItem || ((LivingEntity)this.actor).getOffHandStack().getItem() instanceof ModCrossbowItem));
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo cbi) {
        boolean bl3;
        boolean bl2;
        LivingEntity livingEntity = ((MobEntity)this.actor).getTarget();
        if (livingEntity == null) {
            return;
        }
        ItemStack handStack = this.actor.getMainHandStack();
        if (handStack.isEmpty()) {
            handStack = this.actor.getOffHandStack();
            if (handStack.isEmpty()) {
                cbi.cancel();
            }
        }
        boolean bl = ((MobEntity)this.actor).getVisibilityCache().canSee(livingEntity);
        bl2 = this.seeingTargetTicker > 0;
        if (bl != bl2) {
            this.seeingTargetTicker = 0;
        }
        this.seeingTargetTicker = bl ? ++this.seeingTargetTicker : --this.seeingTargetTicker;
        double d = ((Entity)this.actor).squaredDistanceTo(livingEntity);
        bl3 = (d > (double)this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
        if (bl3) {
            --this.cooldown;
            if (this.cooldown <= 0) {
                ((MobEntity)this.actor).getNavigation().startMovingTo(livingEntity, this.stage == Stage.UNCHARGED ? this.speed : this.speed * 0.5);
                this.cooldown = COOLDOWN_RANGE.get(((LivingEntity)this.actor).getRandom());
            }
        } else {
            this.cooldown = 0;
            ((MobEntity)this.actor).getNavigation().stop();
        }
        ((MobEntity)this.actor).getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
        if (this.stage == Stage.UNCHARGED) {
            if (!bl3) {
                ((LivingEntity)this.actor).setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, handStack.getItem()));
                this.stage = Stage.CHARGING;
                ((CrossbowUser)this.actor).setCharging(true);
            }
        } else if (this.stage == Stage.CHARGING) {
            if (!((LivingEntity)this.actor).isUsingItem()) {
                this.stage = Stage.UNCHARGED;
            }
            if ((((LivingEntity)this.actor).getItemUseTime()) >= ModCrossbowItem.getPullTime(((LivingEntity)this.actor).getActiveItem())) {
                ((LivingEntity)this.actor).stopUsingItem();
                this.stage = Stage.CHARGED;
                this.chargedTicksLeft = 5*this.actor.world.getDifficulty().getId() + ((LivingEntity)this.actor).getRandom().nextInt(5);
                ((CrossbowUser)this.actor).setCharging(false);
            }
        } else if (this.stage == Stage.CHARGED) {
            --this.chargedTicksLeft;
            if (this.chargedTicksLeft == 0) {
                this.stage = Stage.READY_TO_ATTACK;
            }
        } else if (this.stage == Stage.READY_TO_ATTACK && bl) {
            ((RangedAttackMob)this.actor).attack(livingEntity, 1.0f);
            ItemStack itemStack2 = ((LivingEntity)this.actor).getStackInHand(ProjectileUtil.getHandPossiblyHolding(this.actor, handStack.getItem()));
            if (itemStack2.getItem() instanceof ModCrossbowItem) {
                ModCrossbowItem.setCharged(itemStack2, false);
            }
            this.stage = Stage.UNCHARGED;
        }
        cbi.cancel();
    }

    @Inject(method = "isUncharged", at = @At("HEAD"), cancellable = true)
    private void injectedIsUncharged(CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(this.stage == Stage.UNCHARGED);
    }
    
}
