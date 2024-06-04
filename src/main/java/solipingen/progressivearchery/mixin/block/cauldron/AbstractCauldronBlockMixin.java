package solipingen.progressivearchery.mixin.block.cauldron;

import net.minecraft.util.ItemActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockMixin extends Block {
    @Shadow @Final private CauldronBehavior.CauldronBehaviorMap behaviorMap;

    
    public AbstractCauldronBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onUseWithItem", at = @At("HEAD"), cancellable = true)
    private void injectedOnUse(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cbireturn) {
        if (!this.behaviorMap.map().containsKey(stack.getItem())) {
            cbireturn.setReturnValue(ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
        }
    }
    
}
