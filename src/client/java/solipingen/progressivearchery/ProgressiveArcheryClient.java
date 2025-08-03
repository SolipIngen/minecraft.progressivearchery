package solipingen.progressivearchery;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import solipingen.progressivearchery.client.color.block.ModBlockColorProvider;
import solipingen.progressivearchery.client.gui.screen.ingame.FletcherScreen;
import solipingen.progressivearchery.client.gui.screen.ingame.FletchingScreen;
import solipingen.progressivearchery.client.render.entity.ModEntityModelLayers;
import solipingen.progressivearchery.client.render.entity.ModEntityRendererRegistry;
import solipingen.progressivearchery.client.render.item.model.ModItemModelTypes;
import solipingen.progressivearchery.client.render.item.property.ModItemRenderProperties;
import solipingen.progressivearchery.screen.ModScreenHandlers;


@Environment(value = EnvType.CLIENT)
public class ProgressiveArcheryClient implements ClientModInitializer {

    
    @Override
    public void onInitializeClient() {

        ModBlockColorProvider.registerModBlockColors();
        ModItemModelTypes.registerModelItemModelTypes();
        ModItemRenderProperties.registerModItemRenderProperties();
        ModEntityModelLayers.registerModEntityLayers();
        ModEntityRendererRegistry.registerModEntityRenderers();
//        ModModelPredicateProvider.registerModItemModelPredicates();

        HandledScreens.register(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);
        HandledScreens.register(ModScreenHandlers.FLETCHER_SCREEN_HANDLER, FletcherScreen::new);

    }
    
}
