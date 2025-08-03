package solipingen.progressivearchery.entity.passive;

import java.util.Objects;
import java.util.UUID;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    default public void writeArcherAngerToData(WriteView view) {
        view.putInt(ANGER_TIME_KEY, this.getArcherAngerTime());
        view.putNullable(ANGRY_AT_KEY, Uuids.INT_STREAM_CODEC, this.getArcherAngryAt());
    }

    default public void readArcherAngerFromData(World world, ReadView view) {
        this.setArcherAngerTime(view.getInt(ANGER_TIME_KEY, 0));
        if (!(world instanceof ServerWorld)) {
            return;
        }
        UUID uUID = view.read(ANGRY_AT_KEY, Uuids.INT_STREAM_CODEC).orElse(null);
        this.setArcherAngryAt(uUID);
        Entity entity = world.getEntity(uUID);
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
        if (livingEntity != null && (livingEntity.isSpectator() || (livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).isCreative()))) {
            this.stopArcherAnger();
        }
    }

    default public boolean shouldArcherAngerAt(LivingEntity entity, ServerWorld world) {
        if (!this.canArcherTarget(entity, world)) {
            return false;
        }
        if (entity.getType() == EntityType.PLAYER && this.isUniversallyArcherAngry(world)) {
            return true;
        }
        return entity.getUuid().equals(this.getArcherAngryAt());
    }

    default public boolean isUniversallyArcherAngry(ServerWorld world) {
        return world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) && this.hasArcherAngerTime() && this.getArcherAngryAt() == null;
    }

    default public boolean hasArcherAngerTime() {
        return this.getArcherAngerTime() > 0;
    }

    default public void forgive(ServerPlayerEntity player) {
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

    public boolean canArcherTarget(LivingEntity var1, ServerWorld serverWorld);

    @Nullable
    public LivingEntity getArcherTarget();
}
