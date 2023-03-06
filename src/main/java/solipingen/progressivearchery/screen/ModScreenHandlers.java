package solipingen.progressivearchery.screen;

import net.minecraft.screen.ScreenHandlerType;
import solipingen.progressivearchery.screen.fletching.FletchingScreenHandler;


public class ModScreenHandlers {
    public static ScreenHandlerType<FletchingScreenHandler> FLETCHING_SCREEN_HANDLER;

    
    public static void registerScreenHandlers() {
        FLETCHING_SCREEN_HANDLER = new ScreenHandlerType<>(FletchingScreenHandler::new);
    }
    
}
