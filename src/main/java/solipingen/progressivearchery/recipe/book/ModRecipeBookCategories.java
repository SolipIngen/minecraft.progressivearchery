package solipingen.progressivearchery.recipe.book;


import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;

public class ModRecipeBookCategories {

    public static final RecipeBookCategory FLETCHING = ModRecipeBookCategories.register("fletching");


    private static RecipeBookCategory register(String name) {
        return Registry.register(Registries.RECIPE_BOOK_CATEGORY, Identifier.of(ProgressiveArchery.MOD_ID, name),
                new RecipeBookCategory());
    }

    public static void registerModRecipeBookCategories() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Recipe Book Categories for " + ProgressiveArchery.MOD_ID);
    }


}
