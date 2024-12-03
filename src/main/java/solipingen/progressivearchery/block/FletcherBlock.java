package solipingen.progressivearchery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.Orientation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.block.entity.FletcherBlockEntity;
import solipingen.progressivearchery.block.entity.ModBlockEntityTypes;
import solipingen.progressivearchery.recipe.FletchingRecipe;
import solipingen.progressivearchery.recipe.FletchingRecipeCache;
import solipingen.progressivearchery.registry.tag.ModItemTags;
import solipingen.progressivearchery.sound.ModSoundEvents;

import java.util.Iterator;
import java.util.Optional;


public class FletcherBlock extends BlockWithEntity {
    public static final MapCodec<FletcherBlock> CODEC = createCodec(FletcherBlock::new);
    public static final BooleanProperty CRAFTING = Properties.CRAFTING;
    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;
    private static final EnumProperty<Orientation> ORIENTATION = Properties.ORIENTATION;
    private static final FletchingRecipeCache RECIPE_CACHE = new FletchingRecipeCache(5);


    public FletcherBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ORIENTATION, Orientation.NORTH_UP).with(TRIGGERED, false).with(CRAFTING, false));
    }

    @Override
    protected MapCodec<FletcherBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FletcherBlockEntity fletcherBlockEntity) {
            return fletcherBlockEntity.getComparatorOutput();
        }
        else {
            return 0;
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos);
        boolean bl2 = state.get(TRIGGERED);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (bl && !bl2) {
            world.scheduleBlockTick(pos, this, 4);
            world.setBlockState(pos, state.with(TRIGGERED, true), 2);
            this.setTriggered(blockEntity, true);
        }
        else if (!bl && bl2) {
            world.setBlockState(pos, state.with(TRIGGERED, false).with(CRAFTING, false), 2);
            this.setTriggered(blockEntity, false);
        }

    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.craft(state, world, pos);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlockEntityTypes.FLETCHER_BLOCK_ENTITY, FletcherBlockEntity::tickCrafting);
    }

    private void setTriggered(@Nullable BlockEntity blockEntity, boolean triggered) {
        if (blockEntity instanceof FletcherBlockEntity fletcherBlockEntity) {
            fletcherBlockEntity.setTriggered(triggered);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        FletcherBlockEntity fletcherBlockEntity = new FletcherBlockEntity(pos, state);
        fletcherBlockEntity.setTriggered(state.contains(TRIGGERED) && state.get(TRIGGERED));
        return fletcherBlockEntity;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerLookDirection().getOpposite();
        Direction direction2;
        switch (direction) {
            case DOWN:
                direction2 = ctx.getHorizontalPlayerFacing().getOpposite();
                break;
            case UP:
                direction2 = ctx.getHorizontalPlayerFacing();
                break;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                direction2 = Direction.UP;
                break;
            default:
                throw new MatchException(null, null);
        }
        return this.getDefaultState().with(ORIENTATION, Orientation.byDirections(direction, direction2)).with(TRIGGERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (state.get(TRIGGERED)) {
            world.scheduleBlockTick(pos, this, 4);
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof FletcherBlockEntity) {
                player.openHandledScreen((FletcherBlockEntity)blockEntity);
            }
            return ActionResult.CONSUME;
        }
    }

    protected void craft(BlockState state, ServerWorld world, BlockPos pos) {
        BlockEntity var5 = world.getBlockEntity(pos);
        if (var5 instanceof FletcherBlockEntity fletcherBlockEntity) {
            CraftingRecipeInput craftingRecipeInput = fletcherBlockEntity.createRecipeInput();
            Optional optional = FletcherBlock.getFletchingRecipe(world, craftingRecipeInput);
            if (optional.isEmpty()) {
                world.playSound(null, pos, ModSoundEvents.FLETCHER_FAIL, SoundCategory.BLOCKS, 1.0f, 0.96f + 0.08f*world.getRandom().nextFloat());
            }
            else {
                RecipeEntry<FletchingRecipe> recipeEntry = (RecipeEntry)optional.get();
                ItemStack itemStack = recipeEntry.value().craft(craftingRecipeInput, world.getRegistryManager());
                if (itemStack.isEmpty()) {
                    world.playSound(null, pos, ModSoundEvents.FLETCHER_FAIL, SoundCategory.BLOCKS, 1.0f, 0.96f + 0.08f*world.getRandom().nextFloat());
                }
                else {
                    fletcherBlockEntity.setCraftingTicksRemaining(6);
                    world.setBlockState(pos, state.with(CRAFTING, true), 2);
                    itemStack.onCraftByCrafter(world);
                    this.transferOrSpawnStack(world, pos, fletcherBlockEntity, itemStack, state, recipeEntry);
                    Iterator<ItemStack> var8 = recipeEntry.value().getRemainder(craftingRecipeInput).iterator();
                    while (var8.hasNext()) {
                        ItemStack itemStack2 = (ItemStack)var8.next();
                        if (!itemStack2.isEmpty()) {
                            this.transferOrSpawnStack(world, pos, fletcherBlockEntity, itemStack2, state, recipeEntry);
                        }
                    }
                    fletcherBlockEntity.getHeldStacks().forEach((stack) -> {
                        if (!stack.isEmpty()) {
                            if (fletcherBlockEntity.getHeldStacks().indexOf(stack) == 3) {
                                if (!stack.isIn(ModItemTags.UNCONSUMED_FLETCHING_ADDITIONS)) {
                                    stack.decrement(1);
                                }
                            }
                            else {
                                stack.decrement(1);
                            }
                        }
                    });
                    fletcherBlockEntity.markDirty();
                }
            }
        }
    }

    public static Optional<RecipeEntry<FletchingRecipe>> getFletchingRecipe(World world, CraftingRecipeInput inputInventory) {
        return RECIPE_CACHE.getRecipe(world, inputInventory);
    }

    private void transferOrSpawnStack(ServerWorld world, BlockPos pos, FletcherBlockEntity blockEntity, ItemStack stack, BlockState state, RecipeEntry<FletchingRecipe> recipe) {
        Direction direction = state.get(ORIENTATION).getFacing();
        Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(direction));
        ItemStack itemStack = stack.copy();
        if (inventory != null && (inventory instanceof FletcherBlockEntity || stack.getCount() > inventory.getMaxCount(stack))) {
            while (!itemStack.isEmpty()) {
                ItemStack itemStack2 = itemStack.copyWithCount(1);
                ItemStack itemStack3 = HopperBlockEntity.transfer(blockEntity, inventory, itemStack2, direction.getOpposite());
                if (!itemStack3.isEmpty()) {
                    break;
                }
                itemStack.decrement(1);
            }
        }
        else if (inventory != null) {
            while (!itemStack.isEmpty()) {
                int i = itemStack.getCount();
                itemStack = HopperBlockEntity.transfer(blockEntity, inventory, itemStack, direction.getOpposite());
                if (i == itemStack.getCount()) {
                    break;
                }
            }
        }
        if (!itemStack.isEmpty()) {
            Vec3d vec3d = Vec3d.ofCenter(pos);
            Vec3d vec3d2 = vec3d.offset(direction, 0.7);
            ItemDispenserBehavior.spawnItem(world, itemStack, 6, direction, vec3d2);
            world.playSound(null, pos, ModSoundEvents.FLETCHER_FLETCH, SoundCategory.BLOCKS, 1.0f, 0.96f + 0.08f*world.getRandom().nextFloat());
        }

    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(ORIENTATION, rotation.getDirectionTransformation().mapJigsawOrientation(state.get(ORIENTATION)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(ORIENTATION, mirror.getDirectionTransformation().mapJigsawOrientation(state.get(ORIENTATION)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION, TRIGGERED, CRAFTING);
    }



}
