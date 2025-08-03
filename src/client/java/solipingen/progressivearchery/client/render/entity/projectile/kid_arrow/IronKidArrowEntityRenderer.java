package solipingen.progressivearchery.client.render.entity.projectile.kid_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.render.entity.ModEntityModelLayers;
import solipingen.progressivearchery.client.render.entity.model.projectile.kid_arrow.KidArrowEntityModel;
import solipingen.progressivearchery.entity.projectile.kid_arrow.IronKidArrowEntity;


@Environment(value = EnvType.CLIENT)
public class IronKidArrowEntityRenderer extends ProjectileEntityRenderer<IronKidArrowEntity, ArrowEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/iron_kid_arrow.png");
    private final KidArrowEntityModel model;


    public IronKidArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new KidArrowEntityModel(context.getPart(ModEntityModelLayers.KID_ARROW_ENTITY_MODEL_LAYER));
    }

    @Override
    public void render(ArrowEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(state)));
        this.model.setAngles(state);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        if (state.displayName != null) {
            this.renderLabelIfPresent(state, state.displayName, matrixStack, vertexConsumerProvider, i);
        }
    }

    @Override
    public ArrowEntityRenderState createRenderState() {
        return new ArrowEntityRenderState();
    }

    @Override
    protected Identifier getTexture(ArrowEntityRenderState state) {
        return TEXTURE;
    }


}

