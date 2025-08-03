package solipingen.progressivearchery.network.packet.c2s.play;

import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.network.packet.ModPlayPackets;


public record QuiverItemSelectedC2SPacket(int slotIndex, int selectedItemIndex) implements Packet<ServerPlayPacketListener>, CustomPayload {
    public static final Identifier QUIVER_ITEM_SELECTED_C2S_PACKET_ID = Identifier.of(ProgressiveArchery.MOD_ID, "quiver_item_selected");
    public static final CustomPayload.Id<QuiverItemSelectedC2SPacket> PAYLOAD_ID = new CustomPayload.Id<>(QUIVER_ITEM_SELECTED_C2S_PACKET_ID);
    public static final PacketCodec<PacketByteBuf, QuiverItemSelectedC2SPacket> CODEC = Packet.createCodec(
            QuiverItemSelectedC2SPacket::write, QuiverItemSelectedC2SPacket::new
    );


    private QuiverItemSelectedC2SPacket(PacketByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
        if (this.selectedItemIndex < 0 && this.selectedItemIndex != -1) {
            throw new IllegalArgumentException("Invalid selectedItemIndex: " + this.selectedItemIndex);
        }
    }

    public QuiverItemSelectedC2SPacket(int slotIndex, int selectedItemIndex) {
        this.slotIndex = slotIndex;
        this.selectedItemIndex = selectedItemIndex;
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.slotIndex);
        buf.writeVarInt(this.selectedItemIndex);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        if (serverPlayPacketListener instanceof ServerPlayNetworkHandler serverPlayNetworkHandler) {
            ServerPlayerEntity serverPlayerEntity = serverPlayNetworkHandler.getPlayer();
            NetworkThreadUtils.forceMainThread(this, serverPlayNetworkHandler, serverPlayerEntity.getWorld());
            ItemStack itemStack = serverPlayerEntity.getInventory().getStack(this.slotIndex);
            QuiverItem.setSelectedStackIndex(itemStack, this.selectedItemIndex);
        }
    }

    @Override
    public PacketType<QuiverItemSelectedC2SPacket> getPacketType() {
        return ModPlayPackets.QUIVER_ITEM_SELECTED;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }


}
