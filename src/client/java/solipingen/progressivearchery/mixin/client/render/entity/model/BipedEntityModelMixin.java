package solipingen.progressivearchery.mixin.client.render.entity.model;

import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;


@Mixin(BipedEntityModel.class)
@Environment(EnvType.CLIENT)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState> extends EntityModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;


    protected BipedEntityModelMixin(ModelPart root) {
        super(root);
    }

    @SuppressWarnings("incomplete-switch")
    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void injectedPositionRightArm(T state, ArmPose armPose, CallbackInfo cbi) {
        if (armPose == ArmPose.BOW_AND_ARROW) {
            this.rightArm.yaw = 0.1f + this.head.yaw - 0.4f;
            this.leftArm.yaw = -0.1f + this.head.yaw;
            this.rightArm.pitch = -1.5707964f + this.head.pitch;
            this.leftArm.pitch = -1.5707964f + this.head.pitch;
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    private void injectedPositionLeftArm(T state, ArmPose armPose, CallbackInfo cbi) {
        if (armPose == ArmPose.BOW_AND_ARROW) {
            this.rightArm.yaw = 0.1f + this.head.yaw;
            this.leftArm.yaw = -0.1f + this.head.yaw + 0.4f;
            this.rightArm.pitch = -1.5707964f + this.head.pitch;
            this.leftArm.pitch = -1.5707964f + this.head.pitch;
        }
    }
    

}
