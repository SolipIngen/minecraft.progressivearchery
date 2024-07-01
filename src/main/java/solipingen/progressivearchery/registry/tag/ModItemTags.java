package solipingen.progressivearchery.registry.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ModItemTags {
    
    public static TagKey<Item> INFINITY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "enchantable/infinity"));

    public static final TagKey<Item> CUTTABLE_METAL_ARROWS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "cuttable_metal_arrows"));
    public static final TagKey<Item> FLETCHES = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "fletches"));

    public static final TagKey<Item> INFINITY_100P = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "infinity_100p"));
    public static final TagKey<Item> INFINITY_75P = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "infinity_75p"));
    public static final TagKey<Item> INFINITY_50P = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "infinity_50p"));
    public static final TagKey<Item> INFINITY_25P = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "infinity_25p"));
    public static final TagKey<Item> INFINITY_12p5P = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "infinity_12.5p"));

    public static final TagKey<Item> CRAFTED_ARROWHEADS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "crafted_arrowheads"));
    public static final TagKey<Item> METAL_ARROW_CUTTERS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "metal_arrow_cutters"));
    public static final TagKey<Item> METAL_ARROW_WELDING_HEAT_SOURCES = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "metal_arrow_welding_heat_sources"));
    public static final TagKey<Item> UNCONSUMED_FLETCHING_ADDITIONS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "unconsumed_fletching_additions"));

    public static final TagKey<Item> QUIVER_COVERINGS = TagKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, "quiver_coverings"));
    
    
}
