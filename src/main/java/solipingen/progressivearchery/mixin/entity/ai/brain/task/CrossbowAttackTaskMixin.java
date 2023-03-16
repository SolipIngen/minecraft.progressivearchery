package solipingen.progressivearchery.mixin.entity.ai.brain.task;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.entity.crossbowattack.CrossbowState;


@Mixin(CrossbowAttackTask.class)
public abstract class CrossbowAttackTaskMixin<E extends MobEntity, T extends LivingEntity> extends MultiTickTask<E> {
    @Shadow private int chargingCooldown;
    private CrossbowState state = CrossbowState.UNCHARGED;

    @Invoker("getAttackTarget")
    public abstract LivingEntity invokeGetAttackTarget(LivingEntity entity);


    public CrossbowAttackTaskMixin(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
        super(requiredMemoryState);
    }

    @Inject(method = "shouldRun", at = @At("HEAD"), cancellable = true)
    private void injectedShouldRun(ServerWorld serverWorld, E mobEntity, CallbackInfoReturnable<Boolean> cbireturn) {
        LivingEntity livingEntity = this.invokeGetAttackTarget(mobEntity);
        cbireturn.setReturnValue((((LivingEntity)mobEntity).isHolding(Items.CROSSBOW) || ((LivingEntity)mobEntity).isHolding(ModItems.WOODEN_CROSSBOW) || ((LivingEntity)mobEntity).isHolding(ModItems.GOLD_FUSED_CROSSBOW)) 
            && LookTargetUtil.isVisibleInMemory(mobEntity, livingEntity) && LookTargetUtil.isTargetWithinAttackRange(mobEntity, livingEntity, 0));
    }
    
    @Inject(method = "finishRunning", at = @At("TAIL"))
    private void finishRunning(ServerWorld serverWorld, E mobEntity, long l, CallbackInfo cbi) {
        if (((LivingEntity)mobEntity).isHolding(ModItems.WOODEN_CROSSBOW)) {
            ((CrossbowUser)mobEntity).setCharging(false);
            ModCrossbowItem.setCharged(((LivingEntity)mobEntity).getActiveItem(), false);
        }
        else if (((LivingEntity)mobEntity).isHolding(ModItems.COPPER_FUSED_CROSSBOW)) {
            ((CrossbowUser)mobEntity).setCharging(false);
            ModCrossbowItem.setCharged(((LivingEntity)mobEntity).getActiveItem(), false);
        }
        else if (((LivingEntity)mobEntity).isHolding(ModItems.GOLD_FUSED_CROSSBOW)) {
            ((CrossbowUser)mobEntity).setCharging(false);
            ModCrossbowItem.setCharged(((LivingEntity)mobEntity).getActiveItem(), false);
        }
        else if (((LivingEntity)mobEntity).isHolding(ModItems.IRON_FUSED_CROSSBOW)) {
            ((CrossbowUser)mobEntity).setCharging(false);
            ModCrossbowItem.setCharged(((LivingEntity)mobEntity).getActiveItem(), false);
        }
        else if (((LivingEntity)mobEntity).isHolding(ModItems.DIAMOND_FUSED_CROSSBOW)) {
            ((CrossbowUser)mobEntity).setCharging(false);
            ModCrossbowItem.setCharged(((LivingEntity)mobEntity).getActiveItem(), false);
        }
    }

    @Inject(method = "tickState", at = @At("HEAD"), cancellable = true)
    private void injectedTickState(E entity, LivingEntity target, CallbackInfo cbi) {
        ItemStack handStack = ((LivingEntity)entity).getMainHandStack();
        if (handStack.isEmpty()) {
            handStack = ((LivingEntity)entity).getOffHandStack();
            if (handStack.isEmpty()) {
                cbi.cancel();
            }
        }
        if (handStack.getItem() instanceof ModCrossbowItem) {
            if (this.state == CrossbowState.UNCHARGED) {
                ((LivingEntity)entity).setCurrentHand(ProjectileUtil.getHandPossiblyHolding(entity, handStack.getItem()));
                this.state = CrossbowState.CHARGING;
                ((CrossbowUser)entity).setCharging(true);
            } else if (this.state == CrossbowState.CHARGING) {
                if (!((LivingEntity)entity).isUsingItem()) {
                    this.state = CrossbowState.UNCHARGED;
                }
                if ((((LivingEntity)entity).getItemUseTime()) >= ModCrossbowItem.getPullTime(((LivingEntity)entity).getActiveItem())) {
                    ((LivingEntity)entity).stopUsingItem();
                    this.state = CrossbowState.CHARGED;
                    this.chargingCooldown = 5*((LivingEntity)entity).world.getDifficulty().getId() + ((LivingEntity)entity).getRandom().nextInt(5);
                    ((CrossbowUser)entity).setCharging(false);
                }
            } 
            else if (this.state == CrossbowState.CHARGED) {
                --this.chargingCooldown;
                if (this.chargingCooldown == 0) {
                    this.state = CrossbowState.READY_TO_ATTACK;
                }
            } 
            else if (this.state == CrossbowState.READY_TO_ATTACK) {
                ((RangedAttackMob)entity).attack(target, 1.0f);
                ItemStack itemStack2 = ((LivingEntity)entity).getStackInHand(ProjectileUtil.getHandPossiblyHolding(entity, handStack.getItem()));
                if (itemStack2.getItem() == ModItems.WOODEN_CROSSBOW) {
                    ModCrossbowItem.setCharged(itemStack2, false);
                }
                else if (itemStack2.getItem() == ModItems.COPPER_FUSED_CROSSBOW) {
                    ModCrossbowItem.setCharged(itemStack2, false);
                }
                else if (itemStack2.getItem() == ModItems.GOLD_FUSED_CROSSBOW) {
                    ModCrossbowItem.setCharged(itemStack2, false);
                }
                else if (itemStack2.getItem() == ModItems.IRON_FUSED_CROSSBOW) {
                    ModCrossbowItem.setCharged(itemStack2, false);
                }
                else if (itemStack2.getItem() == ModItems.DIAMOND_FUSED_CROSSBOW) {
                    ModCrossbowItem.setCharged(itemStack2, false);
                }
                else if (itemStack2.getItem() == ModItems.NETHERITE_FUSED_CROSSBOW) {
                    ModCrossbowItem.setCharged(itemStack2, false);
                }
                this.state = CrossbowState.UNCHARGED;
            }
            cbi.cancel();
        }
    }


}
