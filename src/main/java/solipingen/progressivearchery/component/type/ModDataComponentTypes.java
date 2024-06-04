package solipingen.progressivearchery.component.type;

import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.component.QuiverContentsComponent;

import java.util.function.UnaryOperator;


public class ModDataComponentTypes {

    public static final DataComponentType<QuiverContentsComponent> QUIVER_CONTENTS = ModDataComponentTypes.register("quiver_contents", (builder) -> {
        return builder.codec(QuiverContentsComponent.CODEC).packetCodec(QuiverContentsComponent.PACKET_CODEC).cache();
    });


    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(ProgressiveArchery.MOD_ID, id), ((DataComponentType.Builder)builderOperator.apply(DataComponentType.builder())).build());
    }

    public static void registerModDataComponentTypes () {
        ProgressiveArchery.LOGGER.debug("Registering mod data component types.");
    }


}
