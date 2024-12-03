package solipingen.progressivearchery.state.property;

import net.minecraft.registry.Registries;
import net.minecraft.state.property.IntProperty;


public class ModProperties {
    
    public static final IntProperty BLOCK_POTION_TYPE = IntProperty.of("block_potion_type", 1, Math.max(Registries.POTION.size(), 108));
    
}
