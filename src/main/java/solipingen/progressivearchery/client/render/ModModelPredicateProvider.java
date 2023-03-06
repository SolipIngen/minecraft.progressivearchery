package solipingen.progressivearchery.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.QuiverItem;


@Environment(value=EnvType.CLIENT)
public class ModModelPredicateProvider {

    
    public static void registerModModelPredicates() {
        
        //Bows
        ModModelPredicateProvider.registerBow(ModItems.WOODEN_BOW);
        ModModelPredicateProvider.registerBow(ModItems.COPPER_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.IRON_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.GOLD_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.DIAMOND_FUSED_BOW);
        ModModelPredicateProvider.registerBow(ModItems.NETHERITE_FUSED_BOW);
        
        //Horn Bows
        ModModelPredicateProvider.registerHornBow(ModItems.WOODEN_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.COPPER_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.IRON_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.GOLD_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.DIAMOND_FUSED_HORN_BOW);
        ModModelPredicateProvider.registerHornBow(ModItems.NETHERITE_FUSED_HORN_BOW);


        //Tubular Bows
        ModModelPredicateProvider.registerTubularBow(ModItems.WOODEN_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.COPPER_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.IRON_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.GOLD_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.DIAMOND_FUSED_TUBULAR_BOW);
        ModModelPredicateProvider.registerTubularBow(ModItems.NETHERITE_FUSED_TUBULAR_BOW);


        //Longbows
        ModModelPredicateProvider.registerLongbow(ModItems.WOODEN_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.COPPER_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.IRON_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.GOLD_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.DIAMOND_FUSED_LONGBOW);
        ModModelPredicateProvider.registerLongbow(ModItems.NETHERITE_FUSED_LONGBOW);

        //Crossbows
        ModModelPredicateProvider.registerCrossbow(ModItems.WOODEN_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.COPPER_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.IRON_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.GOLD_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.DIAMOND_FUSED_CROSSBOW);
        ModModelPredicateProvider.registerCrossbow(ModItems.NETHERITE_FUSED_CROSSBOW);

        //Quivers
        ModModelPredicateProvider.registerQuiver(ModItems.QUIVER);
    }
    

    private static void registerBow(Item bow) {
        ModelPredicateProviderRegistry.register(bow, new Identifier("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft())/(10.0f - 2.5f*i);
        });

        ModelPredicateProviderRegistry.register(bow, new Identifier("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerHornBow(Item horn_bow) {
        ModelPredicateProviderRegistry.register(horn_bow, new Identifier("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft())/(20.0f - 5.0f*i);
        });

        ModelPredicateProviderRegistry.register(horn_bow, new Identifier("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerLongbow(Item longbow) {
        ModelPredicateProviderRegistry.register(longbow, new Identifier("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft())/(30.0f - 7.5f*i);
        });

        ModelPredicateProviderRegistry.register(longbow, new Identifier("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerTubularBow(Item tubular_bow) {
        ModelPredicateProviderRegistry.register(tubular_bow, new Identifier("pull"),
        (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft())/(40.0f - 10.0f*i);
        });

        ModelPredicateProviderRegistry.register(tubular_bow, new Identifier("pulling"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

    }

    private static void registerCrossbow(Item crossbow) {
        ModelPredicateProviderRegistry.register(crossbow, new Identifier("pull"),
        (stack, world, entity, seed) -> {
            float pulltime = crossbow.getMaxUseTime(stack);
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft())/pulltime;
        });
        
        ModelPredicateProviderRegistry.register(crossbow, new Identifier("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !ModCrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(crossbow, new Identifier("charged"), (stack, world, entity, seed) -> entity != null && ModCrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(crossbow, new Identifier("firework"), (stack, world, entity, seed) -> entity != null && ModCrossbowItem.isCharged(stack) && ModCrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0f : 0.0f);
    }

    private static void registerQuiver(Item quiver) {
        ModelPredicateProviderRegistry.register(quiver, new Identifier("filled"), (stack, world, entity, seed) -> entity != null && QuiverItem.getQuiverOccupancy(stack) > 0 ? 1.0f : 0.0f);
    }

}
