package solipingen.progressivearchery.client.render.entity.feature.stuck_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.SpectralKidArrowEntityRenderer;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.PlayerEntityRenderStateInterface;


@Environment(value = EnvType.CLIENT)
public class StuckSpectralKidArrowFeatureRenderer<M extends PlayerEntityModel> extends StuckObjectsFeatureRenderer<M> {


    public StuckSpectralKidArrowFeatureRenderer(LivingEntityRenderer<?, PlayerEntityRenderState, M> entityRenderer, EntityRendererFactory.Context context) {
        super(entityRenderer, new ArrowEntityModel(context.getPart(EntityModelLayers.ARROW)), SpectralKidArrowEntityRenderer.TEXTURE, RenderPosition.IN_CUBE);
    }

    @Override
    protected int getObjectCount(PlayerEntityRenderState playerRenderState) {
        return ((PlayerEntityRenderStateInterface)playerRenderState).getStuckSpectralKidArrowCount();
    }



}
