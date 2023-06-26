package solipingen.progressivearchery.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;


@Mixin(CrossbowUser.class)
public interface CrossbowUserMixin extends RangedAttackMob {
    

    @Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/CrossbowUser;postShoot()V"))
    private void injectedShoot(LivingEntity entity, float speed, CallbackInfo cbi) {
        if (entity.isHolding(ModItems.WOODEN_CROSSBOW)) {
            Hand hand = ProjectileUtil.getHandPossiblyHolding(entity, ModItems.WOODEN_CROSSBOW);
            ItemStack itemStack = entity.getStackInHand(hand);
            ModCrossbowItem.shootAll(entity.getWorld(), entity, hand, itemStack, speed, 12 - entity.getWorld().getDifficulty().getId() * 3);
        }
        else if (entity.isHolding(ModItems.COPPER_FUSED_CROSSBOW)) {
            Hand hand = ProjectileUtil.getHandPossiblyHolding(entity, ModItems.COPPER_FUSED_CROSSBOW);
            ItemStack itemStack = entity.getStackInHand(hand);
            ModCrossbowItem.shootAll(entity.getWorld(), entity, hand, itemStack, speed, 11 - entity.getWorld().getDifficulty().getId() * 3);
        }
        else if (entity.isHolding(ModItems.GOLD_FUSED_CROSSBOW)) {
            Hand hand = ProjectileUtil.getHandPossiblyHolding(entity, ModItems.GOLD_FUSED_CROSSBOW);
            ItemStack itemStack = entity.getStackInHand(hand);
            ModCrossbowItem.shootAll(entity.getWorld(), entity, hand, itemStack, speed, 11 - entity.getWorld().getDifficulty().getId() * 3);
        }
        else if (entity.isHolding(ModItems.IRON_FUSED_CROSSBOW)) {
            Hand hand = ProjectileUtil.getHandPossiblyHolding(entity, ModItems.IRON_FUSED_CROSSBOW);
            ItemStack itemStack = entity.getStackInHand(hand);
            ModCrossbowItem.shootAll(entity.getWorld(), entity, hand, itemStack, speed, 10 - entity.getWorld().getDifficulty().getId() * 3);
        }
        else if (entity.isHolding(ModItems.DIAMOND_FUSED_CROSSBOW)) {
            Hand hand = ProjectileUtil.getHandPossiblyHolding(entity, ModItems.DIAMOND_FUSED_CROSSBOW);
            ItemStack itemStack = entity.getStackInHand(hand);
            ModCrossbowItem.shootAll(entity.getWorld(), entity, hand, itemStack, speed, 10 - entity.getWorld().getDifficulty().getId() * 3);
        }
    }

    @ModifyConstant(method = "Lnet/minecraft/entity/CrossbowUser;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;FF)V", constant = @Constant(doubleValue = (double)0.2f))
    private double modifiedDivergenceConstant(double originalD, LivingEntity entity, LivingEntity target, ProjectileEntity projectile, float multishotSpray, float speed) {
        if (entity.isHolding(Items.CROSSBOW)) {
            return originalD;
        }
        return 0.1;
    }

}
