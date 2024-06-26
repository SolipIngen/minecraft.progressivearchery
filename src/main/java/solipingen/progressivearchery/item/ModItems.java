package solipingen.progressivearchery.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.LeadItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.item.arrows.SpectralKidArrowItem;
import solipingen.progressivearchery.item.arrows.TippedKidArrowItem;


public class ModItems {

    // Arrows
    public static final Item WOODEN_ARROW = ModItems.registerItem("wooden_arrow",
        new ModArrowItem(new Item.Settings()));

    public static final Item FLINT_ARROW = ModItems.registerItem("flint_arrow",
        new ModArrowItem(new Item.Settings()));

    public static final Item COPPER_ARROW = ModItems.registerItem("copper_arrow",
        new ModArrowItem(new Item.Settings()));

    public static final Item GOLDEN_ARROW = ModItems.registerItem("golden_arrow",
        new ModArrowItem(new Item.Settings()));

    public static final Item IRON_ARROW = ModItems.registerItem("iron_arrow",
        new ModArrowItem(new Item.Settings()));

    public static final Item DIAMOND_ARROW = ModItems.registerItem("diamond_arrow",
        new ModArrowItem(new Item.Settings()));


    // Kid Arrows
    public static final Item WOODEN_KID_ARROW = ModItems.registerItem("wooden_kid_arrow",
        new KidArrowItem(new Item.Settings()));

    public static final Item FLINT_KID_ARROW = ModItems.registerItem("flint_kid_arrow",
        new KidArrowItem(new Item.Settings()));

    public static final Item COPPER_KID_ARROW = ModItems.registerItem("copper_kid_arrow",
        new KidArrowItem(new Item.Settings()));

    public static final Item GOLDEN_KID_ARROW = ModItems.registerItem("golden_kid_arrow",
        new KidArrowItem(new Item.Settings()));

    public static final Item IRON_KID_ARROW = ModItems.registerItem("iron_kid_arrow",
        new KidArrowItem(new Item.Settings()));

    public static final Item TIPPED_KID_ARROW = ModItems.registerItem("tipped_kid_arrow",
        new TippedKidArrowItem(new Item.Settings().component(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)));

    public static final Item SPECTRAL_KID_ARROW = ModItems.registerItem("spectral_kid_arrow",
        new SpectralKidArrowItem(new Item.Settings()));

    public static final Item DIAMOND_KID_ARROW = ModItems.registerItem("diamond_kid_arrow",
        new KidArrowItem(new Item.Settings()));


    // Bows
    public static final Item COPPER_FUSED_BOW = ModItems.registerItem("copper_fused_bow",
        new ModBowItem(BowMaterials.COPPER, 0, new Item.Settings()));

    public static final Item IRON_FUSED_BOW = ModItems.registerItem("iron_fused_bow",
        new ModBowItem(BowMaterials.IRON, 0, new Item.Settings()));

    public static final Item GOLD_FUSED_BOW = ModItems.registerItem("gold_fused_bow",
        new ModBowItem(BowMaterials.GOLD, 0, new Item.Settings()));

    public static final Item DIAMOND_FUSED_BOW = ModItems.registerItem("diamond_fused_bow",
        new ModBowItem(BowMaterials.DIAMOND, 0, new Item.Settings()));

    public static final Item NETHERITE_FUSED_BOW = ModItems.registerItem("netherite_fused_bow",
        new ModBowItem(BowMaterials.NETHERITE, 0, new Item.Settings().fireproof()));


    // Horn Bows
    public static final Item HORN_BOW = ModItems.registerItem("horn_bow",
        new ModBowItem(BowMaterials.WOOD, 1, new Item.Settings()));

    public static final Item COPPER_FUSED_HORN_BOW = ModItems.registerItem("copper_fused_horn_bow",
        new ModBowItem(BowMaterials.COPPER, 1, new Item.Settings()));

    public static final Item IRON_FUSED_HORN_BOW = ModItems.registerItem("iron_fused_horn_bow",
        new ModBowItem(BowMaterials.IRON, 1, new Item.Settings()));

    public static final Item GOLD_FUSED_HORN_BOW = ModItems.registerItem("gold_fused_horn_bow",
        new ModBowItem(BowMaterials.GOLD, 1, new Item.Settings()));

    public static final Item DIAMOND_FUSED_HORN_BOW = ModItems.registerItem("diamond_fused_horn_bow",
        new ModBowItem(BowMaterials.DIAMOND, 1, new Item.Settings()));

    public static final Item NETHERITE_FUSED_HORN_BOW = ModItems.registerItem("netherite_fused_horn_bow",
        new ModBowItem(BowMaterials.NETHERITE, 1, new Item.Settings().fireproof()));


    // Longbows
    public static final Item LONGBOW = ModItems.registerItem("longbow",
        new ModBowItem(BowMaterials.WOOD, 2, new Item.Settings()));

    public static final Item COPPER_FUSED_LONGBOW = ModItems.registerItem("copper_fused_longbow",
        new ModBowItem(BowMaterials.COPPER, 2, new Item.Settings()));

    public static final Item GOLD_FUSED_LONGBOW = ModItems.registerItem("gold_fused_longbow",
        new ModBowItem(BowMaterials.GOLD, 2, new Item.Settings()));

    public static final Item IRON_FUSED_LONGBOW = ModItems.registerItem("iron_fused_longbow",
        new ModBowItem(BowMaterials.IRON, 2, new Item.Settings()));

    public static final Item DIAMOND_FUSED_LONGBOW = ModItems.registerItem("diamond_fused_longbow",
        new ModBowItem(BowMaterials.DIAMOND, 2, new Item.Settings()));

    public static final Item NETHERITE_FUSED_LONGBOW = ModItems.registerItem("netherite_fused_longbow",
        new ModBowItem(BowMaterials.NETHERITE, 2, new Item.Settings().fireproof()));


    // Tubular Bows
    public static final Item TUBULAR_BOW = ModItems.registerItem("tubular_bow",
        new ModBowItem(BowMaterials.WOOD, 3, new Item.Settings()));

    public static final Item COPPER_FUSED_TUBULAR_BOW = ModItems.registerItem("copper_fused_tubular_bow",
        new ModBowItem(BowMaterials.COPPER, 3, new Item.Settings()));

    public static final Item IRON_FUSED_TUBULAR_BOW = ModItems.registerItem("iron_fused_tubular_bow",
        new ModBowItem(BowMaterials.IRON, 3, new Item.Settings()));

    public static final Item GOLD_FUSED_TUBULAR_BOW = ModItems.registerItem("gold_fused_tubular_bow",
        new ModBowItem(BowMaterials.GOLD, 3, new Item.Settings()));

    public static final Item DIAMOND_FUSED_TUBULAR_BOW = ModItems.registerItem("diamond_fused_tubular_bow",
        new ModBowItem(BowMaterials.DIAMOND, 3, new Item.Settings()));

    public static final Item NETHERITE_FUSED_TUBULAR_BOW = ModItems.registerItem("netherite_fused_tubular_bow",
        new ModBowItem(BowMaterials.NETHERITE, 3, new Item.Settings().fireproof()));


    // Crossbows
    public static final Item COPPER_FUSED_CROSSBOW = ModItems.registerItem("copper_fused_crossbow",
        new ModCrossbowItem(BowMaterials.COPPER, new Item.Settings()));

    public static final Item GOLD_FUSED_CROSSBOW = ModItems.registerItem("gold_fused_crossbow",
        new ModCrossbowItem(BowMaterials.GOLD, new Item.Settings()));

    public static final Item IRON_FUSED_CROSSBOW = ModItems.registerItem("iron_fused_crossbow",
        new ModCrossbowItem(BowMaterials.IRON, new Item.Settings()));

    public static final Item DIAMOND_FUSED_CROSSBOW = ModItems.registerItem("diamond_fused_crossbow",
        new ModCrossbowItem(BowMaterials.DIAMOND, new Item.Settings()));

    public static final Item NETHERITE_FUSED_CROSSBOW = ModItems.registerItem("netherite_fused_crossbow",
        new ModCrossbowItem(BowMaterials.NETHERITE, new Item.Settings().fireproof()));


    // Quiver
    public static final Item QUIVER = ModItems.registerItem("quiver",
        new QuiverItem(new Item.Settings().maxCount(1)));


    // Bow Fusion Template
    public static final Item BOW_FUSION_SMITHING_TEMPLATE = ModItems.registerItem("bow_fusion_smithing_template",
        (Item)BowFusionTemplateItem.createBowFusionTemplate());


    // Metal Arrowheads
    public static final Item COPPER_ARROWHEAD = ModItems.registerItem("copper_arrowhead",
        new Item(new Item.Settings()));

    public static final Item IRON_ARROWHEAD = ModItems.registerItem("iron_arrowhead",
        new Item(new Item.Settings()));

    public static final Item GOLDEN_ARROWHEAD = ModItems.registerItem("golden_arrowhead",
        new Item(new Item.Settings()));

    public static final Item DIAMOND_ARROWHEAD = ModItems.registerItem("diamond_arrowhead",
        new Item(new Item.Settings()));


    // Horse, Strider and Hoglin Hairs
    public static final Item HORSEHAIR = ModItems.registerItem("horsehair",
        new Item(new Item.Settings()));

    public static final Item STRIDERHAIR = ModItems.registerItem("striderhair",
        new Item(new Item.Settings().fireproof()));

    public static final Item HOGLINHAIR = ModItems.registerItem("hoglinhair",
        new Item(new Item.Settings()));


    // Fireproof Lead
    public static final Item FIREPROOF_LEAD = ModItems.registerItem("fireproof_lead",
        new LeadItem(new Item.Settings().fireproof()));


    // Registering Methods
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, name), item);
    }

    public static void registerModItems() {
        FuelRegistry.INSTANCE.add(ModItems.HORN_BOW, 300);
        FuelRegistry.INSTANCE.add(ModItems.LONGBOW, 400);
        FuelRegistry.INSTANCE.add(ModItems.TUBULAR_BOW, 350);
        FuelRegistry.INSTANCE.add(ModItems.WOODEN_ARROW, 150);
        FuelRegistry.INSTANCE.add(ModItems.WOODEN_KID_ARROW, 75);
        FuelRegistry.INSTANCE.add(ModItems.QUIVER, 450);

        ProgressiveArchery.LOGGER.debug("Registering Mod Items for " + ProgressiveArchery.MOD_ID);
    }

}
