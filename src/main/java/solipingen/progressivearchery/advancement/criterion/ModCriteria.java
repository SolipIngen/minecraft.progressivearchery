package solipingen.progressivearchery.advancement.criterion;

import net.minecraft.advancement.criterion.Criteria;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.advancement.criterion.fletcher.FletchedArrowCriterion;
import solipingen.progressivearchery.advancement.criterion.killed_by_bow.KilledByBlindedHornBowCriterion;
import solipingen.progressivearchery.advancement.criterion.killed_by_bow.KilledByHornBowCriterion;
import solipingen.progressivearchery.advancement.criterion.killed_by_bow.KilledByLongbowCriterion;
import solipingen.progressivearchery.advancement.criterion.killed_by_bow.KilledByModBowCriterion;
import solipingen.progressivearchery.advancement.criterion.killed_by_bow.KilledByTubularBowCriterion;
import solipingen.progressivearchery.advancement.criterion.shot_bow.ShotHornBowCriterion;
import solipingen.progressivearchery.advancement.criterion.shot_bow.ShotLongbowCriterion;
import solipingen.progressivearchery.advancement.criterion.shot_bow.ShotModBowCriterion;
import solipingen.progressivearchery.advancement.criterion.shot_bow.ShotTubularBowCriterion;


public class ModCriteria {

    public static final FletchedArrowCriterion FLETCHED_ARROW = Criteria.register(new FletchedArrowCriterion());
    public static final ShotModBowCriterion SHOT_BOW = Criteria.register(new ShotModBowCriterion());
    public static final ShotHornBowCriterion SHOT_HORN_BOW = Criteria.register(new ShotHornBowCriterion());
    public static final ShotLongbowCriterion SHOT_LONGBOW = Criteria.register(new ShotLongbowCriterion());
    public static final ShotTubularBowCriterion SHOT_TUBULAR_BOW = Criteria.register(new ShotTubularBowCriterion());
    public static final KilledByModBowCriterion KILLED_BY_BOW = Criteria.register(new KilledByModBowCriterion());
    public static final KilledByHornBowCriterion KILLED_BY_HORN_BOW = Criteria.register(new KilledByHornBowCriterion());
    public static final KilledByBlindedHornBowCriterion KILLED_BY_BLINDED_HORN_BOW = Criteria.register(new KilledByBlindedHornBowCriterion());
    public static final KilledByLongbowCriterion KILLED_BY_LONGBOW = Criteria.register(new KilledByLongbowCriterion());
    public static final KilledByTubularBowCriterion KILLED_BY_TUBULAR_BOW = Criteria.register(new KilledByTubularBowCriterion());


    public static void registerModAdvancementCriteria() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Advancement Criteria for " + ProgressiveArchery.MOD_ID);
    }
    
}
