package solipingen.progressivearchery.mixin.client.render.entity.model;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.entity.LivingEntity;


@Mixin(BipedEntityModel.class)
@Environment(EnvType.CLIENT)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow public ArmPose leftArmPose;
    @Shadow public ArmPose rightArmPose;


    @SuppressWarnings("incomplete-switch")
    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void injectedPositionRightArm(T entity, CallbackInfo cbi) {
        if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
            this.rightArm.yaw = 0.1f + this.head.yaw - 0.4f;
            this.leftArm.yaw = -0.1f + this.head.yaw;
            this.rightArm.pitch = -1.5707964f + this.head.pitch;
            this.leftArm.pitch = -1.5707964f + this.head.pitch;
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    private void injectedPositionLeftArm(T entity, CallbackInfo cbi) {
        if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
            this.rightArm.yaw = 0.1f + this.head.yaw;
            this.leftArm.yaw = -0.1f + this.head.yaw + 0.4f;
            this.rightArm.pitch = -1.5707964f + this.head.pitch;
            this.leftArm.pitch = -1.5707964f + this.head.pitch;
        }
    }
    

}
