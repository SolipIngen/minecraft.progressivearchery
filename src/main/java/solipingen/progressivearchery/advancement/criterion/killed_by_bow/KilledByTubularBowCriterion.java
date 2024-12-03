package solipingen.progressivearchery.advancement.criterion.killed_by_bow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class KilledByTubularBowCriterion extends AbstractCriterion<KilledByTubularBowConditions> {
    public static final Identifier ID = Identifier.of(ProgressiveArchery.MOD_ID, "killed_by_tubular_bow");


    @Override
    public Codec<KilledByTubularBowConditions> getConditionsCodec() {
        return KilledByTubularBowConditions.CODEC;
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
