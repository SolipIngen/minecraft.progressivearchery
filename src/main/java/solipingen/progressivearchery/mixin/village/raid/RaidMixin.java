package solipingen.progressivearchery.mixin.village.raid;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import solipingen.progressivearchery.util.village.raid.Member;


@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow @Final private ServerWorld world;
    @Shadow private float totalHealth;
    @Shadow private int wavesSpawned;
    @Shadow @Final private ServerBossBar bar;
    @Shadow @Final private Random random;
    @Shadow @Final private int waveCount;
    @Shadow private Optional<BlockPos> preCalculatedRavagerSpawnLocation;

    @Invoker("isSpawningExtraWave")
    public abstract boolean invokeIsSpawningExtraWave();

    @Invoker("markDirty")
    public abstract void invokeMarkDirty();
    
    /**
     * @author SolipIngen
     * @reason Members have Illusioners added to it.
     */
    @Overwrite
    private void spawnNextWave(BlockPos pos) {
        boolean bl = false;
        int i = this.wavesSpawned + 1;
        this.totalHealth = 0.0f;
        LocalDifficulty localDifficulty = this.world.getLocalDifficulty(pos);
        boolean bl2 = this.invokeIsSpawningExtraWave();
        for (Member member : Member.VALUES) {
            int j = this.getCount(member, i, bl2) + this.getBonusCount(member, this.random, i, localDifficulty, bl2);
            int k = 0;
            for (int l = 0; l < j; ++l) {
                RaiderEntity raiderEntity = member.type.create(this.world);
                if (!bl && raiderEntity.canLead()) {
                    raiderEntity.setPatrolLeader(true);
                    ((Raid)(Object)this).setWaveCaptain(i, raiderEntity);
                    bl = true;
                }
                ((Raid)(Object)this).addRaider(i, raiderEntity, pos, false);
                if (member.type != EntityType.RAVAGER) continue;
                RaiderEntity raiderEntity2 = null;
                if (i == ((Raid)(Object)this).getMaxWaves(Difficulty.NORMAL)) {
                    raiderEntity2 = EntityType.PILLAGER.create(this.world);
                } else if (i >= ((Raid)(Object)this).getMaxWaves(Difficulty.HARD)) {
                    raiderEntity2 = k == 0 ? (RaiderEntity)EntityType.EVOKER.create(this.world) : (RaiderEntity)EntityType.VINDICATOR.create(this.world);
                }
                ++k;
                if (raiderEntity2 == null) continue;
                ((Raid)(Object)this).addRaider(i, raiderEntity2, pos, false);
                raiderEntity2.refreshPositionAndAngles(pos, 0.0f, 0.0f);
                raiderEntity2.startRiding(raiderEntity);
            }
        }
        this.preCalculatedRavagerSpawnLocation = Optional.empty();
        ++this.wavesSpawned;
        ((Raid)(Object)this).updateBar();
        this.invokeMarkDirty();
    }

    public int getCount(Member member, int wave, boolean extra) {
        return extra ? member.countInWave[this.waveCount] : member.countInWave[wave];
    }

    public int getBonusCount(Member member, Random random, int wave, LocalDifficulty localDifficulty, boolean extra) {
        int i;
        Difficulty difficulty = localDifficulty.getGlobalDifficulty();
        boolean bl = difficulty == Difficulty.EASY;
        boolean bl2 = difficulty == Difficulty.NORMAL;
        switch (member) {
            case WITCH: {
                if (!bl && wave > 2 && wave != 4) {
                    i = 1;
                    break;
                }
                return 0;
            }
            case PILLAGER: 
            case VINDICATOR: {
                if (bl) {
                    i = random.nextInt(2);
                    break;
                }
                if (bl2) {
                    i = 1;
                    break;
                }
                i = 2;
                break;
            }
            case RAVAGER: {
                i = !bl && extra ? 1 : 0;
                break;
            }
            default: {
                return 0;
            }
        }
        return i > 0 ? random.nextInt(i + 1) : 0;
    }
    
}
