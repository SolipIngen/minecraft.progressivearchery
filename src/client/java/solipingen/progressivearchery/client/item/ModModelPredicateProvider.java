package solipingen.progressivearchery.client.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.QuiverItem;


@Environment(value = EnvType.CLIENT)
public class ModModelPredicateProvider {

    
    public static void registerModItemModelPredicates() {
        
        //Bows
        ModModelPredicateProvider.registerBow(Items.BOW);
        ModModelPredicateProvider.registerBow(ModItems.COPPER_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.IRON_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.GOLD_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.DIAMOND_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.NETHERITE_FUSED_BOW);
        
        //Horn Bows
        ModModelPredicateProvider.registerHornBow(ModItems.HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.COPPER_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.IRON_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.GOLD_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.DIAMOND_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.NETHERITE_FUSED_HORN_BOW);


        //Tubular Bows
        ModModelPredicateProvider.registerTubularBow(ModItems.TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.COPPER_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.IRON_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.GOLD_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.DIAMOND_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.NETHERITE_FUSED_TUBULAR_BOW);


        //Longbows
        ModModelPredicateProvider.registerLongbow(ModItems.LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.COPPER_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.IRON_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.GOLD_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.DIAMOND_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.NETHERITE_FUSED_LONGBOW);

        //Crossbows
        ModModelPredicateProvider.registerCrossbow(ModItems.COPPER_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.IRON_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.GOLD_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.DIAMOND_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.NETHERITE_FUSED_CROSSBOW);

        //Quivers
        ModModelPredicateProvider.registerQuiver(ModItems.QUIVER);
    }
    

    private static void registerBow(Item bow) {
        ModelPredicateProviderRegistry.register(bow, Identifier.of("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            RegistryEntryLookup<Enchantment> enchantmentLookup = entity.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft())/(10.0f - 2.5f*i);
        });
        ModelPredicateProviderRegistry.register(bow, Identifier.of("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerHornBow(Item horn_bow) {
        ModelPredicateProviderRegistry.register(horn_bow, Identifier.of("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            RegistryEntryLookup<Enchantment> enchantmentLookup = entity.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft())/(20.0f - 5.0f*i);
        });
        ModelPredicateProviderRegistry.register(horn_bow, Identifier.of("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerLongbow(Item longbow) {
        ModelPredicateProviderRegistry.register(longbow, Identifier.of("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            RegistryEntryLookup<Enchantment> enchantmentLookup = entity.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft())/(30.0f - 7.5f*i);
        });
        ModelPredicateProviderRegistry.register(longbow, Identifier.of("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerTubularBow(Item tubular_bow) {
        ModelPredicateProviderRegistry.register(tubular_bow, Identifier.of("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            RegistryEntryLookup<Enchantment> enchantmentLookup = entity.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft())/(40.0f - 10.0f*i);
        });
        ModelPredicateProviderRegistry.register(tubular_bow, Identifier.of("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerCrossbow(Item crossbow) {
        ModelPredicateProviderRegistry.register(crossbow, Identifier.of("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            float pulltime = ModCrossbowItem.getPullTime(stack, entity.getWorld());
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft())/pulltime;
        });
        ModelPredicateProviderRegistry.register(crossbow, Identifier.of("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !ModCrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(crossbow, Identifier.of("charged"), (stack, world, entity, seed) -> ModCrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(crossbow, Identifier.of("firework"), (stack, world, entity, seed) -> ModCrossbowItem.isCharged(stack) && ModCrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0f : 0.0f);
    }

    private static void registerQuiver(Item quiver) {
        ModelPredicateProviderRegistry.register(quiver, Identifier.of("filled"), (stack, world, entity, seed) -> QuiverItem.getAmountFilled(stack));
    }

}
