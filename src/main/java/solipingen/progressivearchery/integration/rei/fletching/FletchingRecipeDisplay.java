package solipingen.progressivearchery.integration.rei.fletching;

import java.util.Collections;
import java.util.Optional;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import solipingen.progressivearchery.integration.rei.ModREIClientPlugin;
import solipingen.progressivearchery.recipe.FletchingRecipe;


@Environment(value=EnvType.CLIENT)
public class FletchingRecipeDisplay extends BasicDisplay {


    public FletchingRecipeDisplay(FletchingRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())), Optional.ofNullable(recipe.getId()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ModREIClientPlugin.FLETCHING_CATEGORY_ID;
    }
    
    
}
