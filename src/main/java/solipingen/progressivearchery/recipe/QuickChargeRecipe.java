package solipingen.progressivearchery.recipe;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModItems;


public class QuickChargeRecipe extends SpecialCraftingRecipe {
    public static final RecipeSerializer<QuickChargeRecipe> QUICK_CHARGE_RECIPE_SERIALIZER = new SpecialRecipeSerializer<QuickChargeRecipe>(QuickChargeRecipe::new);

    
    public QuickChargeRecipe(CraftingRecipeCategory category) {
        super(category);
    }
    
    @Override
    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        if (craftingRecipeInput.getWidth() != 2 || craftingRecipeInput.getHeight() != 3) {
            return false;
        }
        boolean bowLeftBl = craftingRecipeInput.getStackInSlot(2).isIn(ItemTags.BOW_ENCHANTABLE) || craftingRecipeInput.getStackInSlot(2).isIn(ItemTags.CROSSBOW_ENCHANTABLE);
        boolean bowRightBl = craftingRecipeInput.getStackInSlot(3).isIn(ItemTags.BOW_ENCHANTABLE) || craftingRecipeInput.getStackInSlot(2).isIn(ItemTags.CROSSBOW_ENCHANTABLE);
        boolean horsehairLeftBl = craftingRecipeInput.getStackInSlot(0).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(2).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(4).isOf(ModItems.HORSEHAIR);
        boolean horsehairRightBl = craftingRecipeInput.getStackInSlot(1).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(3).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(5).isOf(ModItems.HORSEHAIR);
        boolean striderhairLeftBl = craftingRecipeInput.getStackInSlot(0).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(2).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(4).isOf(ModItems.STRIDERHAIR);
        boolean striderhairRightBl = craftingRecipeInput.getStackInSlot(1).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(3).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(5).isOf(ModItems.STRIDERHAIR);
        boolean hoglinhairLeftBl = craftingRecipeInput.getStackInSlot(0).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(2).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(4).isOf(ModItems.HOGLINHAIR);
        boolean hoglinhairRightBl = craftingRecipeInput.getStackInSlot(1).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(3).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(5).isOf(ModItems.HOGLINHAIR);
        return (bowLeftBl && (horsehairRightBl || striderhairRightBl || hoglinhairRightBl)) ^ (bowRightBl && (horsehairLeftBl || striderhairLeftBl || hoglinhairLeftBl));
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack leftStack = craftingRecipeInput.getStackInSlot(craftingRecipeInput.getWidth()).copy();
        ItemStack rightStack = craftingRecipeInput.getStackInSlot(1 + craftingRecipeInput.getWidth()).copy();
        if (leftStack.isIn(ItemTags.BOW_ENCHANTABLE) || leftStack.isIn(ItemTags.CROSSBOW_ENCHANTABLE)) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = wrapperLookup.createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int quickChargeLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), leftStack);
            if (quickChargeLevel >= 3) {
                return ItemStack.EMPTY;
            }
            boolean horsehairRightBl = craftingRecipeInput.getStackInSlot(1).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(3).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(5).isOf(ModItems.HORSEHAIR);
            boolean striderhairRightBl = craftingRecipeInput.getStackInSlot(1).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(3).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(5).isOf(ModItems.STRIDERHAIR);
            boolean hoglinhairRightBl = craftingRecipeInput.getStackInSlot(1).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(3).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(5).isOf(ModItems.HOGLINHAIR);
            if (quickChargeLevel <= 0 && horsehairRightBl) {
                leftStack.addEnchantment(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), quickChargeLevel + 1);
                return leftStack;
            }
            else if (quickChargeLevel == 1 && striderhairRightBl) {
                leftStack.addEnchantment(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), quickChargeLevel + 1);
                return leftStack;
            }
            else if (quickChargeLevel == 2 && hoglinhairRightBl) {
                leftStack.addEnchantment(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), quickChargeLevel + 1);
                return leftStack;
            }
        }
        if (rightStack.isIn(ItemTags.BOW_ENCHANTABLE) || rightStack.isIn(ItemTags.CROSSBOW_ENCHANTABLE)) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = wrapperLookup.createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int quickChargeLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), rightStack);
            if (quickChargeLevel >= 3) {
                return ItemStack.EMPTY;
            }
            boolean horsehairLeftBl = craftingRecipeInput.getStackInSlot(0).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(2).isOf(ModItems.HORSEHAIR) && craftingRecipeInput.getStackInSlot(4).isOf(ModItems.HORSEHAIR);
            boolean striderhairLeftBl = craftingRecipeInput.getStackInSlot(0).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(2).isOf(ModItems.STRIDERHAIR) && craftingRecipeInput.getStackInSlot(4).isOf(ModItems.STRIDERHAIR);
            boolean hoglinhairLeftBl = craftingRecipeInput.getStackInSlot(0).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(2).isOf(ModItems.HOGLINHAIR) && craftingRecipeInput.getStackInSlot(4).isOf(ModItems.HOGLINHAIR);
            if (quickChargeLevel <= 0 && horsehairLeftBl) {
                rightStack.addEnchantment(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), quickChargeLevel + 1);
                return rightStack;
            }
            else if (quickChargeLevel == 1 && striderhairLeftBl) {
                rightStack.addEnchantment(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), quickChargeLevel + 1);
                return rightStack;
            }
            else if (quickChargeLevel == 2 && hoglinhairLeftBl) {
                rightStack.addEnchantment(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), quickChargeLevel + 1);
                return rightStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return QUICK_CHARGE_RECIPE_SERIALIZER;
    }
    

}
