package solipingen.progressivearchery.recipe.display;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ModRecipeDisplays {

    public static void registerModRecipeDisplays() {
        Registry.register(Registries.RECIPE_DISPLAY, Identifier.of(ProgressiveArchery.MOD_ID, "fletching"),
                FletchingRecipeDisplay.SERIALIZER);

        ProgressiveArchery.LOGGER.debug("Registering Mod Recipe Displays for " + ProgressiveArchery.MOD_ID);
    }

}
