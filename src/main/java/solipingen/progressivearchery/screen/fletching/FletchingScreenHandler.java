package solipingen.progressivearchery.screen.fletching;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import solipingen.progressivearchery.screen.ModScreenHandlers;
import solipingen.progressivearchery.sound.ModSoundEvents;

import org.jetbrains.annotations.Nullable;


public class FletchingScreenHandler extends ArrowmakingScreenHandler {
    private final World world;
    @Nullable private FletchingRecipe currentRecipe;
    private final List<FletchingRecipe> recipes;


    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, syncId, playerInventory, context);
        this.world = playerInventory.player.getWorld();
        this.recipes = this.world.getRecipeManager().listAllOfType(FletchingRecipe.Type.INSTANCE);
        
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
        return (this.currentRecipe != null && this.currentRecipe.matches(this.input, this.world)) || outputStack.getItem() instanceof ModArrowItem || outputStack.getItem() instanceof KidArrowItem;
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.getWorld(), player, stack.getCount());
        ArrayList<ItemStack> inputStacks = new ArrayList<ItemStack>(4);
        for (int i = 0; i < 4; i++) {
            inputStacks.set(i, this.input.getStack(i));
        }
        this.output.unlockLastRecipe(player, inputStacks);
        this.decrementStack(0);
        this.decrementStack(1);
        this.decrementStack(2);
        if (this.input.getStack(3).getItem() == Items.STRING || this.input.getStack(3).getItem() == Items.GLOWSTONE_DUST) {
            this.decrementStack(3);
        }
        this.context.run((world, pos) -> world.playSound(null, (BlockPos)pos, ModSoundEvents.FLETCHING_TABLE_USED, SoundCategory.BLOCKS, 1.0f, 1.0f + world.getRandom().nextBetween(-1, 1)*world.getRandom().nextFloat()*0.2f));
        if (player instanceof ServerPlayerEntity) {
            ModCriteria.FLETCHED_ARROW.trigger((ServerPlayerEntity)player, stack.getItem());
        }
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        itemStack.decrement(1);
        this.input.setStack(slot, itemStack);
    }

    @Override
    public void updateResult() {
        List<FletchingRecipe> list = this.world.getRecipeManager().getAllMatches(FletchingRecipe.Type.INSTANCE, this.input, this.world);
        if (list.isEmpty()) {
            this.output.setStack(4, ItemStack.EMPTY);
        } else {
            this.currentRecipe = list.get(0);
            ItemStack itemStack = this.currentRecipe.craft(this.input, this.world.getRegistryManager());
            this.output.setLastRecipe(this.currentRecipe);
            this.output.setStack(4, itemStack);
        }
    }

    @Override
    protected boolean isUsableAsHead(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.testHead(stack));
    }

    @Override
    protected boolean isUsableAsBody(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.testBody(stack));
    }

    @Override
    protected boolean isUsableAsTail(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.testTail(stack));
    }

    @Override
    protected boolean isUsableAsAddition(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.testAddition(stack));
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
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

