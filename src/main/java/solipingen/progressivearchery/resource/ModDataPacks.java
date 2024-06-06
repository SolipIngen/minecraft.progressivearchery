package solipingen.progressivearchery.resource;


import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ModDataPacks {

    public static void registerModDataPacks() {

        FabricLoader.getInstance().getModContainer(ProgressiveArchery.MOD_ID).ifPresent((container) -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(ProgressiveArchery.MOD_ID, "progressivearchery"), container,
                    ResourcePackActivationType.DEFAULT_ENABLED);
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(ProgressiveArchery.MOD_ID, "solipingen_mods"), container,
                    ResourcePackActivationType.NORMAL);
        });

    }

}
