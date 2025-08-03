package solipingen.progressivearchery.screen.fletching;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.recipe.FletchingRecipe;
import solipingen.progressivearchery.registry.tag.ModItemTags;
import solipingen.progressivearchery.screen.ModScreenHandlers;
import solipingen.progressivearchery.sound.ModSoundEvents;

import org.jetbrains.annotations.Nullable;


public class FletchingScreenHandler extends ArrowmakingScreenHandler {
    private final World world;
    @Nullable private RecipeEntry<FletchingRecipe> currentRecipe;


    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, syncId, playerInventory, context);
        this.world = playerInventory.player.getWorld();
        
        // Input Slots
        this.addSlot(new Slot(this.input, 0, 69, 23){
            
            @Override
            public boolean canInsert(ItemStack stack) {
                return FletchingScreenHandler.this.isUsableAsHead(stack);
            }
        });
        this.addSlot(new Slot(this.input, 1, 44, 23){
            
            @Override
            public boolean canInsert(ItemStack stack) {
                return FletchingScreenHandler.this.isUsableAsBody(stack);
            }
        });
        this.addSlot(new Slot(this.input, 2, 19, 23){
            
            @Override
            public boolean canInsert(ItemStack stack) {
                return FletchingScreenHandler.this.isUsableAsTail(stack);
            }
        });
        this.addSlot(new Slot(this.input, 3, 44, 53){

            @Override
            public boolean canInsert(ItemStack stack) {
                return FletchingScreenHandler.this.isUsableAsAddition(stack);
            }
        });

        // Output Slot
        this.addSlot(new Slot(this.output, 4, 129, 37){

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return FletchingScreenHandler.this.canTakeOutput(playerEntity, this.hasStack());
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                FletchingScreenHandler.this.onTakeOutput(player, stack);
            }
        });

        // Player Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Player Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(Blocks.FLETCHING_TABLE);
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        ItemStack outputStack = this.output.getStack(4);
        return (this.currentRecipe != null && this.currentRecipe.value().matches(this.createRecipeInput(), this.world)) || outputStack.getItem() instanceof ModArrowItem || outputStack.getItem() instanceof KidArrowItem;
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraftByPlayer(player, stack.getCount());
        ArrayList<ItemStack> inputStacks = new ArrayList<ItemStack>();
        for (int i = 0; i < 4; i++) {
            inputStacks.add(i, this.input.getStack(i));
        }
        this.output.unlockLastRecipe(player, inputStacks);
        this.decrementStack(0);
        this.decrementStack(1);
        this.decrementStack(2);
        if (!this.input.getStack(3).isIn(ModItemTags.UNCONSUMED_FLETCHING_ADDITIONS)) {
            this.decrementStack(3);
        }
        this.context.run((world, pos) -> world.playSound(null, pos, ModSoundEvents.FLETCHING_TABLE_USED, SoundCategory.BLOCKS, 1.0f, 1.0f + world.getRandom().nextBetween(-1, 1)*world.getRandom().nextFloat()*0.2f));
        if (player instanceof ServerPlayerEntity) {
            ModCriteria.FLETCHED_ARROW.trigger((ServerPlayerEntity)player, stack.getItem());
        }
    }

    private List<ItemStack> getInputStacks() {
        return List.of(this.input.getStack(0), this.input.getStack(1), this.input.getStack(2), this.input.getStack(3));
    }

    private CraftingRecipeInput createRecipeInput() {
        return CraftingRecipeInput.create(4, 1, this.getInputStacks());
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        itemStack.decrement(1);
        this.input.setStack(slot, itemStack);
    }

    @Override
    public void updateResult() {
        if (this.world.getServer() != null) {
            List<RecipeEntry<FletchingRecipe>> list = this.world.getServer().getRecipeManager().getAllMatches(FletchingRecipe.Type.INSTANCE, this.createRecipeInput(), this.world).toList();
            if (list.isEmpty()) {
                this.output.setStack(4, ItemStack.EMPTY);
            }
            else {
                this.currentRecipe = list.getFirst();
                ItemStack itemStack = this.currentRecipe.value().craft(this.createRecipeInput(), this.world.getRegistryManager());
                this.output.setLastRecipe(this.currentRecipe);
                this.output.setStack(4, itemStack);
            }
        }
    }

    @Override
    protected boolean isUsableAsHead(ItemStack stack) {
        if (this.world.getServer() == null) {
            return false;
        }
        else {
            return this.world.getServer().getRecipeManager().getAllOfType(FletchingRecipe.Type.INSTANCE).stream().anyMatch(recipe -> recipe.value().testHead(stack));
        }
    }

    @Override
    protected boolean isUsableAsBody(ItemStack stack) {
        if (this.world.getServer() == null) {
            return false;
        }
        else {
            return this.world.getServer().getRecipeManager().getAllOfType(FletchingRecipe.Type.INSTANCE).stream().anyMatch(recipe -> recipe.value().testBody(stack));
        }
    }

    @Override
    protected boolean isUsableAsTail(ItemStack stack) {
        if (this.world.getServer() == null) {
            return false;
        }
        else {
            return this.world.getServer().getRecipeManager().getAllOfType(FletchingRecipe.Type.INSTANCE).stream().anyMatch(recipe -> recipe.value().testTail(stack));
        }
    }

    @Override
    protected boolean isUsableAsAddition(ItemStack stack) {
        if (this.world.getServer() == null) {
            return false;
        }
        else {
            return this.world.getServer().getRecipeManager().getAllOfType(FletchingRecipe.Type.INSTANCE).stream().anyMatch(recipe -> recipe.value().testAddition(stack));
        }
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 4) {
                if (!this.insertItem(itemStack2, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index == 0 || index == 1 || index == 2 || index == 3) {
                if (!this.insertItem(itemStack2, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 41) {
                int i = this.isUsableAsAddition(itemStack) ? 1 : 0;
                if (!this.insertItem(itemStack2, i, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }


}

