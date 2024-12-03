package solipingen.progressivearchery.mixin.block.dispenser;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;


@Mixin(ProjectileDispenserBehavior.class)
public abstract class ProjectileDispenserBehaviorMixin extends ItemDispenserBehavior {
    @Shadow @Final private ProjectileItem projectile;
    @Shadow @Final private ProjectileItem.Settings projectileSettings;


    @Inject(method = "dispenseSilently", at = @At("HEAD"), cancellable = true)
    private void injectedDispenseSilently(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cbireturn) {
        World world = pointer.world();
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        Position position = this.projectileSettings.positionFunction().getDispensePosition(pointer, direction);
        ProjectileEntity projectileEntity = this.projectile.createEntity(world, position, stack, direction);
        int dispenserPowerLevel = projectileEntity.getWorld().getReceivedRedstonePower(projectileEntity.getBlockPos());
        float maxPower = stack.getItem() instanceof KidArrowItem ? 4.2f : (stack.getItem() instanceof ModArrowItem ? 3.0f : this.projectileSettings.power());
        float power = (stack.getItem() instanceof KidArrowItem ? MathHelper.sqrt(2.0f) : 1.0f)*0.2f*dispenserPowerLevel*maxPower;
        float uncertainty = stack.getItem() instanceof KidArrowItem || stack.getItem() instanceof ModArrowItem ? 2.0f : this.projectileSettings.uncertainty();
        this.projectile.initializeProjectile(projectileEntity, direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ(), power, uncertainty);
        world.spawnEntity(projectileEntity);
        stack.decrement(1);
        cbireturn.setReturnValue(stack);
    }

}
