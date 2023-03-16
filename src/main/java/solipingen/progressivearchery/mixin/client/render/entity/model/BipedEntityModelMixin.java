package solipingen.progressivearchery.mixin.client.render.entity.model;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;


@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow public ArmPose leftArmPose;
    @Shadow public ArmPose rightArmPose;


    @SuppressWarnings("incomplete-switch")
    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void injectedPositionRightArm(T entity, CallbackInfo cbi) {
        switch (this.rightArmPose) {
            case EMPTY: {
                this.rightArm.yaw = 0.0f;
                break;
            }
            case BLOCK: {
                this.rightArm.pitch = this.rightArm.pitch * 0.5f - 0.9424779f;
                this.rightArm.yaw = -0.5235988f;
                break;
            }
            case ITEM: {
                this.rightArm.pitch = this.rightArm.pitch * 0.5f - 0.31415927f;
                this.rightArm.yaw = 0.0f;
                break;
            }
            case THROW_SPEAR: {
                this.rightArm.pitch = this.rightArm.pitch * 0.5f - (float)Math.PI;
                this.rightArm.yaw = 0.0f;
                break;
            }
            case BOW_AND_ARROW: {
                this.rightArm.yaw = 0.1f + this.head.yaw - 0.4f;
                this.leftArm.yaw = -0.1f + this.head.yaw; 
                this.rightArm.pitch = -1.5707964f + this.head.pitch;
                this.leftArm.pitch = -1.5707964f + this.head.pitch;
                break;
            }
            case CROSSBOW_CHARGE: {
                CrossbowPosing.charge(this.rightArm, this.leftArm, entity, false);
                break;
            }
            case CROSSBOW_HOLD: {
                CrossbowPosing.hold(this.rightArm, this.leftArm, this.head, true);
                break;
            }
            case SPYGLASS: {
                this.rightArm.pitch = MathHelper.clamp(this.head.pitch - 1.9198622f - (((Entity)entity).isInSneakingPose() ? 0.2617994f : 0.0f), -2.4f, 3.3f);
                this.rightArm.yaw = this.head.yaw - 0.2617994f;
                break;
            }
            case TOOT_HORN: {
                this.rightArm.pitch = MathHelper.clamp(this.head.pitch, -1.2f, 1.2f) - 1.4835298f;
                this.rightArm.yaw = this.head.yaw - 0.5235988f;
            }
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    private void injectedPositionLeftArm(T entity, CallbackInfo cbi) {
        switch (this.leftArmPose) {
            case EMPTY: {
                this.leftArm.yaw = 0.0f;
                break;
            }
            case BLOCK: {
                this.leftArm.pitch = this.leftArm.pitch * 0.5f - 0.9424779f;
                this.leftArm.yaw = 0.5235988f;
                break;
            }
            case ITEM: {
                this.leftArm.pitch = this.leftArm.pitch * 0.5f - 0.31415927f;
                this.leftArm.yaw = 0.0f;
                break;
            }
            case THROW_SPEAR: {
                this.leftArm.pitch = this.leftArm.pitch * 0.5f - (float)Math.PI;
                this.leftArm.yaw = 0.0f;
                break;
            }
            case BOW_AND_ARROW: {
                this.rightArm.yaw = 0.1f + this.head.yaw;
                this.leftArm.yaw = -0.1f + this.head.yaw + 0.4f; 
                this.rightArm.pitch = -1.5707964f + this.head.pitch;
                this.leftArm.pitch = -1.5707964f + this.head.pitch;
                break;
            }
            case CROSSBOW_CHARGE: {
                CrossbowPosing.charge(this.rightArm, this.leftArm, entity, true);
                break;
            }
            case CROSSBOW_HOLD: {
                CrossbowPosing.hold(this.rightArm, this.leftArm, this.head, false);
                break;
            }
            case SPYGLASS: {
                this.leftArm.pitch = MathHelper.clamp(this.head.pitch - 1.9198622f - (((Entity)entity).isInSneakingPose() ? 0.2617994f : 0.0f), -2.4f, 3.3f);
                this.leftArm.yaw = this.head.yaw + 0.2617994f;
                break;
            }
            case TOOT_HORN: {
                this.leftArm.pitch = MathHelper.clamp(this.head.pitch, -1.2f, 1.2f) - 1.4835298f;
                this.leftArm.yaw = this.head.yaw + 0.5235988f;
            }
        }
    }
    

}
