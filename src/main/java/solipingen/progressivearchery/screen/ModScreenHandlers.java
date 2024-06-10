package solipingen.progressivearchery.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType.Factory;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.screen.fletching.FletcherScreenHandler;
import solipingen.progressivearchery.screen.fletching.FletchingScreenHandler;


public class ModScreenHandlers {

    public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING_SCREEN_HANDLER = ModScreenHandlers.register(new Identifier(ProgressiveArchery.MOD_ID, "fletching"), FletchingScreenHandler::new);
    public static final ScreenHandlerType<FletcherScreenHandler> FLETCHER_SCREEN_HANDLER = ModScreenHandlers.register(new Identifier(ProgressiveArchery.MOD_ID, "fletcher"), FletcherScreenHandler::new);


    private static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier id, Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<T>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static void registerScreenHandlers() {
        ProgressiveArchery.LOGGER.debug("Registering Screen Handlers for " + ProgressiveArchery.MOD_ID);
    }
    
}
