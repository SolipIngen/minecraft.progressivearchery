package solipingen.progressivearchery.client.render.item.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModelTypes;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


@Environment(EnvType.CLIENT)
public class ModItemModelTypes {


    public static void registerModelItemModelTypes() {
        ItemModelTypes.ID_MAPPER.put(Identifier.of(ProgressiveArchery.MOD_ID, "quiver/selected_item"), QuiverSelectedItemModel.Unbaked.CODEC);

        ProgressiveArchery.LOGGER.debug("Registering Mod Item Model Types for " + ProgressiveArchery.MOD_ID);
    }

}
