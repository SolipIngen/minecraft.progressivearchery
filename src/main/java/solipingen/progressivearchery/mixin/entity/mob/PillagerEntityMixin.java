package solipingen.progressivearchery.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
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
        if (this.isHolding(Items.CROSSBOW) || this.isHolding(ModItems.COPPER_FUSED_CROSSBOW) || this.isHolding(ModItems.IRON_FUSED_CROSSBOW) || this.isHolding(ModItems.GOLD_FUSED_CROSSBOW) || this.isHolding(ModItems.DIAMOND_FUSED_CROSSBOW)) {
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
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
            }
        }
    }

    @Inject(method = "enchantMainHandItem", at = @At("TAIL"))
    public void enchantMainHandItem(Random random, float power, CallbackInfo cbi) {
        ItemStack itemStack = this.getMainHandStack();
        super.enchantMainHandItem(random, power);
        if (random.nextInt(250) == 0 && itemStack.isOf(ModItems.COPPER_FUSED_CROSSBOW)) {
            itemStack.addEnchantment(Enchantments.PIERCING, 1);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
        else if (random.nextInt(300) == 0 && itemStack.isOf(ModItems.IRON_FUSED_CROSSBOW)) {
            itemStack.addEnchantment(Enchantments.PIERCING, 1);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
        else if (random.nextInt(150) == 0 && itemStack.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
            itemStack.addEnchantment(Enchantments.PIERCING, 2);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
        else if (random.nextInt(200) == 0 && itemStack.isOf(Items.CROSSBOW)) {
            itemStack.addEnchantment(Enchantments.PIERCING, 1);
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
        }
    }

    @ModifyConstant(method = "shootAt", constant = @Constant(floatValue = 1.6f))
    private float modifiedShootSpeed(float originalSpeed) {
        ItemStack mainHandStack = this.getMainHandStack();
        int difficultyId = this.getWorld().getDifficulty().getId();
        float speed = 3.6f - 0.6f*(3 - difficultyId);
        if (mainHandStack.isOf(ModItems.COPPER_FUSED_CROSSBOW)) {
            speed = 3.7f - 0.6f*(3 - difficultyId);
        }
        else if (mainHandStack.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
            speed = 3.7f - 0.6f*(3 - difficultyId);
        }
        else if (mainHandStack.isOf(ModItems.IRON_FUSED_CROSSBOW)) {
            speed = 3.8f - 0.6f*(3 - difficultyId);
        }
        else if (mainHandStack.isOf(ModItems.DIAMOND_FUSED_CROSSBOW)) {
            speed = 3.9f - 0.6f*(3 - difficultyId);
        }
        return speed;
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
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    itemStack.addEnchantment(Enchantments.QUICK_CHARGE, 1);
                    itemStack.addEnchantment(Enchantments.POWER, 2);
                }
                else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    itemStack.addEnchantment(Enchantments.POWER, 1);
                }
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else if (mainHandStack.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
                ItemStack itemStack = new ItemStack(ModItems.GOLD_FUSED_CROSSBOW);
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    itemStack.addEnchantment(Enchantments.QUICK_CHARGE, 1);
                    itemStack.addEnchantment(Enchantments.POWER, 3);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    itemStack.addEnchantment(Enchantments.POWER, 2);
                }
                itemStack.addEnchantment(Enchantments.MULTISHOT, 1);
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else if (mainHandStack.isOf(ModItems.IRON_FUSED_CROSSBOW)) {
                ItemStack itemStack = new ItemStack(ModItems.IRON_FUSED_CROSSBOW);
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    itemStack.addEnchantment(Enchantments.QUICK_CHARGE, 1);
                    itemStack.addEnchantment(Enchantments.POWER, 2);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    itemStack.addEnchantment(Enchantments.POWER, 1);
                }
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else if (mainHandStack.isOf(ModItems.DIAMOND_FUSED_CROSSBOW)) {
                ItemStack itemStack = new ItemStack(ModItems.DIAMOND_FUSED_CROSSBOW);
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    itemStack.addEnchantment(Enchantments.QUICK_CHARGE, 1);
                    itemStack.addEnchantment(Enchantments.POWER, 2);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    itemStack.addEnchantment(Enchantments.POWER, 1);
                }
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }
            else {
                ItemStack itemStack = new ItemStack(Items.CROSSBOW);
                if (wave > raid.getMaxWaves(Difficulty.NORMAL)) {
                    itemStack.addEnchantment(Enchantments.QUICK_CHARGE, 1);
                    itemStack.addEnchantment(Enchantments.POWER, 2);
                } else if (wave > raid.getMaxWaves(Difficulty.EASY)) {
                    itemStack.addEnchantment(Enchantments.POWER, 1);
                }
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            }         
        }
    }

    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true)
    private void injectedCanUseRangedWeapon(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(weapon instanceof CrossbowItem || weapon instanceof ModCrossbowItem);
    }

    
}
