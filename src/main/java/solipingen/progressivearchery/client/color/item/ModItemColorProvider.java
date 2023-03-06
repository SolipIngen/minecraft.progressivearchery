package solipingen.progressivearchery.client.color.item;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.potion.PotionUtil;
import solipingen.progressivearchery.item.ModItems;

public class ModItemColorProvider {
    public static void registerModItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? PotionUtil.getColor(stack) : -1, ModItems.TIPPED_ARROW);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? PotionUtil.getColor(stack) : -1, ModItems.TIPPED_KID_ARROW);
    }
}
