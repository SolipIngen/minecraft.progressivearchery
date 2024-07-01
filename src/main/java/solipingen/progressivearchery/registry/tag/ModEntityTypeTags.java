package solipingen.progressivearchery.registry.tag;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ModEntityTypeTags {

    public static final TagKey<EntityType<?>> BIRDS = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ProgressiveArchery.MOD_ID, "birds"));

    public static final TagKey<EntityType<?>> ARCHER_VILLAGER_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ProgressiveArchery.MOD_ID, "archer_villager_targets"));

    
}
