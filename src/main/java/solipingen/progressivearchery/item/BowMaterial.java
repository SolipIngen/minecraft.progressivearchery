package solipingen.progressivearchery.item;


import net.minecraft.recipe.Ingredient;


public interface BowMaterial {

    int getDurability();

    float getMiningSpeedMultiplier();

    float getAttackDamage();

    public int getMiningLevel();

    int getEnchantability();

    Ingredient getRepairIngredient();

}
