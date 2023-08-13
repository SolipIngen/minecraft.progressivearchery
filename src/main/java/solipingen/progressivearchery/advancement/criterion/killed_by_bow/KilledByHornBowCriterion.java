package solipingen.progressivearchery.advancement.criterion.killed_by_bow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class KilledByHornBowCriterion extends AbstractCriterion<KilledByHornBowConditions> {
    static final Identifier ID = new Identifier(ProgressiveArchery.MOD_ID, "killed_by_horn_bow");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public KilledByHornBowConditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        LootContextPredicate[] lootContextPredicates = EntityPredicate.contextPredicateArrayFromJson(jsonObject, "victims", advancementEntityPredicateDeserializer);
        NumberRange.IntRange intRange = NumberRange.IntRange.fromJson(jsonObject.get("unique_entity_types"));
        return new KilledByHornBowConditions(extended, lootContextPredicates, intRange);
    }

    public void trigger(ServerPlayerEntity player, Collection<Entity> killedEntities) {
        ArrayList<LootContext> list = Lists.newArrayList();
        HashSet<EntityType<?>> set = Sets.newHashSet();
        for (Entity entity : killedEntities) {
            set.add(entity.getType());
            list.add(EntityPredicate.createAdvancementEntityLootContext(player, entity));
        }
        this.trigger(player, (conditions) -> conditions.matches(list, set.size()));
    }

}
