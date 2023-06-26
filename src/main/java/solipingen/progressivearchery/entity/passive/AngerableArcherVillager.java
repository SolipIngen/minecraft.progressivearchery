package solipingen.progressivearchery.entity.passive;

import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;


public interface AngerableArcherVillager {
    public static final String ANGER_TIME_KEY = "ArcherAngerTime";
    public static final String ANGRY_AT_KEY = "ArcherAngryAt";

    public int getArcherAngerTime();

    public void setArcherAngerTime(int var1);

    @Nullable
    public UUID getArcherAngryAt();

    public void setArcherAngryAt(@Nullable UUID var1);

    public void chooseRandomArcherAngerTime();

    default public void writeArcherAngerToNbt(NbtCompound nbt) {
        nbt.putInt(ANGER_TIME_KEY, this.getArcherAngerTime());
        if (this.getArcherAngryAt() != null) {
            nbt.putUuid(ANGRY_AT_KEY, this.getArcherAngryAt());
        }
    }

    default public void readArcherAngerFromNbt(World world, NbtCompound nbt) {
        this.setArcherAngerTime(nbt.getInt(ANGER_TIME_KEY));
        if (!(world instanceof ServerWorld)) {
            return;
        }
        if (!nbt.containsUuid(ANGRY_AT_KEY)) {
            this.setArcherAngryAt(null);
            return;
        }
        UUID uUID = nbt.getUuid(ANGRY_AT_KEY);
        this.setArcherAngryAt(uUID);
        Entity entity = ((ServerWorld)world).getEntity(uUID);
        if (entity == null) {
            return;
        }
        if (entity instanceof MobEntity) {
            this.setArcherAttacker((MobEntity)entity);
        }
        if (entity.getType() == EntityType.PLAYER) {
            this.setArcherAttacking((PlayerEntity)entity);
        }
    }

    /**
     * @param angerPersistent if {@code true}, the anger time will not decrease for a player target
     */
    default public void tickArcherAngerLogic(ServerWorld world, boolean angerPersistent) {
        LivingEntity livingEntity = this.getArcherTarget();
        UUID uUID = this.getArcherAngryAt();
        if ((livingEntity == null || livingEntity.isDead()) && uUID != null && world.getEntity(uUID) instanceof MobEntity) {
            this.stopArcherAnger();
            return;
        }
        if (livingEntity != null && !Objects.equals(uUID, livingEntity.getUuid())) {
            this.setArcherAngryAt(livingEntity.getUuid());
            this.chooseRandomArcherAngerTime();
        }
        if (!(this.getArcherAngerTime() <= 0 || livingEntity != null && livingEntity.getType() == EntityType.PLAYER && angerPersistent)) {
            this.setArcherAngerTime(this.getArcherAngerTime() - 1);
            if (this.getArcherAngerTime() == 0) {
                this.stopArcherAnger();
            }
        }
    }

    default public boolean shouldArcherAngerAt(LivingEntity entity) {
        if (!this.canArcherTarget(entity)) {
            return false;
        }
        if (entity.getType() == EntityType.PLAYER && this.isUniversallyArcherAngry(entity.getWorld())) {
            return true;
        }
        return entity.getUuid().equals(this.getArcherAngryAt());
    }

    default public boolean isUniversallyArcherAngry(World world) {
        return world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) && this.hasArcherAngerTime() && this.getArcherAngryAt() == null;
    }

    default public boolean hasArcherAngerTime() {
        return this.getArcherAngerTime() > 0;
    }

    default public void forgive(PlayerEntity player) {
        if (!player.getWorld().getGameRules().getBoolean(GameRules.FORGIVE_DEAD_PLAYERS)) {
            return;
        }
        if (!player.getUuid().equals(this.getArcherAngryAt())) {
            return;
        }
        this.stopArcherAnger();
    }

    default public void universallyArcherAnger() {
        this.stopArcherAnger();
        this.chooseRandomArcherAngerTime();
    }

    default public void stopArcherAnger() {
        this.setArcherAttacker(null);
        this.setArcherAngryAt(null);
        this.setArcherTarget(null);
        this.setArcherAngerTime(0);
    }

    @Nullable
    public LivingEntity getArcherAttacker();

    public void setArcherAttacker(@Nullable LivingEntity var1);

    public void setArcherAttacking(@Nullable PlayerEntity var1);

    public void setArcherTarget(@Nullable LivingEntity var1);

    public boolean canArcherTarget(LivingEntity var1);

    @Nullable
    public LivingEntity getArcherTarget();
}
