package solipingen.progressivearchery.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;


public class ModBlocks {

    // Potion Cauldron
    public static final Block POTION_CAULDRON = ModBlocks.registerBlock("potion_cauldron",
            new PotionCauldronBlock(Biome.Precipitation.NONE, PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR, AbstractBlock.Settings.copy(Blocks.CAULDRON).luminance(PotionCauldronBlock.LEVEL_TO_LUMINANCE)), false);

    // Archer Villager Marker
    public static final Block ARCHER_MARKER = ModBlocks.registerBlock("archer_marker",
            new VillagerFighterMarkerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE_SLAB)), true);

    // Fletcher
    public static final Block FLETCHER = ModBlocks.registerBlock("fletcher",
            new FletcherBlock(AbstractBlock.Settings.copy(Blocks.CRAFTER)), true);


    private static Block registerBlock(String name, Block block, boolean withBlockItem) {
        if (withBlockItem) {
            ModBlocks.registerBlockItem(name, block);
        }
        return Registry.register(Registries.BLOCK, Identifier.of(ProgressiveArchery.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Blocks for " + ProgressiveArchery.MOD_ID);
    }

    
}
