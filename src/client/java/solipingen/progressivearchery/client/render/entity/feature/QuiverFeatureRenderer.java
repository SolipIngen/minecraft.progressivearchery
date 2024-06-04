package solipingen.progressivearchery.client.render.entity.feature;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
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
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.util.interfaces.mixin.entity.player.PlayerEntityInterface;

import java.util.Map;
import java.util.Optional;


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
        Identifier identifier = TEXTURE;
        ItemStack quiverStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinketComponentOptional = TrinketsApi.getTrinketComponent(abstractClientPlayerEntity);
        boolean trinketSlotOccupiedBl = false;
        if (trinketComponentOptional.isPresent()) {
            Map<String, Map<String, TrinketInventory>> trinketInventoryMap = trinketComponentOptional.get().getInventory();
            if (trinketInventoryMap.containsKey("chest") && trinketInventoryMap.get("chest").containsKey("back")) {
                TrinketInventory trinketInventory = trinketInventoryMap.get("chest").get("back");
                for (int index = 0; index < trinketInventory.size(); index++) {
                    if (trinketInventory.getStack(index).getItem() instanceof QuiverItem) {
                        trinketSlotOccupiedBl = true;
                        quiverStack = trinketInventory.getStack(index);
                        if (QuiverItem.getAmountFilled(quiverStack) > 0.0f) {
                            identifier = FILLED_TEXTURE;
                        }
                        break;
                    }
                }
            }
        }
        if (!trinketSlotOccupiedBl && !iPlayerEntity.hasQuiver()) {
            return;
        }
        if (quiverStack.isEmpty()) {
            for (int index = 0; index < abstractClientPlayerEntity.getInventory().size(); index++) {
                quiverStack = abstractClientPlayerEntity.getInventory().getStack(index);
                if (quiverStack.getItem() instanceof QuiverItem) {
                    if (QuiverItem.getAmountFilled(quiverStack) > 0.0f) {
                        identifier = FILLED_TEXTURE;
                    }
                    break;
                }
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
