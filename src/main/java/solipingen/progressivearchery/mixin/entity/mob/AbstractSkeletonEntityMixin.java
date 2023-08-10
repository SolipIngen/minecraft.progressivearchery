package solipingen.progressivearchery.mixin.entity.mob;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.arrows.KidArrowItem;


@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity implements RangedAttackMob {
    @Shadow private BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal;
    @Shadow private MeleeAttackGoal meleeAttackGoal;

    @Invoker("createArrowProjectile")
    public abstract PersistentProjectileEntity invokeCreateArrowProjectile(ItemStack arrow, float damageModifier);


    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "initEquipment", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;BOW:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC))
    private Item redirectedItem(Random random, LocalDifficulty localDifficulty) {
        float randomf = random.nextFloat();
        if (randomf < 0.6f) {
            float bowrandom = Math.min(random.nextFloat()*localDifficulty.getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId(), 1.0f);
            if (0.4f <= bowrandom && bowrandom < 0.7f) {
                return ModItems.COPPER_FUSED_BOW;
            }
            else if (0.7f <= bowrandom && bowrandom < 0.9f) {
                return ModItems.IRON_FUSED_BOW;
            }
            else if (0.9f <= bowrandom && bowrandom < 0.998f) {
                return ModItems.GOLD_FUSED_BOW;
            }
            else if (0.998f <= bowrandom && bowrandom < 1.0f) {
                return ModItems.DIAMOND_FUSED_BOW;
            }
        }
        else if (0.6f <= randomf && randomf < 0.9f) {
            float hornbowrandom = Math.min(random.nextFloat()*localDifficulty.getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId(), 1.0f);
            if (hornbowrandom < 0.4f) {
                return ModItems.HORN_BOW;
            }
            else if (0.4f <= hornbowrandom && hornbowrandom < 0.7f) {
                return ModItems.COPPER_FUSED_HORN_BOW;
            }
            else if (0.7f <= hornbowrandom && hornbowrandom < 0.9f) {
                return ModItems.IRON_FUSED_HORN_BOW;
            }
            else if (0.9f <= hornbowrandom && hornbowrandom < 0.998f) {
                return ModItems.GOLD_FUSED_HORN_BOW;
            }
            else if (0.998f <= hornbowrandom && hornbowrandom < 1.0f) {
                return ModItems.DIAMOND_FUSED_HORN_BOW;
            }
        }
        else if (0.9f <= randomf && randomf < 0.96f) {
            float longbowrandom = Math.min(random.nextFloat()*localDifficulty.getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId(), 1.0f);
            if (longbowrandom < 0.4f) {
                return ModItems.LONGBOW;
            }
            else if (0.4f <= longbowrandom && longbowrandom < 0.7f) {
                return ModItems.COPPER_FUSED_LONGBOW;
            }
            else if (0.7f <= longbowrandom && longbowrandom < 0.9f) {
                return ModItems.IRON_FUSED_LONGBOW;
            }
            else if (0.9f <= longbowrandom && longbowrandom < 0.998f) {
                return ModItems.GOLD_FUSED_LONGBOW;
            }
            else if (0.998f <= longbowrandom && longbowrandom < 1.0f) {
                return ModItems.DIAMOND_FUSED_LONGBOW;
            }
        }
        else if (0.96f <= randomf && randomf <= 1.0f) {
            float tubularbowrandom = Math.min(random.nextFloat()*localDifficulty.getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId(), 1.0f);
            if (tubularbowrandom < 0.4f) {
                return ModItems.TUBULAR_BOW;
            }
            else if (0.4f <= tubularbowrandom && tubularbowrandom < 0.7f) {
                return ModItems.COPPER_FUSED_TUBULAR_BOW;
            }
            else if (0.7f <= tubularbowrandom && tubularbowrandom < 0.9f) {
                return ModItems.IRON_FUSED_TUBULAR_BOW;
            }
            else if (0.9f <= tubularbowrandom && tubularbowrandom < 0.998f) {
                return ModItems.GOLD_FUSED_TUBULAR_BOW;
            }
            else if (0.998f <= tubularbowrandom && tubularbowrandom < 1.0f) {
                return ModItems.DIAMOND_FUSED_TUBULAR_BOW;
            }
        }
        return Items.BOW;
    }

    @Inject(method = "updateAttackType", at = @At("HEAD"), cancellable = true)
    private void inhjectedUpdateAttackType(CallbackInfo cbi) {
        World world = this.getWorld();
        if (world == null || world.isClient) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.bowAttackGoal);

        Item mainhandItem = this.getMainHandStack().getItem();
        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, mainhandItem));

        if (itemStack.isOf(Items.BOW)) {
            int i = 20;
            if (world.getDifficulty() != Difficulty.HARD) {
                i = 40;
            }
            this.bowAttackGoal.setAttackInterval(i);
            this.goalSelector.add(4, this.bowAttackGoal);
            cbi.cancel();
        }
        if (itemStack.getItem() instanceof ModBowItem) {
            int i = 20 + 5*((ModBowItem)itemStack.getItem()).getBowType();
            if (world.getDifficulty() != Difficulty.HARD) {
                i = 40 + 5*((ModBowItem)itemStack.getItem()).getBowType();
            }
            this.bowAttackGoal.setAttackInterval(i);
            this.goalSelector.add(4, this.bowAttackGoal);
            cbi.cancel();
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void injectedAttack(LivingEntity target, float pullProgress, CallbackInfo cbi) {
        if (!(this.getMainHandStack().isOf(Items.BOW) || this.getOffHandStack().isOf(Items.BOW))) {
            ItemStack itemStack = this.getMainHandStack();
            if (itemStack.isEmpty()) {
                itemStack = this.getOffHandStack();
                if (itemStack.isEmpty()) {
                    cbi.cancel();
                }
            }
            ItemStack arrowStack = this.getProjectileType(itemStack);
            PersistentProjectileEntity persistentProjectileEntity = this.invokeCreateArrowProjectile(arrowStack, pullProgress);
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            int difficultyLevel = this.getWorld().getDifficulty().getId();
            if (itemStack.isOf(Items.BOW)) {
                persistentProjectileEntity.setVelocity(d, e + g * 0.2, f, 2.4f - 0.3f*(3 - difficultyLevel), 11.0f - difficultyLevel * 3);
                this.getWorld().spawnEntity(persistentProjectileEntity);
                this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            }
            if (itemStack.getItem() instanceof ModBowItem) {
                int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
                int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
                float releaseSpeed = 0.75f*((ModBowItem)itemStack.getItem()).getMaxReleaseSpeed() + 0.15f*strengthLevel - 0.15f*weaknessLevel;
                if (((ModBowItem)itemStack.getItem()).getBowType() == 3) {
                    persistentProjectileEntity = this.createKidArrowProjectile(itemStack, pullProgress);
                }
                persistentProjectileEntity.setVelocity(d, e + g * 0.1, f, releaseSpeed - 0.3f*(3 - difficultyLevel), 10.5f - difficultyLevel * 3);
                this.getWorld().spawnEntity(persistentProjectileEntity);
                this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            }
            cbi.cancel();
        }
    }

    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true)
    private void injectedCanUseRangedWeapon(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(weapon instanceof BowItem || weapon instanceof ModBowItem);
    }
    
    private PersistentProjectileEntity createKidArrowProjectile(ItemStack stack, float damageModifier) {
        KidArrowItem kidarrowItem = (KidArrowItem)(stack.getItem() instanceof KidArrowItem ? stack.getItem() : ModItems.WOODEN_KID_ARROW);
        PersistentProjectileEntity persistentProjectileEntity = kidarrowItem.createKidArrow(this.getWorld(), stack, this);
        persistentProjectileEntity.applyEnchantmentEffects(this, damageModifier);
        if (stack.isOf(ModItems.TIPPED_KID_ARROW) && persistentProjectileEntity instanceof GoldenKidArrowEntity) {
            ((GoldenKidArrowEntity)persistentProjectileEntity).initFromStack(stack);
        }
        return persistentProjectileEntity;
    }

}
