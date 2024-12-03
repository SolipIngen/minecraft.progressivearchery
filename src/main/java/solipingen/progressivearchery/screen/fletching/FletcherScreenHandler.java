package solipingen.progressivearchery.screen.fletching;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import solipingen.progressivearchery.block.FletcherBlock;
import solipingen.progressivearchery.screen.ModScreenHandlers;
import solipingen.progressivearchery.screen.slot.FletcherInputSlot;
import solipingen.progressivearchery.screen.slot.FletcherOutputSlot;


public class FletcherScreenHandler extends ScreenHandler implements ScreenHandlerListener {
    private final CraftingResultInventory resultInventory = new CraftingResultInventory();
    private final PropertyDelegate propertyDelegate;
    private final PlayerEntity player;
    private final RecipeInputInventory inputInventory;


    public FletcherScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.FLETCHER_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player;
        this.propertyDelegate = new ArrayPropertyDelegate(1);
        this.inputInventory = new CraftingInventory(this, 4, 1);
        this.addSlots(playerInventory);
    }

    public FletcherScreenHandler(int syncId, PlayerInventory playerInventory, RecipeInputInventory inputInventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.FLETCHER_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player;
        this.propertyDelegate = propertyDelegate;
        this.inputInventory = inputInventory;
        FletcherScreenHandler.checkSize(inputInventory, 4);
        inputInventory.onOpen(playerInventory.player);
        this.addSlots(playerInventory);
        this.addListener(this);
    }

    private void addSlots(PlayerInventory playerInventory) {
        this.addSlot(new FletcherInputSlot(this.inputInventory, 0, 69, 23, this));
        this.addSlot(new FletcherInputSlot(this.inputInventory, 1, 44, 23, this));
        this.addSlot(new FletcherInputSlot(this.inputInventory, 2, 19, 23, this));
        this.addSlot(new FletcherInputSlot(this.inputInventory, 3, 44, 53, this));
        for (int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addSlot(new FletcherOutputSlot(this.resultInventory, 4, 129, 37));
        this.addProperties(this.propertyDelegate);
        this.updateResult();
    }

    public boolean isTriggered() {
        return this.propertyDelegate.get(0) == 1;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < 4) {
                if (!this.insertItem(itemStack2, 4, 45, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.insertItem(itemStack2, 0, 4, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setStackNoCallbacks(ItemStack.EMPTY);
            }
            else {
                slot2.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inputInventory.canPlayerUse(player);
    }

    private void updateResult() {
        if (this.player instanceof ServerPlayerEntity serverPlayerEntity) {
            World world = serverPlayerEntity.getWorld();
            CraftingRecipeInput recipeInput = this.inputInventory.createRecipeInput();
            ItemStack itemStack = FletcherBlock.getFletchingRecipe(world, recipeInput).map((recipeEntry) ->
                    recipeEntry.value().craft(recipeInput, world.getRegistryManager())).orElse(ItemStack.EMPTY);
            this.resultInventory.setStack(0, itemStack);
        }
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
        this.updateResult();
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
    }
}
