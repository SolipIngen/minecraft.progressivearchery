package solipingen.progressivearchery.advancement.criterion.killed_by_bow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;


public record KilledByBlindedHornBowConditions(Optional<LootContextPredicate> player, List<LootContextPredicate> victims, NumberRange.IntRange uniqueEntityTypes) implements AbstractCriterion.Conditions {
    static final Identifier ID = KilledByBlindedHornBowCriterion.ID;
    public static final Codec<KilledByBlindedHornBowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(KilledByBlindedHornBowConditions::player), 
            Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.listOf(), "victims", List.of()).forGetter(KilledByBlindedHornBowConditions::victims), 
            Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "unique_entity_types", NumberRange.IntRange.ANY).forGetter(KilledByBlindedHornBowConditions::uniqueEntityTypes))
        .apply(instance, KilledByBlindedHornBowConditions::new));


    public static KilledByBlindedHornBowConditions create(EntityPredicate.Builder ... victimPredicates) {
        ArrayList<LootContextPredicate> lootContextPredicates = new ArrayList<LootContextPredicate>(victimPredicates.length);
        for (int i = 0; i < victimPredicates.length; ++i) {
            EntityPredicate.Builder builder = victimPredicates[i];
            lootContextPredicates.set(i, EntityPredicate.asLootContextPredicate(builder.build()));
        }
        return new KilledByBlindedHornBowConditions(Optional.empty(), lootContextPredicates, NumberRange.IntRange.ANY);
    }

    public static KilledByBlindedHornBowConditions create(NumberRange.IntRange uniqueEntityTypes) {
        ArrayList<LootContextPredicate> extendeds = new ArrayList<LootContextPredicate>();
        return new KilledByBlindedHornBowConditions(Optional.empty(), extendeds, uniqueEntityTypes);
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


}
