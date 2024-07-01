package solipingen.progressivearchery.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModItems;


@Mixin(IllusionerEntity.class)
public abstract class IllusionerEntityMixin extends SpellcastingIllagerEntity implements RangedAttackMob {

    
    protected IllusionerEntityMixin(EntityType<? extends SpellcastingIllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cbireturn) {
        float hornbowrandom = Math.min(random.nextFloat()*difficulty.getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId(), 1.0f);
        if (0.2f <= hornbowrandom && hornbowrandom < 0.5f) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_FUSED_HORN_BOW));
        }
        else if (0.5f <= hornbowrandom && hornbowrandom < 0.8f) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_FUSED_HORN_BOW));
        }
        else if (0.8f <= hornbowrandom && hornbowrandom < 1.0f) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLD_FUSED_HORN_BOW));
        } 
        else {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HORN_BOW));
        }
    }

    @Inject(method = "shootAt", at = @At("HEAD"), cancellable = true)
    private void injectedShootAt(LivingEntity target, float pullProgress, CallbackInfo cbi) {
        ItemStack itemStack = this.getMainHandStack();
        if (itemStack.isEmpty()) {
            itemStack = this.getOffHandStack();
            if (itemStack.isEmpty()) {
                cbi.cancel();
            }
        }
        ItemStack arrowStack = this.getProjectileType(itemStack);
        PersistentProjectileEntity persistentProjectileEntity = ProjectileUtil.createArrowProjectile(this, arrowStack, pullProgress, itemStack);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        int difficultyId = this.getWorld().getDifficulty().getId();
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        if (itemStack.isOf(Items.BOW)) {
            persistentProjectileEntity.setVelocity(d, e + g*0.14, f, 3.0f + 0.15f*strengthLevel - 0.15f*weaknessLevel - 0.3f*(3 - difficultyId), 13.0f - difficultyId*4);
            this.getWorld().spawnEntity(persistentProjectileEntity);
            this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            cbi.cancel();
        }
        if (itemStack.getItem() instanceof ModBowItem) {
            persistentProjectileEntity.setVelocity(d, e + g*0.14 - 0.01*(((ModBowItem)itemStack.getItem()).getBowType() + ((ModBowItem)itemStack.getItem()).getMaterial().getMiningLevel()), f, ((ModBowItem)itemStack.getItem()).getMaxReleaseSpeed() + 0.15f*strengthLevel - 0.15f*weaknessLevel - 0.3f*(3 - difficultyId), 13.0f - difficultyId*4);
            this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            this.getWorld().spawnEntity(persistentProjectileEntity);
            cbi.cancel();
        }
    }

}
