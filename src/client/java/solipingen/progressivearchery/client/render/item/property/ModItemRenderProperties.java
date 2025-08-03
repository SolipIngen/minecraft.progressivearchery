package solipingen.progressivearchery.client.render.item.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


@Environment(EnvType.CLIENT)
public class ModItemRenderProperties {


    public static void registerModItemRenderProperties() {
        NumericProperties.ID_MAPPER.put(Identifier.of(ProgressiveArchery.MOD_ID, "bow/pull"), BowPullProperty.CODEC);
        BooleanProperties.ID_MAPPER.put(Identifier.of(ProgressiveArchery.MOD_ID, "quiver/has_item"), QuiverHasItemProperty.CODEC);
        BooleanProperties.ID_MAPPER.put(Identifier.of(ProgressiveArchery.MOD_ID, "quiver/has_selected_item"), QuiverHasSelectedItemProperty.CODEC);

        ProgressiveArchery.LOGGER.debug("Registering Mod Item Render Properties for " + ProgressiveArchery.MOD_ID);
    }

}
