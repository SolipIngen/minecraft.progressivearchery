package solipingen.progressivearchery.client.render.entity.feature.stuck_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.IronArrowEntityRenderer;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.PlayerEntityRenderStateInterface;
import solipingen.progressivearchery.entity.projectile.arrow.IronArrowEntity;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


@Environment(value = EnvType.CLIENT)
public class StuckIronArrowFeatureRenderer<M extends PlayerEntityModel> extends StuckObjectsFeatureRenderer<M> {


    public StuckIronArrowFeatureRenderer(LivingEntityRenderer<?, PlayerEntityRenderState, M> entityRenderer, EntityRendererFactory.Context context) {
        super(entityRenderer, new ArrowEntityModel(context.getPart(EntityModelLayers.ARROW)), IronArrowEntityRenderer.TEXTURE, RenderPosition.IN_CUBE);
    }

    @Override
    protected int getObjectCount(PlayerEntityRenderState playerRenderState) {
        return ((PlayerEntityRenderStateInterface)playerRenderState).getStuckIronArrowCount();
    }


}
