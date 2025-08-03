package solipingen.progressivearchery.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;

import java.util.function.Function;


public class ModBlocks {

    // Potion Cauldron
    public static final Block POTION_CAULDRON = ModBlocks.registerBlock("potion_cauldron",
            settings -> new PotionCauldronBlock(Biome.Precipitation.NONE, PotionCauldronBlock.POTION_CAULDRON_BEHAVIOR, settings),
            AbstractBlock.Settings.copy(Blocks.CAULDRON).luminance(PotionCauldronBlock.LEVEL_TO_LUMINANCE), false);

    // Archer Villager Marker
    public static final Block ARCHER_MARKER = ModBlocks.registerBlock("archer_marker",
            VillagerFighterMarkerBlock::new, AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE_SLAB), true);

    // Fletcher
    public static final Block FLETCHER = ModBlocks.registerBlock("fletcher",
            FletcherBlock::new, AbstractBlock.Settings.copy(Blocks.CRAFTER), true);


    // Registering Methods
    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean withBlockItem) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(ProgressiveArchery.MOD_ID, name));
        Block block = blockFactory.apply(settings.registryKey(blockKey));
        if (withBlockItem) {
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ProgressiveArchery.MOD_ID, name));
            Registry.register(Registries.ITEM, itemKey, new BlockItem(block, new Item.Settings().registryKey(itemKey)));
        }
        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    public static void registerModBlocks() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Blocks for " + ProgressiveArchery.MOD_ID);
    }

    
}
