package solipingen.progressivearchery.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;


public class ModBlocks {

    // Potion Cauldron
    public static final Block POTION_CAULDRON = registerBlockWithoutItem("potion_cauldron", 
        new PotionCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).luminance(PotionCauldronBlock.LEVEL_TO_LUMINANCE), null, PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR));

    // Archer Villager Marker
    public static final Block ARCHER_MARKER = registerBlock("archer_marker", 
        new VillagerFighterMarkerBlock(FabricBlockSettings.copy(Blocks.POLISHED_DIORITE_SLAB)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(ProgressiveArchery.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(ProgressiveArchery.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(ProgressiveArchery.MOD_ID, name), 
            new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Blocks for " + ProgressiveArchery.MOD_ID);
    }
}
