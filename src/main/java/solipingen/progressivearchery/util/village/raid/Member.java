package solipingen.progressivearchery.util.village.raid;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;

public enum Member {
    PILLAGER(EntityType.PILLAGER, new int[]{0, 4, 3, 3, 4, 4, 4, 2}),
    VINDICATOR(EntityType.VINDICATOR, new int[]{0, 0, 2, 0, 1, 4, 2, 5}),
    WITCH(EntityType.WITCH, new int[]{0, 0, 0, 0, 3, 0, 0, 1}),
    ILLUSIONER(EntityType.ILLUSIONER, new int[]{0, 0, 0, 0, 1, 0, 1, 2}),
    EVOKER(EntityType.EVOKER, new int[]{0, 0, 0, 0, 0, 1, 1, 2}),
    RAVAGER(EntityType.RAVAGER, new int[]{0, 0, 0, 1, 0, 1, 0, 2});

    public static final Member[] VALUES;
    public final EntityType<? extends RaiderEntity> type;
    public final int[] countInWave;


    private Member(EntityType<? extends RaiderEntity> type, int[] countInWave) {
        this.type = type;
        this.countInWave = countInWave;
    }

    static {
        VALUES = Member.values();
    }
    
}
