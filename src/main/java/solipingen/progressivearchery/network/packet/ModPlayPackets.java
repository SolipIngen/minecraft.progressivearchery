package solipingen.progressivearchery.network.packet;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.PacketType;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.network.packet.c2s.play.QuiverItemSelectedC2SPacket;


public class ModPlayPackets {

    public static final PacketType<QuiverItemSelectedC2SPacket> QUIVER_ITEM_SELECTED = new PacketType<>(NetworkSide.SERVERBOUND,
            QuiverItemSelectedC2SPacket.QUIVER_ITEM_SELECTED_C2S_PACKET_ID);


    public static void registerModPlayPackets() {
        PayloadTypeRegistry.playC2S().register(QuiverItemSelectedC2SPacket.PAYLOAD_ID, QuiverItemSelectedC2SPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(QuiverItemSelectedC2SPacket.PAYLOAD_ID,
                (packet, context) -> packet.apply(context.player().networkHandler));

        ProgressiveArchery.LOGGER.debug("Registering Mod Play Packets for " + ProgressiveArchery.MOD_ID);
    }

}
