package solipingen.progressivearchery.mixin.entity.ai.brain.task;

import java.util.Map;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;


@Mixin(CrossbowAttackTask.class)
public abstract class CrossbowAttackTaskMixin<E extends MobEntity, T extends LivingEntity> extends MultiTickTask<E> {

    @Invoker("getAttackTarget")
    public static LivingEntity invokeGetAttackTarget(LivingEntity entity) {
        throw new AssertionError();
    }


    public CrossbowAttackTaskMixin(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
        super(requiredMemoryState);
    }

    @Inject(method = "shouldRun(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void injectedShouldRun(ServerWorld serverWorld, E mobEntity, CallbackInfoReturnable<Boolean> cbireturn) {
        LivingEntity livingEntity = this.invokeGetAttackTarget(mobEntity);
        cbireturn.setReturnValue((mobEntity.isHolding(Items.CROSSBOW) || mobEntity.isHolding(stack -> stack.getItem() instanceof ModCrossbowItem))
            && TargetUtil.isVisibleInMemory(mobEntity, livingEntity) && TargetUtil.isTargetWithinAttackRange(mobEntity, livingEntity, 0));
    }
    
    @Inject(method = "finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V", at = @At("TAIL"))
    private void finishRunning(ServerWorld serverWorld, E mobEntity, long l, CallbackInfo cbi) {
        if (mobEntity.isHolding(stack -> stack.getItem() instanceof ModCrossbowItem)) {
            ((CrossbowUser)mobEntity).setCharging(false);
            mobEntity.getActiveItem().set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        }
    }

    @ModifyConstant(method = "tickState", constant = @Constant(intValue = 20))
    private int redirectedBaseChargedTicksLeft(int originalI, E entity, LivingEntity target) {
        return 20 - 5*entity.getWorld().getDifficulty().getId();
    }

    @Inject(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;", shift = At.Shift.AFTER))
    private void injectedGetHandPossiblyHolding(E entity, LivingEntity target, CallbackInfo cbi) {
        ItemStack itemStack = entity.getMainHandStack().getItem() instanceof ModCrossbowItem ? entity.getMainHandStack() : entity.getOffHandStack();
        if (itemStack.getItem() instanceof ModCrossbowItem) {
            entity.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(entity, itemStack.getItem()));
        }
    }


}
