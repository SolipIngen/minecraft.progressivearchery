package solipingen.progressivearchery.mixin.structure;

import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.SwampHutGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.cauldron.PotionCauldronBlock;

import java.util.ArrayList;


@Mixin(SwampHutGenerator.class)
public abstract class SwampHutGeneratorMixin extends ShiftableStructurePiece {


    protected SwampHutGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @Inject(method = "generate", at = @At("TAIL"))
    private void injectedPotionCauldronGenerate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo cbi) {
        for (int y = chunkBox.getMinY(); y <= chunkBox.getMaxY(); y++) {
            for (int x = chunkBox.getMinX(); x <= chunkBox.getMaxX(); x++) {
                for (int z = chunkBox.getMinZ(); z <= chunkBox.getMaxZ(); z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    if (world.getBlockState(blockPos).isOf(Blocks.CAULDRON)) {
                        ArrayList<Integer> blockPotionTypeArray = new ArrayList<>(PotionCauldronBlock.BLOCK_POTION_TYPE.values());
                        int blockPotionType = blockPotionTypeArray.get(random.nextInt(Registries.POTION.size()));
                        BlockState blockState = ModBlocks.POTION_CAULDRON.getDefaultState().with(PotionCauldronBlock.LEVEL, random.nextBetween(1, 3)).with(PotionCauldronBlock.POTION_TYPE, blockPotionType);
                        world.setBlockState(blockPos, blockState, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }

    
}
