package solipingen.progressivearchery.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.FletcherBlock;
import solipingen.progressivearchery.screen.fletching.FletcherScreenHandler;


public class FletcherBlockEntity extends LootableContainerBlockEntity implements RecipeInputInventory {
    private DefaultedList<ItemStack> inputStacks;
    private int craftingTicksRemaining;
    protected final PropertyDelegate propertyDelegate;


    public FletcherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.FLETCHER_BLOCK_ENTITY, pos, state);
        this.inputStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.craftingTicksRemaining = 0;
        this.propertyDelegate = new PropertyDelegate(){
            private int triggered = 0;

            @Override
            public int get(int index) {
                return this.triggered;
            }

            @Override
            public void set(int index, int value) {
                this.triggered = value;
            }

            @Override
            public int size() {
                return 1;
            }
        };
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable( "container." + ProgressiveArchery.MOD_ID + ".fletcher");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FletcherScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        ItemStack itemStack = this.inputStacks.get(slot);
        int i = itemStack.getCount();
        if (i >= itemStack.getMaxCount()) {
            return false;
        }
        else if (itemStack.isEmpty()) {
            return true;
        }
        else {
            return !this.betterSlotExists(i, itemStack, slot);
        }
    }

    private boolean betterSlotExists(int count, ItemStack stack, int slot) {
        for (int i = slot + 1; i < 4; ++i) {
            ItemStack itemStack = this.getStack(i);
            if (itemStack.isEmpty() || itemStack.getCount() < count && ItemStack.areItemsAndComponentsEqual(itemStack, stack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        this.craftingTicksRemaining = view.getInt("crafting_ticks_remaining", 0);
        this.inputStacks = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(view)) {
            Inventories.readData(view, this.inputStacks);
        }
        this.propertyDelegate.set(0, view.getInt("triggered", 0));
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("crafting_ticks_remaining", this.craftingTicksRemaining);
        if (!this.writeLootTable(view)) {
            Inventories.writeData(view, this.inputStacks);
        }
        this.putTriggered(view);
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        Iterator var1 = (Iterator)this.inputStacks.iterator();
        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }
            itemStack = var1.next();
        }
        while(itemStack.isEmpty());
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inputStacks.get(slot);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public DefaultedList<ItemStack> getHeldStacks() {
        return this.inputStacks;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inputStacks = inventory;
    }

    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public void provideRecipeInputs(RecipeFinder finder) {
        for (ItemStack itemStack : this.inputStacks) {
            finder.addInputIfUsable(itemStack);
        }
    }

    private void putTriggered(WriteView view) {
        view.putInt("triggered", this.propertyDelegate.get(0));
    }

    public void setTriggered(boolean triggered) {
        this.propertyDelegate.set(0, triggered ? 1 : 0);
    }

    public static void tickCrafting(World world, BlockPos pos, BlockState state, FletcherBlockEntity blockEntity) {
        int i = blockEntity.craftingTicksRemaining - 1;
        if (i >= 0) {
            blockEntity.craftingTicksRemaining = i;
            if (i == 0) {
                world.setBlockState(pos, state.with(FletcherBlock.CRAFTING, false), 3);
            }

        }
    }

    public void setCraftingTicksRemaining(int craftingTicksRemaining) {
        this.craftingTicksRemaining = craftingTicksRemaining;
    }

    public int getComparatorOutput() {
        int i = 0;
        for(int j = 0; j < this.size(); ++j) {
            ItemStack itemStack = this.getStack(j);
            if (!itemStack.isEmpty()) {
                ++i;
            }
        }
        return i;
    }




}
