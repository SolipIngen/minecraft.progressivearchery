package solipingen.progressivearchery.client.color.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.potion.PotionUtil;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;


@Environment(value = EnvType.CLIENT)
public class ModBlockColorProvider {


    public static void registerModBlockColors() {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> tintIndex == 0 ? PotionUtil.getColor(PotionCauldronBlock.POTION_TYPE_IN_BLOCK.get(state.get(PotionCauldronBlock.POTION_TYPE))) : -1, ModBlocks.POTION_CAULDRON);
    }

}
