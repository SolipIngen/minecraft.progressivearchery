package solipingen.progressivearchery.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;

public class ModEntityTypeTags {

    public static final TagKey<EntityType<?>> BIRDS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(ProgressiveArchery.MOD_ID, "birds"));

    
}
