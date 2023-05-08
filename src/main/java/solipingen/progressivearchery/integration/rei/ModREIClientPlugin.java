package solipingen.progressivearchery.integration.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.gui.screen.ingame.FletchingScreen;
import solipingen.progressivearchery.integration.rei.fletching.FletchingRecipeCategory;
import solipingen.progressivearchery.integration.rei.fletching.FletchingRecipeDisplay;
import solipingen.progressivearchery.recipe.FletchingRecipe;


@Environment(value=EnvType.CLIENT)
public class ModREIClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<FletchingRecipeDisplay> FLETCHING_CATEGORY_ID = CategoryIdentifier.of(ProgressiveArchery.MOD_ID, FletchingRecipe.Type.ID);
    
    
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FletchingRecipeCategory());
        registry.addWorkstations(FLETCHING_CATEGORY_ID, EntryStacks.of(Blocks.FLETCHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FletchingRecipe.class, FletchingRecipe.Type.INSTANCE, FletchingRecipeDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(14, 20, 138, 56), FletchingScreen.class, FLETCHING_CATEGORY_ID);
    }



}
