package solipingen.progressivearchery.advancement.criterion.killed_by_bow;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;


public record KilledByTubularBowConditions(Optional<LootContextPredicate> player, List<LootContextPredicate> victims, NumberRange.IntRange uniqueEntityTypes) implements AbstractCriterion.Conditions {
    static final Identifier ID = KilledByTubularBowCriterion.ID;
    public static final Codec<KilledByTubularBowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(KilledByTubularBowConditions::player),
            EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(KilledByTubularBowConditions::victims),
            NumberRange.IntRange.CODEC.optionalFieldOf("unique_entity_types", NumberRange.IntRange.ANY).forGetter(KilledByTubularBowConditions::uniqueEntityTypes))
        .apply(instance, KilledByTubularBowConditions::new));


    public static AdvancementCriterion<KilledByTubularBowConditions> create(EntityPredicate.Builder ... victimPredicates) {
        return ModCriteria.KILLED_BY_TUBULAR_BOW.create(new KilledByTubularBowConditions(Optional.empty(),
                EntityPredicate.contextPredicateFromEntityPredicates(victimPredicates), NumberRange.IntRange.ANY));
    }

    public static AdvancementCriterion<KilledByTubularBowConditions> create(NumberRange.IntRange uniqueEntityTypes) {
        return ModCriteria.KILLED_BY_TUBULAR_BOW.create(new KilledByTubularBowConditions(Optional.empty(), List.of(), uniqueEntityTypes));
    }

    public boolean matches(Collection<LootContext> victimContexts, int uniqueEntityTypeCount) {
        if (!this.victims.isEmpty()) {
            List<LootContext> list = Lists.newArrayList(victimContexts);
            Iterator var4 = this.victims.iterator();

            while(var4.hasNext()) {
                LootContextPredicate lootContextPredicate = (LootContextPredicate)var4.next();
                boolean bl = false;
                Iterator<LootContext> iterator = list.iterator();

                while(iterator.hasNext()) {
                    LootContext lootContext = (LootContext)iterator.next();
                    if (lootContextPredicate.test(lootContext)) {
                        iterator.remove();
                        bl = true;
                        break;
                    }
                }

                if (!bl) {
                    return false;
                }
            }
        }

        return this.uniqueEntityTypes.test(uniqueEntityTypeCount);
    }

    public void validate(LootContextPredicateValidator validator) {
        AbstractCriterion.Conditions.super.validate(validator);
        validator.validateEntityPredicates(this.victims, ".victims");
    }

    public Optional<LootContextPredicate> player() {
        return this.player;
    }

    public List<LootContextPredicate> victims() {
        return this.victims;
    }

    public NumberRange.IntRange uniqueEntityTypes() {
        return this.uniqueEntityTypes;
    }


}
