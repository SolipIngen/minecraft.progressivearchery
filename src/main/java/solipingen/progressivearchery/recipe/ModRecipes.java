package solipingen.progressivearchery.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ModRecipes {
    
    
    public static void registerModRecipes() {
        
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(ProgressiveArchery.MOD_ID, FletchingRecipe.Serializer.ID),
            FletchingRecipe.Serializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE, new Identifier(ProgressiveArchery.MOD_ID, FletchingRecipe.Type.ID),
            FletchingRecipe.Type.INSTANCE);

        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(ProgressiveArchery.MOD_ID, "crafting_special_quickcharge"), 
            QuickChargeRecipe.QUICK_CHARGE_RECIPE_SERIALIZER);
    
    }
}
