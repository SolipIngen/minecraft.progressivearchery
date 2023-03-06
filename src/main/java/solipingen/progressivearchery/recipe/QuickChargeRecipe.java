package solipingen.progressivearchery.recipe;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModItems;


public class QuickChargeRecipe extends SpecialCraftingRecipe {
    public static final RecipeSerializer<QuickChargeRecipe> QUICK_CHARGE_RECIPE_SERIALIZER = new SpecialRecipeSerializer<QuickChargeRecipe>(QuickChargeRecipe::new);

    
    public QuickChargeRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }
    
    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        if (craftingInventory.getWidth() != 3 || craftingInventory.getHeight() != 3) {
            return false;
        }
        boolean horsehairLeftBl = craftingInventory.getStack(0).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(3).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(6).isOf(ModItems.HORSEHAIR);
        boolean horsehairRightBl = craftingInventory.getStack(2).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(5).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(8).isOf(ModItems.HORSEHAIR);
        boolean striderhairLeftBl = craftingInventory.getStack(0).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(3).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(6).isOf(ModItems.STRIDERHAIR);
        boolean striderhairRightBl = craftingInventory.getStack(2).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(5).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(8).isOf(ModItems.STRIDERHAIR);
        boolean hoglinhairLeftBl = craftingInventory.getStack(0).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(3).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(6).isOf(ModItems.HOGLINHAIR);
        boolean hoglinhairRightBl = craftingInventory.getStack(2).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(5).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(8).isOf(ModItems.HOGLINHAIR);
        return craftingInventory.getStack(4).getItem() instanceof RangedWeaponItem && ((horsehairLeftBl ^ horsehairRightBl) || (striderhairLeftBl ^ striderhairRightBl) || (hoglinhairLeftBl ^ hoglinhairRightBl));
    }

    @Override
    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack itemStack = craftingInventory.getStack(1 + craftingInventory.getWidth());
        if (!(itemStack.getItem() instanceof RangedWeaponItem)) {
            return ItemStack.EMPTY;
        }

        int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, itemStack);
        if (quickChargeLevel >= 3) {
            return ItemStack.EMPTY;
        }

        boolean horsehairLeftBl = craftingInventory.getStack(0).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(3).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(6).isOf(ModItems.HORSEHAIR);
        boolean horsehairRightBl = craftingInventory.getStack(2).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(5).isOf(ModItems.HORSEHAIR) && craftingInventory.getStack(8).isOf(ModItems.HORSEHAIR);
        boolean striderhairLeftBl = craftingInventory.getStack(0).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(3).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(6).isOf(ModItems.STRIDERHAIR);
        boolean striderhairRightBl = craftingInventory.getStack(2).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(5).isOf(ModItems.STRIDERHAIR) && craftingInventory.getStack(8).isOf(ModItems.STRIDERHAIR);
        boolean hoglinhairLeftBl = craftingInventory.getStack(0).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(3).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(6).isOf(ModItems.HOGLINHAIR);
        boolean hoglinhairRightBl = craftingInventory.getStack(2).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(5).isOf(ModItems.HOGLINHAIR) && craftingInventory.getStack(8).isOf(ModItems.HOGLINHAIR);

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);

        if (quickChargeLevel <= 0 && (horsehairLeftBl ^ horsehairRightBl)) {
            enchantments.put(Enchantments.QUICK_CHARGE, quickChargeLevel + 1);
        }
        else if (quickChargeLevel == 1 && (striderhairLeftBl ^ striderhairRightBl)) {
            enchantments.replace(Enchantments.QUICK_CHARGE, quickChargeLevel + 1);
        }
        else if (quickChargeLevel == 2 && (hoglinhairLeftBl ^ hoglinhairRightBl)) {
            enchantments.replace(Enchantments.QUICK_CHARGE, quickChargeLevel + 1);
        }
        else {
            return ItemStack.EMPTY;
        }
        ItemStack resultItemStack = itemStack.copy();
        EnchantmentHelper.set(enchantments, resultItemStack);
        return resultItemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return QUICK_CHARGE_RECIPE_SERIALIZER;
    }
    

}
