package solipingen.progressivearchery.client.integration.rei.fletching;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import solipingen.progressivearchery.client.integration.rei.ModREIClientPlugin;
import solipingen.progressivearchery.recipe.FletchingRecipe;


@Environment(value=EnvType.CLIENT)
public class FletchingRecipeDisplay extends BasicDisplay {


    public FletchingRecipeDisplay(FletchingRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())), Optional.ofNullable(recipe.getId()));
    }

    public FletchingRecipeDisplay(List<EntryIngredient> input, List<EntryIngredient> output) {
        super(input, output);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ModREIClientPlugin.FLETCHING_CATEGORY_ID;
    }
    
    
}
