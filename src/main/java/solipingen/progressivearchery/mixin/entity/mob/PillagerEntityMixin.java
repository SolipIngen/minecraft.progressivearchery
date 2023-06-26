package solipingen.progressivearchery.mixin.entity.mob;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;


@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity implements CrossbowUser, InventoryOwner {

    
    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "initGoals", constant = @Constant(floatValue = 8.0f))
    private float modifiedCrossbowRange(float originalRange) {
        return 24.0f;
    }

    @ModifyConstant(method = "initGoals", constant = @Constant(floatValue = 15.0f))
    private float modifiedSightRange(float originalRange) {
        return 32.0f;
    }

    @Inject(method = "getState", at = @At("TAIL"), cancellable = true)
    private void injectedState(CallbackInfoReturnable<IllagerEntity.State> cbireturn) {
        if (this.isHolding(ModItems.WOODEN_CROSSBOW) || this.isHolding(ModItems.COPPER_FUSED_CROSSBOW) || this.isHolding(ModItems.IRON_FUSED_CROSSBOW) || this.isHolding(ModItems.GOLD_FUSED_CROSSBOW) || this.isHolding(ModItems.DIAMOND_FUSED_CROSSBOW)) {
            cbireturn.setReturnValue(IllagerEntity.State.CROSSBOW_HOLD);
        }
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        if (this.getMainHandStack().isOf(Items.CROSSBOW) && !this.isPatrolLeader()) {
            float crossbowchooser = Math.min(random.nextFloat()*localDifficulty.getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId(), 1.0f);
            if (0.4f <= crossbowchooser && crossbowchooser < 0.8f) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_FUSED_CROSSBOW));
            }
            else if (0.8f <= crossbowchooser && crossbowchooser < 1.0f) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_FUSED_CROSSBOW));
            }
            else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_CROSSBOW));
            }
        }
    }

    @Inject(method = "enchantMainHandItem", at = @At("TAIL"))
    public void enchantMainHandItem(Random random, float power, CallbackInfo cbi) {
        ItemStack itemStack = this.getMainHandStack();
        super.enchantMainHandItem(random, power);
        if (random.nextInt(250) == 0 && itemStack.isOf(ModItems.COPPER_FUSED_CROSSBOW)) {
            Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack);
            map.putIfAbsent(Enchantments.PIERCING, 1);
            EnchantmentHelper.set(map, itemStack);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
        else if (random.nextInt(300) == 0 && itemStack.isOf(ModItems.IRON_FUSED_CROSSBOW)) {
            Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack);
            map.putIfAbsent(Enchantments.PIERCING, 1);
            EnchantmentHelper.set(map, itemStack);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
        else if (random.nextInt(150) == 0 && itemStack.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
            Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack);
            map.putIfAbsent(Enchantments.PIERCING, 2);
            EnchantmentHelper.set(map, itemStack);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
        else if (random.nextInt(200) == 0 && itemStack.isOf(ModItems.WOODEN_CROSSBOW)) {
            Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack);
            map.putIfAbsent(Enchantments.PIERCING, 1);
            EnchantmentHelper.set(map, itemStack);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
    }

    @Inject(method = "attack", at = @At("TAIL"), cancellable = true)
    private void injectedAttack(LivingEntity target, float pullProgress, CallbackInfo cbi) {
        ItemStack mainHandStack = this.getMainHandStack();
        int difficultyId = this.getWorld().getDifficulty().getId();
        if (mainHandStack.isOf(ModItems.COPPER_FUSED_CROSSBOW)) {
            this.shoot(this, 3.7f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (mainHandStack.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
            this.shoot(this, 3.7f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (mainHandStack.isOf(ModItems.IRON_FUSED_CROSSBOW)) {
            this.shoot(this, 3.8f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (mainHandStack.isOf(ModItems.DIAMOND_FUSED_CROSSBOW)) {
            this.shoot(this, 3.9f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else {
            this.shoot(this, 3.6f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
    }

    @Inject(method = "shoot", at = @At("TAIL"), cancellable = true)
    private void injectedShoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray, CallbackInfo cbi) {
        int difficultyId = this.getWorld().getDifficulty().getId();
        if (crossbow.isOf(ModItems.COPPER_FUSED_CROSSBOW)) {
            this.shoot(this, target, projectile, multiShotSpray, 3.7f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (crossbow.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
            this.shoot(this, target, projectile, multiShotSpray, 3.7f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (crossbow.isOf(ModItems.IRON_FUSED_CROSSBOW)) {
            this.shoot(this, target, projectile, multiShotSpray, 3.8f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (crossbow.isOf(ModItems.DIAMOND_FUSED_CROSSBOW)) {
            this.shoot(this, target, projectile, multiShotSpray, 3.9f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else {
            this.shoot(this, target, projectile, multiShotSpray, 3.6f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
    }

    @Inject(method = "addBonusForWave", at = @At("TAIL"))
    private void injectedAddBonusForWave(int wave, boolean unused, CallbackInfo cbi) {
        boolean bl;
        Raid raid = this.getRaid();
        bl = this.random.nextFloat() <= raid.getEnchantmentChance();
        ItemStack mainHandStack = this.getMainHandStack();
        if (bl) {
            if (mainHandStack.isOf(ModItems.COPPER_FUSED_CROSSBOW)) {
                ItemStack itemStack = new ItemStack(ModItems.COPPER_FUSED_CROSSBOW);
                HashMap<Enchantment, Integer> map = Maps.newHashMap();
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    map.put(Enchantments.QUICK_CHARGE, 1);
                    map.put(Enchantments.POWER, 2);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    map.put(Enchantments.POWER, 1);
                }
                EnchantmentHelper.set(map, itemStack);
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else if (mainHandStack.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
                ItemStack itemStack = new ItemStack(ModItems.GOLD_FUSED_CROSSBOW);
                HashMap<Enchantment, Integer> map = Maps.newHashMap();
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    map.put(Enchantments.QUICK_CHARGE, 1);
                    map.put(Enchantments.POWER, 3);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    map.put(Enchantments.POWER, 2);
                }
                map.put(Enchantments.MULTISHOT, 1);
                EnchantmentHelper.set(map, itemStack);
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else if (mainHandStack.isOf(ModItems.IRON_FUSED_CROSSBOW)) {
                ItemStack itemStack = new ItemStack(ModItems.IRON_FUSED_CROSSBOW);
                HashMap<Enchantment, Integer> map = Maps.newHashMap();
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    map.put(Enchantments.QUICK_CHARGE, 1);
                    map.put(Enchantments.POWER, 2);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    map.put(Enchantments.POWER, 1);
                }
                EnchantmentHelper.set(map, itemStack);
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else if (mainHandStack.isOf(ModItems.DIAMOND_FUSED_CROSSBOW)) {
                ItemStack itemStack = new ItemStack(ModItems.DIAMOND_FUSED_CROSSBOW);
                HashMap<Enchantment, Integer> map = Maps.newHashMap();
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    map.put(Enchantments.QUICK_CHARGE, 1);
                    map.put(Enchantments.POWER, 2);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    map.put(Enchantments.POWER, 1);
                }
                EnchantmentHelper.set(map, itemStack);
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else {
                ItemStack itemStack = new ItemStack(ModItems.WOODEN_CROSSBOW);
                HashMap<Enchantment, Integer> map = Maps.newHashMap();
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    map.put(Enchantments.QUICK_CHARGE, 1);
                    map.put(Enchantments.POWER, 2);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    map.put(Enchantments.POWER, 1);
                }
                EnchantmentHelper.set(map, itemStack);
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }         
        }
    }

    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true)
    private void injectedCanUseRangedWeapon(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(weapon instanceof CrossbowItem || weapon instanceof ModCrossbowItem);
    }

    
}
