package solipingen.progressivearchery.screen.fletching;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;


public abstract class ArrowmakingScreenHandler extends ScreenHandler {
    public static final int FIRST_INPUT_SLOT_INDEX = 0;
    public static final int SECOND_INPUT_SLOT_INDEX = 1;
    public static final int THIRD_INPUT_SLOT_INDEX = 2;
    public static final int FOURTH_INPUT_SLOT_INDEX = 3;
    public static final int OUTPUT_SLOT_INDEX = 4;
    protected final CraftingResultInventory output = new CraftingResultInventory();
    protected final Inventory input = new SimpleInventory(4){

        @Override
        public void markDirty() {
            super.markDirty();
            ArrowmakingScreenHandler.this.onContentChanged(this);
        }

    };
    protected final ScreenHandlerContext context;
    protected final PlayerEntity player;

    protected abstract boolean canTakeOutput(PlayerEntity var1, boolean var2);

    protected abstract void onTakeOutput(PlayerEntity var1, ItemStack var2);

    protected abstract boolean canUse(BlockState var1);

    
    public ArrowmakingScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId);
        this.context = context;
        this.player = playerInventory.player;
    }

    public abstract void updateResult();

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.input) {
            this.updateResult();
        }
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> {
            if (!this.canUse(world.getBlockState((BlockPos)pos))) {
                return false;
            }
            return player.squaredDistanceTo((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5) <= 64.0;
        }, true);
    }

    protected boolean isUsableAsHead(ItemStack stack) {
        return false;
    }

    protected boolean isUsableAsBody(ItemStack stack) {
        return false;
    }

    protected boolean isUsableAsTail(ItemStack stack) {
        return false;
    }

    protected boolean isUsableAsAddition(ItemStack stack) {
        return false;
    }


}

