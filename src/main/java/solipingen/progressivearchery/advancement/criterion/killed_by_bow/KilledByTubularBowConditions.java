package solipingen.progressivearchery.advancement.criterion.killed_by_bow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.util.Identifier;


public class KilledByTubularBowConditions extends AbstractCriterionConditions {
    static final Identifier ID = KilledByTubularBowCriterion.ID;
    private final List<LootContextPredicate> victims;
    private final NumberRange.IntRange uniqueEntityTypes;


    public KilledByTubularBowConditions(Optional<LootContextPredicate> player, List<LootContextPredicate> victims, NumberRange.IntRange uniqueEntityTypes) {
        super(player);
        this.victims = victims;
        this.uniqueEntityTypes = uniqueEntityTypes;
    }

    public static KilledByTubularBowConditions create(EntityPredicate.Builder ... victimPredicates) {
        ArrayList<LootContextPredicate> lootContextPredicates = new ArrayList<LootContextPredicate>(victimPredicates.length);
        for (int i = 0; i < victimPredicates.length; ++i) {
            EntityPredicate.Builder builder = victimPredicates[i];
            lootContextPredicates.set(i, EntityPredicate.asLootContextPredicate(builder.build()));
        }
        return new KilledByTubularBowConditions(Optional.empty(), lootContextPredicates, NumberRange.IntRange.ANY);
    }

    public static KilledByTubularBowConditions create(NumberRange.IntRange uniqueEntityTypes) {
        ArrayList<LootContextPredicate> extendeds = new ArrayList<LootContextPredicate>();
        return new KilledByTubularBowConditions(Optional.empty(), extendeds, uniqueEntityTypes);
    }

    public boolean matches(Collection<LootContext> victimContexts, int uniqueEntityTypeCount) {
        if (this.victims.size() > 0) {
            ArrayList<LootContext> list = Lists.newArrayList(victimContexts);
            for (LootContextPredicate extended : this.victims) {
                boolean bl = false;
                Iterator<LootContext> iterator = list.iterator();
                while (iterator.hasNext()) {
                    LootContext lootContext = (LootContext)iterator.next();
                    if (!extended.test(lootContext)) continue;
                    iterator.remove();
                    bl = true;
                    break;
                }
                if (bl) continue;
                return false;
            }
        }
        return this.uniqueEntityTypes.test(uniqueEntityTypeCount);
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = super.toJson();
        jsonObject.add("victims", LootContextPredicate.toPredicatesJsonArray(this.victims));
        jsonObject.add("unique_entity_types", this.uniqueEntityTypes.toJson());
        return jsonObject;
    }
}
