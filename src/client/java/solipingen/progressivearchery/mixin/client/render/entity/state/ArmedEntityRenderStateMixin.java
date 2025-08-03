package solipingen.progressivearchery.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.progressivearchery.item.ModCrossbowItem;


@Mixin(ArmedEntityRenderState.class)
@Environment(EnvType.CLIENT)
public abstract class ArmedEntityRenderStateMixin extends LivingEntityRenderState {


    @Inject(method = "updateRenderState", at = @At("HEAD"), cancellable = true)
    private static void injectedUpdateRenderState(LivingEntity entity, ArmedEntityRenderState state, ItemModelManager itemModelManager, CallbackInfo cbi) {
        ItemStack itemStack = entity.getActiveItem();
        ItemStack handStack = entity.getOffHandStack().getItem() instanceof CrossbowItem || entity.getOffHandStack().getItem() instanceof ModCrossbowItem ? entity.getOffHandStack() : entity.getMainHandStack();
        if (!itemStack.isEmpty()
                && (itemStack.getItem() instanceof RangedWeaponItem || CrossbowItem.isCharged(handStack) || ModCrossbowItem.isCharged(handStack))) {
            state.mainArm = entity.getMainArm();
            itemModelManager.updateForLivingEntity(state.rightHandItemState, entity.getStackInArm(Arm.LEFT), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
            itemModelManager.updateForLivingEntity(state.leftHandItemState, entity.getStackInArm(Arm.RIGHT), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
        }
    }


}
