package solipingen.progressivearchery.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.LeadItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.item.arrows.SpectralArrowItem;
import solipingen.progressivearchery.item.arrows.SpectralKidArrowItem;
import solipingen.progressivearchery.item.arrows.TippedArrowItem;
import solipingen.progressivearchery.item.arrows.TippedKidArrowItem;


public class ModItems {

// Arrows
    public static final Item WOODEN_ARROW = registerItem("wooden_arrow",
        new ModArrowItem(new FabricItemSettings()));

    public static final Item FLINT_ARROW = registerItem("flint_arrow",
        new ModArrowItem(new FabricItemSettings()));

    public static final Item COPPER_ARROW = registerItem("copper_arrow",
        new ModArrowItem(new FabricItemSettings()));

    public static final Item GOLDEN_ARROW = registerItem("golden_arrow",
        new ModArrowItem(new FabricItemSettings()));

    public static final Item IRON_ARROW = registerItem("iron_arrow",
        new ModArrowItem(new FabricItemSettings()));

    public static final Item TIPPED_ARROW = registerItem("tipped_arrow",
        new TippedArrowItem(new FabricItemSettings()));

    public static final Item SPECTRAL_ARROW = registerItem("spectral_arrow",
        new SpectralArrowItem(new FabricItemSettings()));

    public static final Item DIAMOND_ARROW = registerItem("diamond_arrow", 
        new ModArrowItem(new FabricItemSettings()));


// Kid Arrows
    public static final Item WOODEN_KID_ARROW = registerItem("wooden_kid_arrow",
        new KidArrowItem(new FabricItemSettings()));

    public static final Item FLINT_KID_ARROW = registerItem("flint_kid_arrow",
        new KidArrowItem(new FabricItemSettings()));

    public static final Item COPPER_KID_ARROW = registerItem("copper_kid_arrow",
        new KidArrowItem(new FabricItemSettings()));

    public static final Item GOLDEN_KID_ARROW = registerItem("golden_kid_arrow",
        new KidArrowItem(new FabricItemSettings()));

    public static final Item IRON_KID_ARROW = registerItem("iron_kid_arrow",
        new KidArrowItem(new FabricItemSettings()));

    public static final Item TIPPED_KID_ARROW = registerItem("tipped_kid_arrow",
        new TippedKidArrowItem(new FabricItemSettings()));

    public static final Item SPECTRAL_KID_ARROW = registerItem("spectral_kid_arrow",
        new SpectralKidArrowItem(new FabricItemSettings()));
    
    public static final Item DIAMOND_KID_ARROW = registerItem("diamond_kid_arrow",
        new KidArrowItem(new FabricItemSettings()));


// Regular Bows
    public static final Item WOODEN_BOW = registerItem("wooden_bow", 
        new ModBowItem(BowMaterials.WOOD, 0, new FabricItemSettings()));

    public static final Item COPPER_FUSED_BOW = registerItem("copper_fused_bow", 
        new ModBowItem(BowMaterials.COPPER, 0, new FabricItemSettings()));

    public static final Item IRON_FUSED_BOW = registerItem("iron_fused_bow", 
        new ModBowItem(BowMaterials.IRON, 0, new FabricItemSettings()));
    
    public static final Item GOLD_FUSED_BOW = registerItem("gold_fused_bow", 
        new ModBowItem(BowMaterials.GOLD, 0, new FabricItemSettings()));

    public static final Item DIAMOND_FUSED_BOW = registerItem("diamond_fused_bow", 
        new ModBowItem(BowMaterials.DIAMOND, 0, new FabricItemSettings()));

    public static final Item NETHERITE_FUSED_BOW = registerItem("netherite_fused_bow", 
        new ModBowItem(BowMaterials.NETHERITE, 0, new FabricItemSettings().fireproof()));


// Horn Bows
    public static final Item WOODEN_HORN_BOW = registerItem("wooden_horn_bow", 
        new ModBowItem(BowMaterials.WOOD, 1, new FabricItemSettings()));

    public static final Item COPPER_FUSED_HORN_BOW = registerItem("copper_fused_horn_bow", 
        new ModBowItem(BowMaterials.COPPER, 1, new FabricItemSettings()));

    public static final Item IRON_FUSED_HORN_BOW = registerItem("iron_fused_horn_bow", 
        new ModBowItem(BowMaterials.IRON, 1, new FabricItemSettings()));

    public static final Item GOLD_FUSED_HORN_BOW = registerItem("gold_fused_horn_bow", 
        new ModBowItem(BowMaterials.GOLD, 1, new FabricItemSettings()));

    public static final Item DIAMOND_FUSED_HORN_BOW = registerItem("diamond_fused_horn_bow", 
        new ModBowItem(BowMaterials.DIAMOND, 1, new FabricItemSettings()));

    public static final Item NETHERITE_FUSED_HORN_BOW = registerItem("netherite_fused_horn_bow", 
        new ModBowItem(BowMaterials.NETHERITE, 1, new FabricItemSettings().fireproof()));


// Longbows
    public static final Item WOODEN_LONGBOW = registerItem("wooden_longbow", 
        new ModBowItem(BowMaterials.WOOD, 2, new FabricItemSettings()));

    public static final Item COPPER_FUSED_LONGBOW = registerItem("copper_fused_longbow", 
        new ModBowItem(BowMaterials.COPPER, 2, new FabricItemSettings()));

    public static final Item GOLD_FUSED_LONGBOW = registerItem("gold_fused_longbow", 
        new ModBowItem(BowMaterials.GOLD, 2, new FabricItemSettings()));

    public static final Item IRON_FUSED_LONGBOW = registerItem("iron_fused_longbow", 
        new ModBowItem(BowMaterials.IRON, 2, new FabricItemSettings()));

    public static final Item DIAMOND_FUSED_LONGBOW = registerItem("diamond_fused_longbow", 
        new ModBowItem(BowMaterials.DIAMOND, 2, new FabricItemSettings()));

    public static final Item NETHERITE_FUSED_LONGBOW = registerItem("netherite_fused_longbow", 
        new ModBowItem(BowMaterials.NETHERITE, 2, new FabricItemSettings().fireproof()));
    

// Tubular Bows
    public static final Item WOODEN_TUBULAR_BOW = registerItem("wooden_tubular_bow", 
        new ModBowItem(BowMaterials.WOOD, 3, new FabricItemSettings()));

    public static final Item COPPER_FUSED_TUBULAR_BOW = registerItem("copper_fused_tubular_bow", 
        new ModBowItem(BowMaterials.COPPER, 3, new FabricItemSettings()));

    public static final Item IRON_FUSED_TUBULAR_BOW = registerItem("iron_fused_tubular_bow", 
        new ModBowItem(BowMaterials.IRON, 3, new FabricItemSettings()));

    public static final Item GOLD_FUSED_TUBULAR_BOW = registerItem("gold_fused_tubular_bow", 
        new ModBowItem(BowMaterials.GOLD, 3, new FabricItemSettings()));

    public static final Item DIAMOND_FUSED_TUBULAR_BOW = registerItem("diamond_fused_tubular_bow", 
        new ModBowItem(BowMaterials.DIAMOND, 3, new FabricItemSettings()));

    public static final Item NETHERITE_FUSED_TUBULAR_BOW = registerItem("netherite_fused_tubular_bow", 
        new ModBowItem(BowMaterials.NETHERITE, 3, new FabricItemSettings().fireproof()));


// Crossbows
    public static final Item WOODEN_CROSSBOW = registerItem("wooden_crossbow", 
        new ModCrossbowItem(BowMaterials.WOOD, new FabricItemSettings()));

    public static final Item COPPER_FUSED_CROSSBOW = registerItem("copper_fused_crossbow", 
        new ModCrossbowItem(BowMaterials.COPPER, new FabricItemSettings()));

    public static final Item GOLD_FUSED_CROSSBOW = registerItem("gold_fused_crossbow", 
        new ModCrossbowItem(BowMaterials.GOLD, new FabricItemSettings()));

    public static final Item IRON_FUSED_CROSSBOW = registerItem("iron_fused_crossbow", 
        new ModCrossbowItem(BowMaterials.IRON, new FabricItemSettings()));

    public static final Item DIAMOND_FUSED_CROSSBOW = registerItem("diamond_fused_crossbow", 
        new ModCrossbowItem(BowMaterials.DIAMOND, new FabricItemSettings()));

    public static final Item NETHERITE_FUSED_CROSSBOW = registerItem("netherite_fused_crossbow", 
        new ModCrossbowItem(BowMaterials.NETHERITE, new FabricItemSettings().fireproof()));


// Quiver
    public static final Item QUIVER = registerItem("quiver", 
        new QuiverItem(new FabricItemSettings().maxCount(1)));

// Copper Nugget
    public static final Item COPPER_NUGGET = registerItem("copper_nugget",
        new Item(new FabricItemSettings()));


// Horse, Strider and Hoglin Hairs
    public static final Item HORSEHAIR = registerItem("horsehair",
        new Item(new FabricItemSettings()));

    public static final Item STRIDERHAIR = registerItem("striderhair", 
        new Item(new FabricItemSettings().fireproof()));

    public static final Item HOGLINHAIR = registerItem("hoglinhair",
        new Item(new FabricItemSettings()));

// Fireproof Lead
    public static final Item FIREPROOF_LEAD = registerItem("fireproof_lead",
        new LeadItem(new FabricItemSettings().fireproof()));


// Registering Methods
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ProgressiveArchery.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Items for " + ProgressiveArchery.MOD_ID);
    }

}
