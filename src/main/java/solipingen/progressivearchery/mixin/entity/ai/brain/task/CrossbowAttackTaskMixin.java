package solipingen.progressivearchery.mixin.entity.ai.brain.task;

import java.util.Map;

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
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
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

    @ModifyConstant(method = "tickState", constant = @Constant(intValue = 20))
    private int redirectedBaseChargedTicksLeft(int originalI, E entity, LivingEntity target) {
        return 20 - 5*entity.world.getDifficulty().getId();
    }

    @Redirect(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"))
    private Hand redirectedGetHandPossiblyHolding(LivingEntity entity, Item item) {
        ItemStack itemStack = entity.getMainHandStack().getItem() instanceof ModCrossbowItem ? entity.getMainHandStack() : entity.getOffHandStack();
        if (itemStack.getItem() instanceof ModCrossbowItem) {
            return ProjectileUtil.getHandPossiblyHolding(entity, itemStack.getItem());
        }
        return ProjectileUtil.getHandPossiblyHolding(entity, item);
    }

    @Redirect(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I"))
    private int redirectedGetPullTime(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ModCrossbowItem) {
            return ModCrossbowItem.getPullTime(itemStack);
        }
        return CrossbowItem.getPullTime(itemStack);
    }

    @Redirect(method = "tickState", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;setCharged(Lnet/minecraft/item/ItemStack;Z)V"))
    private void redirectedSetCharged(ItemStack itemStack, boolean charged, E entity, LivingEntity target) {
        ItemStack itemStack2 = entity.getMainHandStack().getItem() instanceof ModCrossbowItem ? entity.getMainHandStack() : entity.getOffHandStack();
        if (itemStack2.getItem() instanceof ModCrossbowItem) {
            ModCrossbowItem.setCharged(itemStack2, charged);
        }
        else {
            CrossbowItem.setCharged(itemStack, charged);
        }
    }


}
