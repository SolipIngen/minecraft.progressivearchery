package solipingen.progressivearchery.client.render.entity.feature;

import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.StriderEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.render.entity.feature.model.StriderhairEntityModel;
import solipingen.progressivearchery.util.interfaces.mixin.entity.passive.StriderEntityInterface;
import net.fabricmc.api.EnvType;


@Environment(value=EnvType.CLIENT)
public class StriderhairFeatureRenderer extends FeatureRenderer<StriderEntity, StriderEntityModel<StriderEntity>> {
    private static final Identifier HAIR_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/strider/striderhair.png");
    private final StriderhairEntityModel<StriderEntity> model;

    
    public StriderhairFeatureRenderer(FeatureRendererContext<StriderEntity, StriderEntityModel<StriderEntity>> context, StriderhairEntityModel<StriderEntity> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, StriderEntity striderEntity, float f, float g, float h, float j, float k, float l) {
        float u = 1.0f;
        float t = 1.0f;
        float s = 1.0f;
        StriderEntityInterface iStriderEntity = (StriderEntityInterface)striderEntity;
        if (iStriderEntity.getIsSheared()) {
            return;
        }
        if (striderEntity.isInvisible()) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            boolean bl = minecraftClient.hasOutline(striderEntity);
            if (bl) {
                ((StriderEntityModel<StriderEntity>)this.getContextModel()).copyStateTo(this.model);
                this.model.animateModel(striderEntity, f, g, h);
                this.model.setAngles(striderEntity, f, g, j, k, l);
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(HAIR_TEXTURE));
                this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(striderEntity, 0.0f), 0.0f, 0.0f, 0.0f, 1.0f);
            }
            return;
        }
        StriderhairFeatureRenderer.render(this.getContextModel(), this.model, HAIR_TEXTURE, matrixStack, vertexConsumerProvider, i, striderEntity, f, g, j, k, l, h, s, t, u);
    }
    
}
