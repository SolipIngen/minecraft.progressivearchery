package solipingen.progressivearchery.client.color.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;


@Environment(value = EnvType.CLIENT)
public class ModBlockColorProvider {


    public static void registerModBlockColors() {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            RegistryEntry<Potion> potionEntry = Registries.POTION.getEntry(PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(state.get(PotionCauldronBlock.POTION_TYPE)));
            return tintIndex == 0 ? PotionContentsComponent.getColor(potionEntry) : -1;
        }, ModBlocks.POTION_CAULDRON);
    }

}
