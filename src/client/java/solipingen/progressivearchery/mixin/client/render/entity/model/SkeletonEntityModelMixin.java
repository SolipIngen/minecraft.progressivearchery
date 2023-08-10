package solipingen.progressivearchery.mixin.client.render.entity.model;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import solipingen.progressivearchery.item.ModBowItem;


@Mixin(SkeletonEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class SkeletonEntityModelMixin<T extends MobEntity> extends BipedEntityModel<T> {


    public SkeletonEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "animateModel", at = @At("HEAD"), cancellable = true)
    private void injectedAnimateModel(T mobEntity, float f, float g, float h, CallbackInfo cbi) {
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        Item mainHandItem = mobEntity.getStackInHand(Hand.MAIN_HAND).getItem();
        if ((mainHandItem instanceof BowItem || mainHandItem instanceof ModBowItem) && mobEntity.isAttacking()) {
            if (mobEntity.getMainArm() == Arm.RIGHT) {
                this.rightArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.animateModel(mobEntity, f, g, h);
        cbi.cancel();
    }

    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    private void injectedSetAngles(T mobEntity, float f, float g, float h, float i, float j, CallbackInfo cbi) {
        super.setAngles(mobEntity, f, g, h, i, j);
        ItemStack itemStack = mobEntity.getMainHandStack();
        if (mobEntity.isAttacking() && (itemStack.isEmpty() || !(itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof ModBowItem))) {
            float k = MathHelper.sin((float)(this.handSwingProgress * (float)Math.PI));
            float l = MathHelper.sin((float)((1.0f - (1.0f - this.handSwingProgress) * (1.0f - this.handSwingProgress)) * (float)Math.PI));
            this.rightArm.roll = 0.0f;
            this.leftArm.roll = 0.0f;
            this.rightArm.yaw = -(0.1f - k * 0.6f);
            this.leftArm.yaw = 0.1f - k * 0.6f;
            this.rightArm.pitch = -1.5707964f;
            this.leftArm.pitch = -1.5707964f;
            this.rightArm.pitch -= k * 1.2f - l * 0.4f;
            this.leftArm.pitch -= k * 1.2f - l * 0.4f;
            CrossbowPosing.swingArms(this.rightArm, this.leftArm, h);
        }
        cbi.cancel();
    }
    

    
}
