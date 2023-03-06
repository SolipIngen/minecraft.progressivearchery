package solipingen.progressivearchery.mixin.client.render.entity.feature;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import solipingen.progressivearchery.item.ModCrossbowItem;


@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    @Shadow @Final private HeldItemRenderer heldItemRenderer;


    public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void injectedRenderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo cbi) {
        ItemStack handStack = entity.getOffHandStack().getItem() instanceof CrossbowItem || entity.getOffHandStack().getItem() instanceof ModCrossbowItem ? entity.getOffHandStack() : entity.getMainHandStack();
        if (entity.getActiveItem().getItem() instanceof RangedWeaponItem || CrossbowItem.isCharged(handStack) || ModCrossbowItem.isCharged(handStack)) {
            ModelTransformation.Mode switchedMode = transformationMode;
            Arm switchedArm = arm;
            if (entity instanceof PlayerEntity && transformationMode == ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND && arm == Arm.RIGHT) {
                switchedMode = ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND;
                switchedArm = Arm.LEFT;
            }
            else if (entity instanceof PlayerEntity && transformationMode == ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND && arm == Arm.LEFT) {
                switchedMode = ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND;
                switchedArm = Arm.RIGHT;
            }
            matrices.push();
            ((ModelWithArms)this.getContextModel()).setArmAngle(switchedArm, matrices);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
            boolean bl = switchedArm == Arm.LEFT;
            matrices.translate((float)(bl ? -1 : 1) / 16.0f, 0.125f, -0.625f);
            this.heldItemRenderer.renderItem(entity, stack, switchedMode, bl, matrices, vertexConsumers, light);
            matrices.pop();
            cbi.cancel();
        }
    }

    
}
