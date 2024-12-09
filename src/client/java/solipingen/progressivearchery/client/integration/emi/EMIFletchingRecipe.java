package solipingen.progressivearchery.client.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.recipe.FletchingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Environment(EnvType.CLIENT)
public class EMIFletchingRecipe implements EmiRecipe {
    protected final FletchingRecipe recipe;
    protected final EmiIngredient head;
    protected final EmiIngredient body;
    protected final EmiIngredient tail;
    protected final EmiIngredient addition;
    protected final EmiStack output;


    public EMIFletchingRecipe(FletchingRecipe recipe) {
        this.recipe = recipe;
        this.head = EmiIngredient.of(recipe.getIngredients().get(0));
        this.body = EmiIngredient.of(recipe.getIngredients().get(1));
        this.tail = EmiIngredient.of(recipe.getIngredients().get(2));
        this.addition = EmiIngredient.of(recipe.getIngredients().get(3));
        this.output = EmiStack.of(recipe.getOutput());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ModEMIClientPlugin.FLETCHING_CATEGORY;
    }

    @Override
    public Identifier getId() {
        Optional<RegistryEntry<Potion>> potionOptional = this.output.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion();
        return potionOptional.map(potionEntry -> Identifier.of(ProgressiveArchery.MOD_ID,
                "/" + FletchingRecipe.Type.ID + "/" + this.output.getId().getNamespace() + "/" + this.output.getId().getPath() + "_" + Registries.POTION.getId(potionEntry.value()).getPath())).orElse(null);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.head, this.body, this.tail, this.addition);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.output);
    }

    @Override
    public int getDisplayWidth() {
        return 128;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 23);
        widgets.addSlot(this.tail, 10, 4);
        widgets.addSlot(this.body, 32, 4);
        widgets.addSlot(this.head, 54, 4);
        widgets.addTexture(EmiTexture.PLUS, 34, 25);
        widgets.addSlot(this.addition, 32, 41);
        widgets.addSlot(this.output, 102, 23).recipeContext(this);
    }

    public FletchingRecipe getRecipe() {
        return this.recipe;
    }

    public static EMIFletchingRecipe setPotion(EMIFletchingRecipe recipe, @Nullable Potion potion) {
        List<Ingredient> ingredients = recipe.getRecipe().getIngredients();
        ArrayList<Ingredient> ingredients2 = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            List<ItemStack> stacks = List.of(ingredient.getMatchingStacks());
            if (stacks.size() != 1) {
                return recipe;
            }
            stacks.forEach(stack -> {
                    stack.set(DataComponentTypes.POTION_CONTENTS, potion != null ? new PotionContentsComponent(Registries.POTION.getEntry(potion)) : PotionContentsComponent.DEFAULT);
                    ingredients2.add(Ingredient.ofStacks(stack));
            });
        }
        ItemStack outputStack = recipe.output.getItemStack().copy();
        outputStack.set(DataComponentTypes.POTION_CONTENTS, potion != null ? new PotionContentsComponent(Registries.POTION.getEntry(potion)) : PotionContentsComponent.DEFAULT);
        FletchingRecipe fletchingRecipe = new FletchingRecipe(ingredients2.get(0), ingredients2.get(1), ingredients2.get(2), ingredients2.get(3), outputStack);
        return new EMIFletchingRecipe(fletchingRecipe);
    }


}
