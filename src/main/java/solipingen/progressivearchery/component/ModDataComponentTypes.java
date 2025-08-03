package solipingen.progressivearchery.component;

import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.component.type.QuiverContentsComponent;

import java.util.function.UnaryOperator;


public class ModDataComponentTypes {

    public static final ComponentType<QuiverContentsComponent> QUIVER_CONTENTS = ModDataComponentTypes.register("quiver_contents",
            builder -> builder.codec(QuiverContentsComponent.CODEC).packetCodec(QuiverContentsComponent.PACKET_CODEC).cache()
    );

    public static final ComponentType<Integer> QUIVER_SELECTED_INDEX = ModDataComponentTypes.register("quiver_selected_index",
            builder -> builder.codec(Codecs.NON_NEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT)
    );


    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ProgressiveArchery.MOD_ID, id), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerModDataComponentTypes () {
        ProgressiveArchery.LOGGER.debug("Registering mod data component types.");
    }


}
