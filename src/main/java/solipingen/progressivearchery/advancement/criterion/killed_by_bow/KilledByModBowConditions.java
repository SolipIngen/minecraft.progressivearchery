package solipingen.progressivearchery.advancement.criterion.killed_by_bow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;

public class KilledByModBowConditions extends AbstractCriterionConditions {
    static final Identifier ID = KilledByModBowCriterion.ID;
    private final EntityPredicate.Extended[] victims;
    private final NumberRange.IntRange uniqueEntityTypes;


    public KilledByModBowConditions(EntityPredicate.Extended player, EntityPredicate.Extended[] victims, NumberRange.IntRange uniqueEntityTypes) {
        super(ID, player);
        this.victims = victims;
        this.uniqueEntityTypes = uniqueEntityTypes;
    }

    public static KilledByModBowConditions create(EntityPredicate.Builder ... victimPredicates) {
        EntityPredicate.Extended[] extendeds = new EntityPredicate.Extended[victimPredicates.length];
        for (int i = 0; i < victimPredicates.length; ++i) {
            EntityPredicate.Builder builder = victimPredicates[i];
            extendeds[i] = EntityPredicate.Extended.ofLegacy(builder.build());
        }
        return new KilledByModBowConditions(EntityPredicate.Extended.EMPTY, extendeds, NumberRange.IntRange.ANY);
    }

    public static KilledByModBowConditions create(NumberRange.IntRange uniqueEntityTypes) {
        EntityPredicate.Extended[] extendeds = new EntityPredicate.Extended[]{};
        return new KilledByModBowConditions(EntityPredicate.Extended.EMPTY, extendeds, uniqueEntityTypes);
    }

    public boolean matches(Collection<LootContext> victimContexts, int uniqueEntityTypeCount) {
        if (this.victims.length > 0) {
            ArrayList<LootContext> list = Lists.newArrayList(victimContexts);
            for (EntityPredicate.Extended extended : this.victims) {
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
    public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
        JsonObject jsonObject = super.toJson(predicateSerializer);
        jsonObject.add("victims", EntityPredicate.Extended.toPredicatesJsonArray(this.victims, predicateSerializer));
        jsonObject.add("unique_entity_types", this.uniqueEntityTypes.toJson());
        return jsonObject;
    }
}
