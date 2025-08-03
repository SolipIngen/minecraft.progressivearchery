package solipingen.progressivearchery.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.ModBlocks;


public class ModBlockEntityTypes {

    public static final BlockEntityType<FletcherBlockEntity> FLETCHER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(ProgressiveArchery.MOD_ID, "fletcher"),
            FabricBlockEntityTypeBuilder.create(FletcherBlockEntity::new, ModBlocks.FLETCHER).build());


    public static void registerModBlockEntities() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Block Entity Types for " + ProgressiveArchery.MOD_ID);
    }

}
