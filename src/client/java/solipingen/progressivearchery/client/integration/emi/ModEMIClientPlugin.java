package solipingen.progressivearchery.client.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.recipe.FletchingRecipe;

import java.util.List;
import java.util.function.Supplier;


@Environment(EnvType.CLIENT)
public class ModEMIClientPlugin implements EmiPlugin {
    public static final EmiStack FLETCHING_TABLE = EmiStack.of(Items.FLETCHING_TABLE);
    public static final EmiStack CAULDRON = EmiStack.of(Items.CAULDRON);
    public static final EmiRecipeCategory FLETCHING_CATEGORY = new EmiRecipeCategory(Identifier.of(ProgressiveArchery.MOD_ID, "fletching"), FLETCHING_TABLE,
            new EmiTexture(Identifier.of(ProgressiveArchery.MOD_ID, "textures/emi/gui/emi_fletching.png"), 0, 0, 16, 16));
    public static final EmiRecipeCategory TIPPING_CATEGORY = new EmiRecipeCategory(Identifier.of(ProgressiveArchery.MOD_ID, "tipping"), CAULDRON,
            new EmiTexture(Identifier.of(ProgressiveArchery.MOD_ID, "textures/emi/gui/emi_tipping.png"), 0, 0, 16, 16));


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
        Registries.ITEM.streamEntries().forEach(itemEntry -> {
            if (itemEntry.value().getDefaultStack().isIn(ItemTags.BOW_ENCHANTABLE) || itemEntry.value().getDefaultStack().isIn(ItemTags.CROSSBOW_ENCHANTABLE)) {
                if (MinecraftClient.getInstance().world != null) {
                    RegistryEntryLookup<Enchantment> enchantmentLookup = MinecraftClient.getInstance().world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
                    RegistryEntry<Enchantment> quickChargeEnchantment = enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE);
                    for (int j = 0; j < quickChargeEnchantment.value().getMaxLevel(); j++) {
                        ItemStack bowStack = new ItemStack(itemEntry.value());
                        if (j > 0) {
                            bowStack.addEnchantment(quickChargeEnchantment, j);
                        }
                        EmiStack bow = EmiStack.of(bowStack);
                        EmiStack hair = switch (j) {
                            case 0 -> EmiStack.of(ModItems.HORSEHAIR);
                            case 1 -> EmiStack.of(ModItems.STRIDERHAIR);
                            case 2 -> EmiStack.of(ModItems.HOGLINHAIR);
                            default -> EmiStack.EMPTY;
                        };
                        List<EmiIngredient> rightInput = List.of(
                                EmiStack.EMPTY, EmiStack.EMPTY, hair,
                                EmiStack.EMPTY, bow, hair,
                                EmiStack.EMPTY, EmiStack.EMPTY, hair
                        );
                        List<EmiIngredient> leftInput = List.of(
                                hair, EmiStack.EMPTY, EmiStack.EMPTY,
                                hair, bow, EmiStack.EMPTY,
                                hair, EmiStack.EMPTY, EmiStack.EMPTY
                        );
                        bowStack.addEnchantment(quickChargeEnchantment, j + 1);
                        EmiStack output = EmiStack.of(bowStack);
                        int level = j;
                        ModEMIClientPlugin.addRecipeSafeNoType(registry, () ->
                                new EmiCraftingRecipe(rightInput, output,
                                        Identifier.of(ProgressiveArchery.MOD_ID,
                                                "/crafting/quick_charge_bow/" + Registries.ITEM.getId(itemEntry.value()).getPath() + "_" + level + "_right"),
                                        false));
                        ModEMIClientPlugin.addRecipeSafeNoType(registry, () ->
                                new EmiCraftingRecipe(leftInput, output,
                                        Identifier.of(ProgressiveArchery.MOD_ID,
                                                "/crafting/quick_charge_bow/" + Registries.ITEM.getId(itemEntry.value()).getPath() + "_" +  level + "_left"),
                                        false));
                    }
                }
            }
        });

    }

    private static void addRecipeSafeNoType(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
        try {
            registry.addRecipe(supplier.get());
        }
        catch (Throwable e) {
            ProgressiveArchery.LOGGER.warn("Exception thrown when parsing EMI recipe.");
            ProgressiveArchery.LOGGER.error(String.valueOf(e));
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
