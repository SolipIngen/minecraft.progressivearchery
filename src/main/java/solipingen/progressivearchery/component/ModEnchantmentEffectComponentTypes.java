package solipingen.progressivearchery.component;


import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.item.ModCrossbowItem;

import java.util.List;
import java.util.function.UnaryOperator;

public interface ModEnchantmentEffectComponentTypes {

    ComponentType<List<ModCrossbowItem.LoadingSounds>> MOD_CROSSBOW_CHARGING_SOUNDS = ModEnchantmentEffectComponentTypes.register(
            "mod_crossbow_charging_sounds", builder -> builder.codec(ModCrossbowItem.LoadingSounds.CODEC.listOf())
    );


    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
                Identifier.of(ProgressiveArchery.MOD_ID, name), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerModEnchantmentEffectComponentTypes() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Enchantment Effect Component Types for " + ProgressiveArchery.MOD_ID);
    }

}
