package solipingen.progressivearchery.client.color.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import solipingen.progressivearchery.item.ModItems;


@Environment(value = EnvType.CLIENT)
public class ModItemColorProvider {


    public static void registerModItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                tintIndex == 0 ? stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getColor() : -1, ModItems.TIPPED_KID_ARROW);
    }


}
