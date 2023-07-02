package solipingen.progressivearchery.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.render.entity.ModEntityModelLayers;
import solipingen.progressivearchery.client.render.entity.feature.model.QuiverLeftHandedEntityModel;
import solipingen.progressivearchery.client.render.entity.feature.model.QuiverRightHandedEntityModel;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.util.interfaces.mixin.entity.player.PlayerEntityInterface;


@Environment(value = EnvType.CLIENT)
public class QuiverFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/models/quiver.png");
    private static final Identifier FILLED_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/models/quiver_filled.png");
    private final QuiverLeftHandedEntityModel<T> left_handed_model;
    private final QuiverRightHandedEntityModel<T> right_handed_model;

    
    public QuiverFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.left_handed_model = new QuiverLeftHandedEntityModel<>(loader.getModelPart(ModEntityModelLayers.QUIVER_LEFT_HANDED_ENTITY_MODEL_LAYER));
        this.right_handed_model = new QuiverRightHandedEntityModel<>(loader.getModelPart(ModEntityModelLayers.QUIVER_RIGHT_HANDED_ENTITY_MODEL_LAYER));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity;
        PlayerEntityInterface iPlayerEntity = (PlayerEntityInterface)abstractClientPlayerEntity;
        if (!(iPlayerEntity.hasQuiver())) {
            return;
        }
        Identifier identifier = TEXTURE;
        ItemStack quiverStack = ItemStack.EMPTY;
        for (int index = 0; index < abstractClientPlayerEntity.getInventory().size(); ++index) {
            quiverStack = abstractClientPlayerEntity.getInventory().getStack(index);
            if (quiverStack.isOf(ModItems.QUIVER)) {
                if (QuiverItem.getQuiverOccupancy(quiverStack) > 0) {
                    identifier = FILLED_TEXTURE;
                }
                break;
            }
        }
        matrixStack.push();
        matrixStack.translate(0.0f, 0.0f, 0.125f);
        if (livingEntity.getMainArm() == Arm.LEFT) {
            ((EntityModel<T>)this.getContextModel()).copyStateTo(this.left_handed_model);
            this.left_handed_model.setAngles(livingEntity, f, g, j, k, l);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(identifier), false, quiverStack.hasGlint());
            this.left_handed_model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            ((EntityModel<T>)this.getContextModel()).copyStateTo(this.right_handed_model);
            this.right_handed_model.setAngles(livingEntity, f, g, j, k, l);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(identifier), false, quiverStack.hasGlint());
            this.right_handed_model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        matrixStack.pop();
    }

}
