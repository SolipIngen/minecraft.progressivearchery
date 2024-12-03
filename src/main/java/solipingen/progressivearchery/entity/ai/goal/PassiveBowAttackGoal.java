package solipingen.progressivearchery.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.village.ModVillagerProfessions;


public class PassiveBowAttackGoal<T extends PassiveEntity> extends Goal {
    private final T actor;
    private final double speed;
    private int attackInterval;
    private final float squaredRange;
    private int cooldown = -1;
    private int targetSeeingTicker;
    private boolean movingToLeft;
    private boolean backward;
    private int combatTicks = -1;


    public PassiveBowAttackGoal(T actor, double speed, int attackInterval, float range) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public void setAttackInterval(int attackInterval) {
        this.attackInterval = attackInterval;
    }

    @Override
    public boolean canStart() {
        if (((MobEntity)this.actor).getTarget() == null) {
            return false;
        }
        return this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return ((LivingEntity)this.actor).isHolding(stack -> stack.getItem() instanceof RangedWeaponItem);
    }


    @Override
    public boolean shouldContinue() {
        return (this.canStart() || !((MobEntity)this.actor).getNavigation().isIdle()) && this.isHoldingBow() && ((MobEntity)this.actor).getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        ((MobEntity)this.actor).setAttacking(true);
        if (this.actor.getWorld() instanceof ServerWorld && this.actor instanceof VillagerEntity) {
            this.actor.playSound(ModSoundEvents.VILLAGER_ATTACK, 1.0f, actor.getSoundPitch());
        }
    }

    @Override
    public void stop() {
        super.stop();
        ((MobEntity)this.actor).setAttacking(false);
        this.targetSeeingTicker = 0;
        this.cooldown = -1;
        if (this.actor instanceof VillagerEntity) {
            VillagerEntity villager = (VillagerEntity)this.actor;
            VillagerProfession profession = villager.getVillagerData().getProfession();
            if (profession == ModVillagerProfessions.ARCHER) {
                int i = villager.getVillagerData().getLevel();
                boolean levelUpBl = VillagerData.canLevelUp(i) && villager.getExperience() >= VillagerData.getUpperLevelExperience(i);
                if (levelUpBl) {
                    villager.setVillagerData(villager.getVillagerData().withLevel(i + 1));
                    villager.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1));
                    if (villager.getWorld() instanceof ServerWorld) {
                        villager.reinitializeBrain((ServerWorld)villager.getWorld());
                    }
                }
            }
        }
        ((LivingEntity)this.actor).clearActiveItem();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = ((MobEntity)this.actor).getTarget();
        ItemStack stack = ((LivingEntity)this.actor).getMainHandStack();
        Item item = stack.getItem();
        int time = ((LivingEntity)this.actor).getItemUseTime();
        if (livingEntity == null) {
            return;
        }
        double d = ((Entity)this.actor).squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        boolean bl = ((MobEntity)this.actor).getVisibilityCache().canSee(livingEntity);
        boolean bl2 = this.targetSeeingTicker > 0;
        if (bl != bl2) {
            this.targetSeeingTicker = 0;
        }
        this.targetSeeingTicker = bl ? ++this.targetSeeingTicker : --this.targetSeeingTicker;
        if (d > (double)this.squaredRange || this.targetSeeingTicker < 20) {
            ((MobEntity)this.actor).getNavigation().startMovingTo(livingEntity, this.speed);
            this.combatTicks = -1;
        } 
        else {
            ((MobEntity)this.actor).getNavigation().stop();
            ++this.combatTicks;
        }
        if (this.combatTicks >= this.getMaxPullTime()) {
            if ((double)((LivingEntity)this.actor).getRandom().nextFloat() < 0.3) {
                this.backward = !this.backward;
            }
            this.combatTicks = 0;
        }
        if (this.combatTicks > -1) {
            if (d > (double)(this.squaredRange * 0.75f)) {
                this.backward = false;
            } 
            else if (d < (double)(this.squaredRange * 0.25f)) {
                this.backward = true;
            }
            ((MobEntity)this.actor).getMoveControl().strafeTo(this.backward ? -0.5f : 0.5f, this.movingToLeft ? 0.5f : -0.5f);
            ((MobEntity)this.actor).lookAtEntity(livingEntity, 30.0f, 30.0f);
        } 
        else {
            ((MobEntity)this.actor).getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
        }
        if (((LivingEntity)this.actor).isUsingItem()) {
            int i = ((LivingEntity)this.actor).getItemUseTime();
            if (!bl && this.targetSeeingTicker < -60) {
                ((LivingEntity)this.actor).clearActiveItem();
            } 
            else if (bl && i >= this.getMaxPullTime()) {
                ((LivingEntity)this.actor).clearActiveItem();
                float pullProgress = item instanceof BowItem ? ModBowItem.getVanillaBowPullProgress(i, stack, this.actor.getWorld()) : ((ModBowItem)item).getPullProgress(time, stack, this.actor.getWorld());
                ((RangedAttackMob)this.actor).shootAt(livingEntity, pullProgress);
                this.cooldown = this.attackInterval;
            }
        } 
        else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
            ((LivingEntity)this.actor).setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, stack.getItem()));
        }
    }

    private int getMaxPullTime() {
        ItemStack itemStack = ((LivingEntity)this.actor).getActiveItem();
        if (itemStack.getItem() instanceof ModBowItem) {
            return 20 + 5*((ModBowItem)itemStack.getItem()).getBowType();
        }
        return 20;
    }


}
