package solipingen.progressivearchery.client.gui.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipSubmenuHandler;
import net.minecraft.client.input.Scroller;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.joml.Vector2i;
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.network.packet.c2s.play.QuiverItemSelectedC2SPacket;


@Environment(EnvType.CLIENT)
public class QuiverTooltipSubmenuHandler implements TooltipSubmenuHandler {
    private final MinecraftClient client;
    private final Scroller scroller;


    public QuiverTooltipSubmenuHandler(MinecraftClient client) {
        this.client = client;
        this.scroller = new Scroller();
    }

    @Override
    public boolean isApplicableTo(Slot slot) {
        return slot.getStack().getItem() instanceof QuiverItem;
    }

    @Override
    public boolean onScroll(double horizontal, double vertical, int slotId, ItemStack item) {
        int i = QuiverItem.getNumberOfStacksShown(item);
        if (i == 0) {
            return false;
        }
        else {
            if (this.client.player != null) {
                int inventoryIndex = QuiverTooltipSubmenuHandler.getMatchingItemSlot(this.client.player, item);
                Vector2i vector2i = this.scroller.update(horizontal, vertical);
                int j = vector2i.y == 0 ? -vector2i.x : vector2i.y;
                if (j != 0) {
                    int k = QuiverItem.getSelectedStackIndex(item);
                    int l = Scroller.scrollCycling(j, k, i);
                    if (k != l) {
                        this.sendPacket(item, inventoryIndex, l);
                    }
                }
            }
            return true;
        }
    }

    private void sendPacket(ItemStack item, int slotIndex, int selectedItemIndex) {
        if (ClientPlayNetworking.canSend(QuiverItemSelectedC2SPacket.PAYLOAD_ID) && selectedItemIndex < QuiverItem.getNumberOfStacksShown(item)) {
            QuiverItem.setSelectedStackIndex(item, selectedItemIndex);
            ClientPlayNetworking.send(new QuiverItemSelectedC2SPacket(slotIndex, selectedItemIndex));
        }
    }

    @Override
    public void reset(Slot slot) {
        this.reset(slot.getStack(), slot.id, false);
    }

    public void reset(ItemStack item, int slotId, boolean resetSelectedIndex) {
        this.sendPacket(item, slotId, resetSelectedIndex ? -1 : QuiverItem.getSelectedStackIndex(item));
    }

    @Override
    public void onMouseClick(Slot slot, SlotActionType actionType) {
        if (actionType == SlotActionType.QUICK_MOVE || actionType == SlotActionType.SWAP) {
            this.reset(slot.getStack(), slot.id, false);
        }
    }

    private static int getMatchingItemSlot(PlayerEntity playerEntity, ItemStack stack) {
        for (int i = 0; i < playerEntity.getInventory().size(); i++) {
            if (ItemStack.areEqual(stack, playerEntity.getInventory().getStack(i))) {
                return i;
            }
        }
        return -1;
    }



}
