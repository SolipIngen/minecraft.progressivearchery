package solipingen.progressivearchery.client.render.entity.feature.stuck_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import solipingen.progressivearchery.entity.projectile.arrow.WoodenArrowEntity;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


@Environment(value = EnvType.CLIENT)
public class StuckWoodenArrowFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>> extends StuckObjectsFeatureRenderer<T, M> {
    private final EntityRenderDispatcher dispatcher;


    public StuckWoodenArrowFeatureRenderer(EntityRendererFactory.Context context, LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
        this.dispatcher = context.getRenderDispatcher();
    }

    @Override
    protected int getObjectCount(T entity) {
        return ((LivingEntityInterface)entity).getStuckWoodenArrowCount();
    }

    @Override
    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        WoodenArrowEntity arrowEntity = new WoodenArrowEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ(), ItemStack.EMPTY, null);
        arrowEntity.setYaw((float)(Math.atan2(directionX, directionZ) * 57.2957763671875));
        arrowEntity.setPitch((float)(Math.atan2(directionY, f) * 57.2957763671875));
        arrowEntity.prevYaw = arrowEntity.getYaw();
        arrowEntity.prevPitch = arrowEntity.getPitch();
        this.dispatcher.render(arrowEntity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light);
    }


}
