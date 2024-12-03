package solipingen.progressivearchery.client.integration.rei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.integration.rei.fletching.FletchingRecipeCategory;
import solipingen.progressivearchery.client.integration.rei.fletching.FletchingRecipeDisplay;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.recipe.FletchingRecipe;
import solipingen.progressivearchery.registry.tag.ModItemTags;


@Environment(value = EnvType.CLIENT)
public class ModREIClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<FletchingRecipeDisplay> FLETCHING_CATEGORY_ID = CategoryIdentifier.of(ProgressiveArchery.MOD_ID, FletchingRecipe.Type.ID);
    
    
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FletchingRecipeCategory());
        registry.addWorkstations(FLETCHING_CATEGORY_ID, EntryStacks.of(Blocks.FLETCHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FletchingRecipe.class, (recipeType) -> recipeType instanceof FletchingRecipe.Type, 
            (recipe) -> recipe instanceof RecipeEntry<FletchingRecipe> && !recipe.value().getOutput().isOf(ModItems.TIPPED_KID_ARROW), 
            FletchingRecipeDisplay::new
        );
        Stream<EntryStack<?>> entryStacksStream = EntryRegistry.getInstance().getEntryStacks().filter(entry -> entry.getValueType() == ItemStack.class && entry.<ItemStack>castValue().getItem() == Items.TIPPED_ARROW);
        ArrayList<Item> additionItemList = new ArrayList<Item>();
        for (Item item : Registries.ITEM) {
            if (item.getDefaultStack().isIn(ModItemTags.METAL_ARROW_CUTTERS)) {
                additionItemList.add(item);
            }
        }
        entryStacksStream.forEach((entry) -> {
            ItemStack itemStack = (ItemStack)entry.getValue();
            RegistryEntry<Potion> potionEntry = itemStack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().get();
            List<EntryIngredient> input = new ArrayList<EntryIngredient>();
            for (int i = 0; i < 3; i++) {
                input.add(EntryIngredients.of(itemStack));
            }
            ItemStack outputStack = PotionContentsComponent.createStack(ModItems.TIPPED_KID_ARROW, potionEntry);
            outputStack.setCount(6);
            List<EntryIngredient> output = Collections.singletonList(EntryIngredients.of(outputStack));
            for (Item additionItem : additionItemList) {
                if (input.size() < 4) {
                    input.add(EntryIngredients.of(additionItem.getDefaultStack()));
                }
                else {
                    input.set(3, EntryIngredients.of(additionItem.getDefaultStack()));
                }
                registry.add(new FletchingRecipeDisplay(input, output));
            }
        });
    }

    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        registry.group(Identifier.of(ProgressiveArchery.MOD_ID, "tipped_kid_arrow"), Text.translatable("item.progressivearchery.tipped_kid_arrow"),
                stack -> stack.getType() == VanillaEntryTypes.ITEM && stack.<ItemStack>castValue().isOf(ModItems.TIPPED_KID_ARROW));
    }



}
