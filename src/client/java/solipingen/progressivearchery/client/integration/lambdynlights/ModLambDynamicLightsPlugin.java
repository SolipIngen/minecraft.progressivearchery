package solipingen.progressivearchery.client.integration.lambdynlights;

import dev.lambdaurora.lambdynlights.api.DynamicLightsContext;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public class ModLambDynamicLightsPlugin implements DynamicLightsInitializer {


    @Override
    public void onInitializeDynamicLights(DynamicLightsContext dynamicLightsContext) {
        
//        DynamicLightHandlers.registerDynamicLightHandler(ModEntityTypes.SPECTRAL_ARROW_ENTITY, (entity) -> {
//            return 12;
//        });
//        DynamicLightHandlers.registerDynamicLightHandler(ModEntityTypes.SPECTRAL_KID_ARROW_ENTITY, (entity) -> {
//            return 10;
//        });

    }
    

}
