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
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.recipe.FletchingRecipe;

import java.util.List;
import java.util.Optional;


@Environment(EnvType.CLIENT)
public class EMITippingRecipe implements EmiRecipe {
    protected final EmiIngredient arrow;
    protected final EmiIngredient potion;
    protected final EmiStack output;


    public EMITippingRecipe(ItemStack arrow, Potion potion, ItemStack output) {
        this.arrow = EmiIngredient.of(Ingredient.ofStacks(arrow));
        this.potion = EmiIngredient.of(Ingredient.ofStacks(PotionContentsComponent.createStack(Items.POTION, Registries.POTION.getEntry(potion))));
        output.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Registries.POTION.getEntry(potion)));
        this.output = EmiStack.of(output);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ModEMIClientPlugin.TIPPING_CATEGORY;
    }

    @Override
    @Nullable
    public Identifier getId() {
        Optional<RegistryEntry<Potion>> potionOptional = this.output.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion();
        return potionOptional.map(potionEntry -> Identifier.of(ProgressiveArchery.MOD_ID,
                "/tipping/" + this.output.getId().getNamespace() + "/" + this.output.getId().getPath() + "_" + Registries.POTION.getId(potionEntry.value()).getPath())).orElse(null);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.arrow, this.potion);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.output);
    }

    @Override
    public int getDisplayWidth() {
        return 64;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 20, 23);
        widgets.addSlot(this.arrow, 4, 3);
        widgets.addTexture(EmiTexture.PLUS, 6, 25);
        widgets.addSlot(this.potion, 4, 43);
        widgets.addTexture(new EmiTexture(Identifier.of(ProgressiveArchery.MOD_ID, "textures/emi/gui/emi_tipping.png"), 16, 0, 16, 16), 6, 61);
        widgets.addSlot(this.output, 44, 23).recipeContext(this);
    }
}
