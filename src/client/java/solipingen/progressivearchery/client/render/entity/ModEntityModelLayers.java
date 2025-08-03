package solipingen.progressivearchery.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.render.entity.feature.model.QuiverLeftHandedEntityModel;
import solipingen.progressivearchery.client.render.entity.feature.model.QuiverRightHandedEntityModel;
import solipingen.progressivearchery.client.render.entity.model.projectile.kid_arrow.KidArrowEntityModel;


@Environment(value = EnvType.CLIENT)
public class ModEntityModelLayers {
    public static final EntityModelLayer KID_ARROW_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(ProgressiveArchery.MOD_ID, "kid_arrow"), "main");

    public static final EntityModelLayer QUIVER_LEFT_HANDED_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(ProgressiveArchery.MOD_ID, "quiver_left_handed"), "main");
    public static final EntityModelLayer QUIVER_RIGHT_HANDED_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(ProgressiveArchery.MOD_ID, "quiver_right_handed"), "main");


    public static void registerModEntityLayers() {
        // Kid Arrow Layer
        EntityModelLayerRegistry.registerModelLayer(KID_ARROW_ENTITY_MODEL_LAYER, KidArrowEntityModel::getTexturedModelData);

        // Quiver Model Layers
        EntityModelLayerRegistry.registerModelLayer(QUIVER_RIGHT_HANDED_ENTITY_MODEL_LAYER, QuiverRightHandedEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(QUIVER_LEFT_HANDED_ENTITY_MODEL_LAYER, QuiverLeftHandedEntityModel::getTexturedModelData);

        ProgressiveArchery.LOGGER.debug("Registering Mod Entity Models for " + ProgressiveArchery.MOD_ID);
    }

    
}
