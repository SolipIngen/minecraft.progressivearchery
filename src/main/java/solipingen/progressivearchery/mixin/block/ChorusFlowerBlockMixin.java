package solipingen.progressivearchery.mixin.block;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.arrow.ModArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;


@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin extends Block {


    public ChorusFlowerBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onProjectileHit", at = @At("HEAD"))
    private void injectedOnProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile, CallbackInfo cbi) {
        BlockPos blockPos = hit.getBlockPos();
        if (world instanceof ServerWorld serverWorld && projectile.canModifyAt(serverWorld, blockPos) && (projectile instanceof ModArrowEntity || projectile instanceof KidArrowEntity)) {
            world.breakBlock(blockPos, true, projectile);
        }
    }

    
}
