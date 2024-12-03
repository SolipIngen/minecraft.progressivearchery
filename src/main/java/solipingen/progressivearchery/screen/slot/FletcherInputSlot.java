package solipingen.progressivearchery.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import solipingen.progressivearchery.screen.fletching.FletcherScreenHandler;


public class FletcherInputSlot extends Slot {
    private final FletcherScreenHandler fletcherScreenHandler;


    public FletcherInputSlot(Inventory inventory, int index, int x, int y, FletcherScreenHandler fletcherScreenHandler) {
        super(inventory, index, x, y);
        this.fletcherScreenHandler = fletcherScreenHandler;
    }

//    @Override
//    public boolean canInsert(ItemStack stack) {
//        return !this.fletcherScreenHandler.isSlotDisabled(this.id) && super.canInsert(stack);
//    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.fletcherScreenHandler.onContentChanged(this.inventory);
    }


}
