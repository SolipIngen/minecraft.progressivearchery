package solipingen.progressivearchery.mixin.client.render.entity;

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
import solipingen.progressivearchery.item.ModCrossbowItem;


@Mixin(PlayerEntityRenderer.class)
@Environment(value=EnvType.CLIENT)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    
    public PlayerEntityRendererMixin(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectedInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo cbi) {
        this.addFeature(new StuckWoodenArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckWoodenKidArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckFlintArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckFlintKidArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckCopperArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckCopperKidArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckGoldenArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckGoldenKidArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckSpectralArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckSpectralKidArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckIronArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckIronKidArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckDiamondArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new StuckDiamondKidArrowFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        this.addFeature(new QuiverFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(this, ctx.getModelLoader()));
    }

    @Inject(method = "getArmPose", at = @At("TAIL"), cancellable = true)
    private static void injectedGetArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cbireturn) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!player.handSwinging && (itemStack.getItem() instanceof ModCrossbowItem || itemStack.isOf(Items.CROSSBOW)) && CrossbowItem.isCharged(itemStack)) {
            cbireturn.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        }
    }
    
}
