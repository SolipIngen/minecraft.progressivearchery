package solipingen.progressivearchery.client.integration.rei.fletching;

import java.util.Collections;
import java.util.List;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.RecipeEntry;
import solipingen.progressivearchery.client.integration.rei.ModREIClientPlugin;
import solipingen.progressivearchery.recipe.FletchingRecipe;


@Environment(value=EnvType.CLIENT)
public class FletchingRecipeDisplay extends BasicDisplay {


    public FletchingRecipeDisplay(RecipeEntry<FletchingRecipe> recipe) {
        super(EntryIngredients.ofIngredients(recipe.value().getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.value().getOutput())));
    }

    public FletchingRecipeDisplay(List<EntryIngredient> input, List<EntryIngredient> output) {
        super(input, output);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ModREIClientPlugin.FLETCHING_CATEGORY_ID;
    }
    
    
}
