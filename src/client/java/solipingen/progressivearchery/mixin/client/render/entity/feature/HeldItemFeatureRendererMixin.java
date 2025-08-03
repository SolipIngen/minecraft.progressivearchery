package solipingen.progressivearchery.mixin.client.render.entity.feature;

import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;


@Mixin(HeldItemFeatureRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ModelWithArms> extends FeatureRenderer<S, M> {


    public HeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

//    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
//    private void injectedRenderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo cbi) {
//        ItemStack handStack = entity.getOffHandStack().getItem() instanceof CrossbowItem || entity.getOffHandStack().getItem() instanceof ModCrossbowItem ? entity.getOffHandStack() : entity.getMainHandStack();
//        if (entity.getActiveItem().getItem() instanceof RangedWeaponItem || CrossbowItem.isCharged(handStack) || ModCrossbowItem.isCharged(handStack)) {
//            ModelTransformationMode switchedMode = transformationMode;
//            Arm switchedArm = arm;
//            if (entity instanceof PlayerEntity && transformationMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND && arm == Arm.RIGHT) {
//                switchedMode = ModelTransformationMode.THIRD_PERSON_LEFT_HAND;
//                switchedArm = Arm.LEFT;
//            }
//            else if (entity instanceof PlayerEntity && transformationMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND && arm == Arm.LEFT) {
//                switchedMode = ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
//                switchedArm = Arm.RIGHT;
//            }
//            matrices.push();
//            ((ModelWithArms)this.getContextModel()).setArmAngle(switchedArm, matrices);
//            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f));
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
//            boolean bl = switchedArm == Arm.LEFT;
//            matrices.translate((float)(bl ? -1 : 1) / 16.0f, 0.125f, -0.625f);
//            this.heldItemRenderer.renderItem(entity, stack, switchedMode, bl, matrices, vertexConsumers, light);
//            matrices.pop();
//            cbi.cancel();
//        }
//    }

    
}
