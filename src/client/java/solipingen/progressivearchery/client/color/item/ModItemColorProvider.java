package solipingen.progressivearchery.client.color.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.potion.PotionUtil;
import solipingen.progressivearchery.item.ModItems;


@Environment(value = EnvType.CLIENT)
public class ModItemColorProvider {


    public static void registerModItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? PotionUtil.getColor(stack) : -1, ModItems.TIPPED_KID_ARROW);
    }


}
