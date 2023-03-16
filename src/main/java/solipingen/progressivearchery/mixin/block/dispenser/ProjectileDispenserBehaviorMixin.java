package solipingen.progressivearchery.mixin.block.dispenser;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;


@Mixin(ProjectileDispenserBehavior.class)
public abstract class ProjectileDispenserBehaviorMixin extends ItemDispenserBehavior {


    @Redirect(method = "dispenseSilently", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(DDDFF)V"))
    private void redirectedSetVelocity(ProjectileEntity projectileEntity, double originalX, double originalY, double originalZ, float originalSpeed, float originalDivergence) {
        Vec3d dispenserPos = projectileEntity.getPos().subtract(originalX, originalY - 0.1, originalZ);
        BlockPos dispenserBlockPos = new BlockPos(dispenserPos.getX() < 0 ? -MathHelper.floor(Math.abs(dispenserPos.getX())) : MathHelper.floor(dispenserPos.getX()), dispenserPos.getY() < 0 ? -MathHelper.floor(Math.abs(dispenserPos.getY())) : MathHelper.floor(dispenserPos.getY()), dispenserPos.getZ() < 0 ? -MathHelper.floor(Math.abs(dispenserPos.getZ())) : MathHelper.floor(dispenserPos.getZ()));
        int dispenserPowerLevel = projectileEntity.world.getReceivedRedstonePower(dispenserBlockPos);
        projectileEntity.setVelocity(originalX, originalY - 0.1, originalZ, projectileEntity instanceof KidArrowEntity ? Math.min(0.2f*dispenserPowerLevel, 4.2f) : Math.min(0.2f*dispenserPowerLevel, 3.0f), 2.0f);
    }
    
    @ModifyConstant(method = "getVariation", constant = @Constant(floatValue = 6.0f))
    private float injectedGetVariation(float originalF) {
        return 2.0f;
    }

    @ModifyConstant(method = "getForce", constant = @Constant(floatValue = 1.1f))
    private float injectedGetForce(float originalF) {
        return 3.0f;
    }

}
