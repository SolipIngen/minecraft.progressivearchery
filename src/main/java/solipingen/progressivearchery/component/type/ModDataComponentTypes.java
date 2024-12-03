package solipingen.progressivearchery.component.type;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.component.QuiverContentsComponent;

import java.util.function.UnaryOperator;


public class ModDataComponentTypes {

    public static final ComponentType<QuiverContentsComponent> QUIVER_CONTENTS = ModDataComponentTypes.register("quiver_contents",
            (builder) -> builder.codec(QuiverContentsComponent.CODEC).packetCodec(QuiverContentsComponent.PACKET_CODEC).cache());


    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ProgressiveArchery.MOD_ID, id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }

    public static void registerModDataComponentTypes () {
        ProgressiveArchery.LOGGER.debug("Registering mod data component types.");
    }


}
