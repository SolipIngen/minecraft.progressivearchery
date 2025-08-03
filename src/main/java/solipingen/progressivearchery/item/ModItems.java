package solipingen.progressivearchery.item;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.LeadItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.item.arrows.SpectralKidArrowItem;
import solipingen.progressivearchery.item.arrows.TippedKidArrowItem;

import java.util.function.Function;


public class ModItems {

    // Arrows
    public static final Item WOODEN_ARROW = ModItems.registerItem("wooden_arrow",
            ModArrowItem::new, new Item.Settings());

    public static final Item FLINT_ARROW = ModItems.registerItem("flint_arrow",
            ModArrowItem::new, new Item.Settings());

    public static final Item COPPER_ARROW = ModItems.registerItem("copper_arrow",
            ModArrowItem::new, new Item.Settings());

    public static final Item GOLDEN_ARROW = ModItems.registerItem("golden_arrow",
            ModArrowItem::new, new Item.Settings());

    public static final Item IRON_ARROW = ModItems.registerItem("iron_arrow",
            ModArrowItem::new, new Item.Settings());

    public static final Item DIAMOND_ARROW = ModItems.registerItem("diamond_arrow",
            ModArrowItem::new, new Item.Settings());


    // Kid Arrows
    public static final Item WOODEN_KID_ARROW = ModItems.registerItem("wooden_kid_arrow",
            KidArrowItem::new, new Item.Settings());

    public static final Item FLINT_KID_ARROW = ModItems.registerItem("flint_kid_arrow",
            KidArrowItem::new, new Item.Settings());

    public static final Item COPPER_KID_ARROW = ModItems.registerItem("copper_kid_arrow",
            KidArrowItem::new, new Item.Settings());

    public static final Item GOLDEN_KID_ARROW = ModItems.registerItem("golden_kid_arrow",
            KidArrowItem::new, new Item.Settings());

    public static final Item IRON_KID_ARROW = ModItems.registerItem("iron_kid_arrow",
            KidArrowItem::new, new Item.Settings());

    public static final Item TIPPED_KID_ARROW = ModItems.registerItem("tipped_kid_arrow",
        TippedKidArrowItem::new, new Item.Settings()
                    .component(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)
                    .component(DataComponentTypes.POTION_DURATION_SCALE, 0.125f));

    public static final Item SPECTRAL_KID_ARROW = ModItems.registerItem("spectral_kid_arrow",
        SpectralKidArrowItem::new, new Item.Settings());

    public static final Item DIAMOND_KID_ARROW = ModItems.registerItem("diamond_kid_arrow",
         KidArrowItem::new, new Item.Settings());


    // Bows
    public static final Item COPPER_FUSED_BOW = ModItems.registerItem("copper_fused_bow",
            settings -> new ModBowItem(BowMaterial.COPPER, 0, settings), new Item.Settings());

    public static final Item IRON_FUSED_BOW = ModItems.registerItem("iron_fused_bow",
            settings -> new ModBowItem(BowMaterial.IRON, 0, settings), new Item.Settings());

    public static final Item GOLD_FUSED_BOW = ModItems.registerItem("gold_fused_bow",
            settings -> new ModBowItem(BowMaterial.GOLD, 0, settings), new Item.Settings());

    public static final Item DIAMOND_FUSED_BOW = ModItems.registerItem("diamond_fused_bow",
            settings -> new ModBowItem(BowMaterial.DIAMOND, 0, settings), new Item.Settings());

    public static final Item NETHERITE_FUSED_BOW = ModItems.registerItem("netherite_fused_bow",
            settings -> new ModBowItem(BowMaterial.NETHERITE, 0, settings), new Item.Settings().fireproof());


    // Horn Bows
    public static final Item HORN_BOW = ModItems.registerItem("horn_bow",
            settings -> new ModBowItem(BowMaterial.WOOD, 1, settings), new Item.Settings());

    public static final Item COPPER_FUSED_HORN_BOW = ModItems.registerItem("copper_fused_horn_bow",
            settings -> new ModBowItem(BowMaterial.COPPER, 1, settings), new Item.Settings());

    public static final Item IRON_FUSED_HORN_BOW = ModItems.registerItem("iron_fused_horn_bow",
            settings -> new ModBowItem(BowMaterial.IRON, 1, settings), new Item.Settings());

    public static final Item GOLD_FUSED_HORN_BOW = ModItems.registerItem("gold_fused_horn_bow",
            settings -> new ModBowItem(BowMaterial.GOLD, 1, settings), new Item.Settings());

    public static final Item DIAMOND_FUSED_HORN_BOW = ModItems.registerItem("diamond_fused_horn_bow",
            settings -> new ModBowItem(BowMaterial.DIAMOND, 1, settings), new Item.Settings());

    public static final Item NETHERITE_FUSED_HORN_BOW = ModItems.registerItem("netherite_fused_horn_bow",
            settings -> new ModBowItem(BowMaterial.NETHERITE, 1, settings), new Item.Settings().fireproof());


    // Longbows
    public static final Item LONGBOW = ModItems.registerItem("longbow",
            settings -> new ModBowItem(BowMaterial.WOOD, 2, settings), new Item.Settings());

    public static final Item COPPER_FUSED_LONGBOW = ModItems.registerItem("copper_fused_longbow",
            settings -> new ModBowItem(BowMaterial.COPPER, 2, settings), new Item.Settings());

    public static final Item IRON_FUSED_LONGBOW = ModItems.registerItem("iron_fused_longbow",
            settings -> new ModBowItem(BowMaterial.IRON, 2, settings), new Item.Settings());

    public static final Item GOLD_FUSED_LONGBOW = ModItems.registerItem("gold_fused_longbow",
            settings -> new ModBowItem(BowMaterial.GOLD, 2, settings), new Item.Settings());

    public static final Item DIAMOND_FUSED_LONGBOW = ModItems.registerItem("diamond_fused_longbow",
            settings -> new ModBowItem(BowMaterial.DIAMOND, 2, settings), new Item.Settings());

    public static final Item NETHERITE_FUSED_LONGBOW = ModItems.registerItem("netherite_fused_longbow",
            settings -> new ModBowItem(BowMaterial.NETHERITE, 2, settings), new Item.Settings().fireproof());


    // Tubular Bows
    public static final Item TUBULAR_BOW = ModItems.registerItem("tubular_bow",
            settings -> new ModBowItem(BowMaterial.WOOD, 3, settings), new Item.Settings());

    public static final Item COPPER_FUSED_TUBULAR_BOW = ModItems.registerItem("copper_fused_tubular_bow",
            settings -> new ModBowItem(BowMaterial.COPPER, 3, settings), new Item.Settings());

    public static final Item IRON_FUSED_TUBULAR_BOW = ModItems.registerItem("iron_fused_tubular_bow",
            settings -> new ModBowItem(BowMaterial.IRON, 3, settings), new Item.Settings());

    public static final Item GOLD_FUSED_TUBULAR_BOW = ModItems.registerItem("gold_fused_tubular_bow",
            settings -> new ModBowItem(BowMaterial.GOLD, 3, settings), new Item.Settings());

    public static final Item DIAMOND_FUSED_TUBULAR_BOW = ModItems.registerItem("diamond_fused_tubular_bow",
            settings -> new ModBowItem(BowMaterial.DIAMOND, 3, settings), new Item.Settings());

    public static final Item NETHERITE_FUSED_TUBULAR_BOW = ModItems.registerItem("netherite_fused_tubular_bow",
            settings -> new ModBowItem(BowMaterial.NETHERITE, 3, settings), new Item.Settings().fireproof());


    // Crossbows
    public static final Item COPPER_FUSED_CROSSBOW = ModItems.registerItem("copper_fused_crossbow",
            settings -> new ModCrossbowItem(BowMaterial.COPPER, settings), new Item.Settings());

    public static final Item GOLD_FUSED_CROSSBOW = ModItems.registerItem("gold_fused_crossbow",
            settings -> new ModCrossbowItem(BowMaterial.GOLD, settings), new Item.Settings());

    public static final Item IRON_FUSED_CROSSBOW = ModItems.registerItem("iron_fused_crossbow",
            settings -> new ModCrossbowItem(BowMaterial.IRON, settings), new Item.Settings());

    public static final Item DIAMOND_FUSED_CROSSBOW = ModItems.registerItem("diamond_fused_crossbow",
            settings -> new ModCrossbowItem(BowMaterial.DIAMOND, settings), new Item.Settings());

    public static final Item NETHERITE_FUSED_CROSSBOW = ModItems.registerItem("netherite_fused_crossbow",
            settings -> new ModCrossbowItem(BowMaterial.NETHERITE, settings), new Item.Settings().fireproof());


    // Quiver
    public static final Item QUIVER = ModItems.registerItem("quiver",
        QuiverItem::new, new Item.Settings().equippable(EquipmentSlot.CHEST).maxCount(1));


    // Bow Fusion Template
    public static final Item BOW_FUSION_SMITHING_TEMPLATE = ModItems.registerItem("bow_fusion_smithing_template",
        BowFusionTemplateItem::createBowFusionTemplate, new Item.Settings());


    // Metal Arrowheads
    public static final Item COPPER_ARROWHEAD = ModItems.registerItem("copper_arrowhead",
            Item::new, new Item.Settings());

    public static final Item IRON_ARROWHEAD = ModItems.registerItem("iron_arrowhead",
            Item::new, new Item.Settings());

    public static final Item GOLDEN_ARROWHEAD = ModItems.registerItem("golden_arrowhead",
            Item::new, new Item.Settings());

    public static final Item DIAMOND_ARROWHEAD = ModItems.registerItem("diamond_arrowhead",
            Item::new, new Item.Settings());


    // Horse, Strider and Hoglin Hairs
    public static final Item HORSEHAIR = ModItems.registerItem("horsehair",
            Item::new, new Item.Settings());

    public static final Item STRIDERHAIR = ModItems.registerItem("striderhair",
            Item::new, new Item.Settings().fireproof());

    public static final Item HOGLINHAIR = ModItems.registerItem("hoglinhair",
            Item::new, new Item.Settings());


    // Fireproof Lead
    public static final Item FIREPROOF_LEAD = ModItems.registerItem("fireproof_lead",
        LeadItem::new, new Item.Settings().fireproof());


    // Registering Methods
    private static Item registerItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        return Registry.register(Registries.ITEM, itemKey, item);
    }

    public static void registerModItems() {
        FuelRegistryEvents.BUILD.register(((builder, context) -> {
            builder.add(ModItems.HORN_BOW, 300);
            builder.add(ModItems.LONGBOW, 400);
            builder.add(ModItems.TUBULAR_BOW, 350);
            builder.add(ModItems.WOODEN_ARROW, 150);
            builder.add(ModItems.WOODEN_KID_ARROW, 75);
            builder.add(ModItems.QUIVER, 450);
        }));

        ProgressiveArchery.LOGGER.debug("Registering Mod Items for " + ProgressiveArchery.MOD_ID);
    }

}
