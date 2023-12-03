package solipingen.progressivearchery.registry.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ModItemTags {

    public static final TagKey<Item> FLETCHES = TagKey.of(RegistryKeys.ITEM, new Identifier(ProgressiveArchery.MOD_ID, "fletches"));
    public static final TagKey<Item> METAL_ARROW_CUTTERS = TagKey.of(RegistryKeys.ITEM, new Identifier(ProgressiveArchery.MOD_ID, "metal_arrow_cutters"));
    public static final TagKey<Item> METAL_ARROW_WELDING_HEAT_SOURCES = TagKey.of(RegistryKeys.ITEM, new Identifier(ProgressiveArchery.MOD_ID, "metal_arrow_welding_heat_sources"));
    public static final TagKey<Item> QUIVER_COVERINGS = TagKey.of(RegistryKeys.ITEM, new Identifier(ProgressiveArchery.MOD_ID, "quiver_coverings"));
    
    
}
