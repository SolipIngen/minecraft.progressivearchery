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
import solipingen.progressivearchery.client.render.entity.projectile.arrow.CopperArrowEntityRenderer;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.PlayerEntityRenderStateInterface;


@Environment(value = EnvType.CLIENT)
public class StuckCopperArrowFeatureRenderer<M extends PlayerEntityModel> extends StuckObjectsFeatureRenderer<M> {


    public StuckCopperArrowFeatureRenderer(LivingEntityRenderer<?, PlayerEntityRenderState, M> entityRenderer, EntityRendererFactory.Context context) {
        super(entityRenderer, new ArrowEntityModel(context.getPart(EntityModelLayers.ARROW)), CopperArrowEntityRenderer.TEXTURE, RenderPosition.IN_CUBE);
    }

    @Override
    protected int getObjectCount(PlayerEntityRenderState playerRenderState) {
        return ((PlayerEntityRenderStateInterface)playerRenderState).getStuckCopperArrowCount();
    }

//    @Override
//    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
//        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
//        CopperArrowEntity arrowEntity = new CopperArrowEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ(), ItemStack.EMPTY, null);
//        arrowEntity.setYaw((float)(Math.atan2(directionX, directionZ) * 57.2957763671875));
//        arrowEntity.setPitch((float)(Math.atan2(directionY, f) * 57.2957763671875));
//        arrowEntity.prevYaw = arrowEntity.getYaw();
//        arrowEntity.prevPitch = arrowEntity.getPitch();
//        this.dispatcher.render(arrowEntity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light);
//    }


}
