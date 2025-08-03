package solipingen.progressivearchery.mixin.client.render.entity;

import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.state.StriderEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.StriderEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.StriderEntityModel;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.StriderEntityRenderStateInterface;
import solipingen.progressivearchery.util.interfaces.mixin.entity.passive.StriderEntityInterface;


@Mixin(StriderEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class StriderEntityRendererMixin extends AgeableMobEntityRenderer<StriderEntity, StriderEntityRenderState, StriderEntityModel> {
    @Unique private static final Identifier SHEARED_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/strider/strider_sheared.png");
    @Unique private static final Identifier COLD_SHEARED_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/strider/strider_sheared_cold.png");


    public StriderEntityRendererMixin(Context context, StriderEntityModel model, StriderEntityModel babyModel, float shadowRadius) {
        super(context, model, babyModel, shadowRadius);
    }

    @Inject(method = "getTexture(Lnet/minecraft/client/render/entity/state/StriderEntityRenderState;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    private void injectedTexture(StriderEntityRenderState striderEntityRenderState, CallbackInfoReturnable<Identifier> cbireturn) {
        if (((StriderEntityRenderStateInterface)striderEntityRenderState).getIsSheared()) {
            cbireturn.setReturnValue(striderEntityRenderState.cold ? COLD_SHEARED_TEXTURE : SHEARED_TEXTURE);
        }
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/passive/StriderEntity;Lnet/minecraft/client/render/entity/state/StriderEntityRenderState;F)V", at = @At("TAIL"))
    private void injectedUpdateRenderState(StriderEntity striderEntity, StriderEntityRenderState striderEntityRenderState, float f, CallbackInfo cbi) {
        ((StriderEntityRenderStateInterface)striderEntityRenderState).setIsSheared(((StriderEntityInterface)striderEntity).getIsSheared());
    }


}
