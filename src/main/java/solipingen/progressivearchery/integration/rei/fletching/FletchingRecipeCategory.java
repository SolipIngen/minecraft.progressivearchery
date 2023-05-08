package solipingen.progressivearchery.integration.rei.fletching;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.gui.screen.ingame.FletchingScreen;
import solipingen.progressivearchery.integration.rei.ModREIClientPlugin;
import solipingen.progressivearchery.recipe.FletchingRecipe;


@Environment(value=EnvType.CLIENT)
public class FletchingRecipeCategory implements DisplayCategory<FletchingRecipeDisplay> {


    @Override
    public CategoryIdentifier<? extends FletchingRecipeDisplay> getCategoryIdentifier() {
        return ModREIClientPlugin.FLETCHING_CATEGORY_ID;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("recipe." + ProgressiveArchery.MOD_ID + "." + FletchingRecipe.Type.ID);
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.FLETCHING_TABLE);
    }

    @Override
    public List<Widget> setupDisplay(FletchingRecipeDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        Rectangle fletchingBounds = new Rectangle(origin.x + 7, origin.y + 7, 136, 54);
        widgets.add(Widgets.createTexturedWidget(FletchingScreen.TEXTURE, fletchingBounds, 16, 21));
        List<EntryIngredient> input = display.getInputEntries();
        if (input != null && !input.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(fletchingBounds.x + 3 + 50, fletchingBounds.y + 2)).entries(input.get(0)).markInput().disableBackground());
            widgets.add(Widgets.createSlot(new Point(fletchingBounds.x + 3 + 25, fletchingBounds.y + 2)).entries(input.get(1)).markInput().disableBackground());
            widgets.add(Widgets.createSlot(new Point(fletchingBounds.x + 3, fletchingBounds.y + 2)).entries(input.get(2)).markInput().disableBackground());
            widgets.add(Widgets.createSlot(new Point(fletchingBounds.x + 3 + 25, fletchingBounds.y + 2 + 30)).entries(input.get(3)).markInput().disableBackground());
        }
        widgets.add(Widgets.createSlot(new Point(fletchingBounds.x + 3 + 110, fletchingBounds.y + 2 + 14)).entries(display.getOutputEntries().get(0)).markOutput().disableBackground());
        return widgets;
    }
    


}
