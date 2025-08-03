package solipingen.progressivearchery.mixin.client.render.entity;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import solipingen.progressivearchery.client.render.entity.feature.QuiverFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckCopperArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckCopperKidArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckDiamondArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckDiamondKidArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckFlintArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckFlintKidArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckGoldenArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckGoldenKidArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckIronArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckIronKidArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckSpectralArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckSpectralKidArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckWoodenArrowFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.stuck_arrow.StuckWoodenKidArrowFeatureRenderer;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.PlayerEntityRenderStateInterface;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


@Mixin(PlayerEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {


    public PlayerEntityRendererMixin(Context ctx, PlayerEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectedInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo cbi) {
        this.addFeature(new StuckWoodenArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckWoodenKidArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckFlintArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckFlintKidArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckCopperArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckCopperKidArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckGoldenArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckGoldenKidArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckSpectralArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckSpectralKidArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckIronArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckIronKidArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckDiamondArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new StuckDiamondKidArrowFeatureRenderer<>(this, ctx));
        this.addFeature(new QuiverFeatureRenderer<>(this, ctx.getEntityModels(), ctx.getEquipmentRenderer()));
    }

    @Inject(method = "getArmPose(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At("TAIL"), cancellable = true)
    private static void injectedGetArmPose(PlayerEntity player, ItemStack stack, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cbireturn) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!player.handSwinging && (itemStack.getItem() instanceof ModCrossbowItem || itemStack.isOf(Items.CROSSBOW)) && CrossbowItem.isCharged(itemStack)) {
            cbireturn.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        }
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
    private void injectedUpdateRenderState(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo cbi) {
        LivingEntityInterface iLivingEntity = (LivingEntityInterface)abstractClientPlayerEntity;
        PlayerEntityRenderStateInterface iState = (PlayerEntityRenderStateInterface)playerEntityRenderState;
        iState.setStuckWoodenArrowCount(iLivingEntity.getStuckWoodenArrowCount());
        iState.setStuckWoodenKidArrowCount(iLivingEntity.getStuckWoodenKidArrowCount());
        iState.setStuckFlintArrowCount(iLivingEntity.getStuckFlintArrowCount());
        iState.setStuckFlintKidArrowCount(iLivingEntity.getStuckFlintKidArrowCount());
        iState.setStuckCopperArrowCount(iLivingEntity.getStuckCopperArrowCount());
        iState.setStuckCopperKidArrowCount(iLivingEntity.getStuckCopperKidArrowCount());
        iState.setStuckGoldenArrowCount(iLivingEntity.getStuckGoldenArrowCount());
        iState.setStuckGoldenKidArrowCount(iLivingEntity.getStuckGoldenKidArrowCount());
        iState.setStuckIronArrowCount(iLivingEntity.getStuckIronArrowCount());
        iState.setStuckIronKidArrowCount(iLivingEntity.getStuckIronKidArrowCount());
        iState.setStuckDiamondArrowCount(iLivingEntity.getStuckDiamondArrowCount());
        iState.setStuckDiamondKidArrowCount(iLivingEntity.getStuckDiamondKidArrowCount());
        iState.setStuckSpectralArrowCount(iLivingEntity.getStuckSpectralArrowCount());
        iState.setStuckSpectralKidArrowCount(iLivingEntity.getStuckSpectralKidArrowCount());
        for (int index = 0; index < abstractClientPlayerEntity.getInventory().size(); index++) {
            ItemStack quiverStack = abstractClientPlayerEntity.getInventory().getStack(index);
            if (quiverStack.getItem() instanceof QuiverItem) {
                iState.setQuiverStack(quiverStack);
                break;
            }
            else {
                iState.setQuiverStack(ItemStack.EMPTY);
            }
        }
    }
    
}
