package solipingen.progressivearchery.client.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.recipe.FletchingRecipe;

import java.util.function.Supplier;


@Environment(EnvType.CLIENT)
public class ModEMIClientPlugin implements EmiPlugin {
    public static final EmiStack FLETCHING_TABLE = EmiStack.of(Items.FLETCHING_TABLE);
    public static final EmiStack CAULDRON = EmiStack.of(Items.CAULDRON);
    public static final EmiRecipeCategory FLETCHING_CATEGORY = new EmiRecipeCategory(new Identifier(ProgressiveArchery.MOD_ID, "fletching"), FLETCHING_TABLE,
            new EmiTexture(new Identifier(ProgressiveArchery.MOD_ID, "textures/emi/gui/emi_fletching.png"), 0, 0, 16, 16));
    public static final EmiRecipeCategory TIPPING_CATEGORY = new EmiRecipeCategory(new Identifier(ProgressiveArchery.MOD_ID, "tipping"), CAULDRON,
            new EmiTexture(new Identifier(ProgressiveArchery.MOD_ID, "textures/emi/gui/emi_tipping.png"), 0, 0, 16, 16));


    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(TIPPING_CATEGORY);
        registry.addWorkstation(TIPPING_CATEGORY, CAULDRON);
        Registries.POTION.forEach(potion -> {
            registry.addRecipe(new EMITippingRecipe(new ItemStack(ModItems.GOLDEN_ARROW).copyWithCount(PotionCauldronBlock.TIPPING_PER_LEVEL), potion, new ItemStack(Items.TIPPED_ARROW).copyWithCount(PotionCauldronBlock.TIPPING_PER_LEVEL)));
            registry.addRecipe(new EMITippingRecipe(new ItemStack(ModItems.GOLDEN_KID_ARROW).copyWithCount(PotionCauldronBlock.TIPPING_PER_LEVEL), potion, new ItemStack(ModItems.TIPPED_KID_ARROW).copyWithCount(PotionCauldronBlock.TIPPING_PER_LEVEL)));
        });
        registry.addCategory(FLETCHING_CATEGORY);
        registry.addWorkstation(FLETCHING_CATEGORY, FLETCHING_TABLE);
        RecipeManager manager = registry.getRecipeManager();
        Comparison potionComparison = Comparison.compareData(stack -> stack.get(DataComponentTypes.POTION_CONTENTS));
        registry.setDefaultComparison(ModItems.TIPPED_KID_ARROW, potionComparison);
        for (RecipeEntry<FletchingRecipe> recipeEntry : manager.listAllOfType(FletchingRecipe.Type.INSTANCE)) {
            FletchingRecipe recipe = recipeEntry.value();
            EMIFletchingRecipe emiFletchingRecipe = new EMIFletchingRecipe(recipe);
            if (recipe.getOutput().isOf(ModItems.TIPPED_KID_ARROW)) {
                Registries.POTION.forEach(potion -> ModEMIClientPlugin.addRecipeSafe(registry, () -> EMIFletchingRecipe.setPotion(emiFletchingRecipe, potion), recipe));
            }
            else {
                registry.addRecipe(emiFletchingRecipe);
            }
        }
    }

    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, Recipe<?> recipe) {
        try {
            registry.addRecipe(supplier.get());
        }
        catch (Throwable e) {
            ProgressiveArchery.LOGGER.warn("Exception thrown when parsing recipe {}", recipe.getType().toString());
            ProgressiveArchery.LOGGER.error(String.valueOf(e));
        }
    }


}
