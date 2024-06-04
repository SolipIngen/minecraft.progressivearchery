package solipingen.progressivearchery.mixin.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
import net.minecraft.util.Hand;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.arrows.ModArrowItem;


@Mixin(CrossbowUser.class)
public interface CrossbowUserMixin extends RangedAttackMob {
    

    @Inject(method = "shoot(Lnet/minecraft/entity/LivingEntity;F)V", at = @At("HEAD"), cancellable = true)
    private void injectedShoot(LivingEntity shooter, float speed, CallbackInfo cbi) {
        Hand hand = Hand.MAIN_HAND;
        ItemStack itemStack = shooter.getStackInHand(hand);
        int divergence = 12;
        if (shooter.isHolding(Items.CROSSBOW)) {
            hand = ProjectileUtil.getHandPossiblyHolding(shooter, Items.CROSSBOW);
            itemStack = shooter.getStackInHand(hand);
            ((CrossbowItem)itemStack.getItem()).shootAll(shooter.getWorld(), shooter, hand, itemStack, speed, (float)(divergence - shooter.getWorld().getDifficulty().getId()*3), ((CrossbowUser)(Object)this).getTarget());
            cbi.cancel();
        }
        else {
            if (shooter.isHolding(ModItems.COPPER_FUSED_CROSSBOW)) {
                hand = ProjectileUtil.getHandPossiblyHolding(shooter, ModItems.COPPER_FUSED_CROSSBOW);
                itemStack = shooter.getStackInHand(hand);
                divergence = 11;
            }
            else if (shooter.isHolding(ModItems.GOLD_FUSED_CROSSBOW)) {
                hand = ProjectileUtil.getHandPossiblyHolding(shooter, ModItems.GOLD_FUSED_CROSSBOW);
                itemStack = shooter.getStackInHand(hand);
                divergence = 11;
            }
            else if (shooter.isHolding(ModItems.IRON_FUSED_CROSSBOW)) {
                hand = ProjectileUtil.getHandPossiblyHolding(shooter, ModItems.IRON_FUSED_CROSSBOW);
                itemStack = shooter.getStackInHand(hand);
                divergence = 10;
            }
            else if (shooter.isHolding(ModItems.DIAMOND_FUSED_CROSSBOW)) {
                hand = ProjectileUtil.getHandPossiblyHolding(shooter, ModItems.DIAMOND_FUSED_CROSSBOW);
                itemStack = shooter.getStackInHand(hand);
                divergence = 10;
            }
            ((ModCrossbowItem)itemStack.getItem()).shootAll(shooter.getWorld(), shooter, hand, itemStack, speed, (float)(divergence - shooter.getWorld().getDifficulty().getId()*3), ((CrossbowUser)(Object)this).getTarget());
            cbi.cancel();
        }
    }

}
