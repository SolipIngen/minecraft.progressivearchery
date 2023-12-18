package solipingen.progressivearchery.state.property;

import net.minecraft.state.property.EnumProperty;
import solipingen.progressivearchery.block.enums.BlockPotionType;


public class ModProperties {
    
    public static final EnumProperty<BlockPotionType> BLOCK_POTION_TYPE = EnumProperty.of("block_potion_type", BlockPotionType.class);
    
}
