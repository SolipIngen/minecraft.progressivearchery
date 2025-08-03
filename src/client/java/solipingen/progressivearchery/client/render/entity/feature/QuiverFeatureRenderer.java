package solipingen.progressivearchery.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.render.entity.ModEntityModelLayers;
import solipingen.progressivearchery.client.render.entity.feature.model.QuiverLeftHandedEntityModel;
import solipingen.progressivearchery.client.render.entity.feature.model.QuiverRightHandedEntityModel;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.PlayerEntityRenderStateInterface;
import solipingen.progressivearchery.item.QuiverItem;


@Environment(value = EnvType.CLIENT)
public class QuiverFeatureRenderer<S extends BipedEntityRenderState, M extends EntityModel<S>> extends FeatureRenderer<S, M> {
    private static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/equipment/quiver/quiver.png");
    private static final Identifier FILLED_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/equipment/quiver/quiver_filled.png");
    private final QuiverLeftHandedEntityModel leftHandedModel;
    private final QuiverRightHandedEntityModel rightHandedModel;

    
    public QuiverFeatureRenderer(FeatureRendererContext<S, M> context, LoadedEntityModels loader, EquipmentRenderer equipmentRenderer) {
        super(context);
        this.leftHandedModel = new QuiverLeftHandedEntityModel(loader.getModelPart(ModEntityModelLayers.QUIVER_LEFT_HANDED_ENTITY_MODEL_LAYER));
        this.rightHandedModel = new QuiverRightHandedEntityModel(loader.getModelPart(ModEntityModelLayers.QUIVER_RIGHT_HANDED_ENTITY_MODEL_LAYER));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S bipedEntityRenderState, float f, float g) {
        if (!(bipedEntityRenderState instanceof PlayerEntityRenderState)) return;
        PlayerEntityRenderStateInterface iState = (PlayerEntityRenderStateInterface)bipedEntityRenderState;
        ItemStack quiverStack = iState.getQuiverStack();
        if (quiverStack.isEmpty()) return;
        Identifier identifier = TEXTURE;
        if (QuiverItem.getAmountFilled(quiverStack) > 0.0f) {
            identifier = FILLED_TEXTURE;
        }
        matrixStack.push();
        matrixStack.translate(0.0f, 0.0f, 0.125f);
        if (bipedEntityRenderState.mainArm == Arm.LEFT) {
            this.leftHandedModel.setAngles(bipedEntityRenderState);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(identifier), false);
            this.leftHandedModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
            if (quiverStack.hasGlint()) {
                this.leftHandedModel.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getArmorEntityGlint()), i, OverlayTexture.DEFAULT_UV);
            }
        }
        else {
            this.rightHandedModel.setAngles(bipedEntityRenderState);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(identifier), false);
            this.rightHandedModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
            if (quiverStack.hasGlint()) {
                this.rightHandedModel.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getArmorEntityGlint()), i, OverlayTexture.DEFAULT_UV);
            }
        }
        matrixStack.pop();
    }

}
