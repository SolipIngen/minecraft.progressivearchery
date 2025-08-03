package solipingen.progressivearchery.mixin.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.item.ModBowItem;


@Mixin(AbstractSkeletonEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class AbstractSkeletonEntityRendererMixin<T extends AbstractSkeletonEntity, S extends SkeletonEntityRenderState> extends BipedEntityRenderer<T, S, SkeletonEntityModel<S>> {


    public AbstractSkeletonEntityRendererMixin(EntityRendererFactory.Context context, SkeletonEntityModel<S> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/mob/AbstractSkeletonEntity;Lnet/minecraft/client/render/entity/state/SkeletonEntityRenderState;F)V", at = @At("TAIL"))
    private void injectedUpdateRenderState(T abstractSkeletonEntity, S skeletonEntityRenderState, float f, CallbackInfo cbi) {
        skeletonEntityRenderState.holdingBow |= abstractSkeletonEntity.isHolding(itemStack -> itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof ModBowItem);
    }

    @Inject(method = "getArmPose(Lnet/minecraft/entity/mob/AbstractSkeletonEntity;Lnet/minecraft/util/Arm;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At("HEAD"), cancellable = true)
    private void injectedGetArmPose(AbstractSkeletonEntity abstractSkeletonEntity, Arm arm, CallbackInfoReturnable<BipedEntityModel.ArmPose> cbireturn) {
        if (abstractSkeletonEntity.getMainArm() == arm && abstractSkeletonEntity.isAttacking()
                && abstractSkeletonEntity.isHolding(itemStack -> itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof ModBowItem)) {
            cbireturn.setReturnValue(BipedEntityModel.ArmPose.BOW_AND_ARROW);
        }
    }


}
