package solipingen.progressivearchery.advancement.criterion.fletcher;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;


public record FletchedArrowConditions(Optional<LootContextPredicate> player, Optional<RegistryEntry<Item>> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = FletchedArrowCriterion.ID;
    public static final Codec<FletchedArrowConditions> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(FletchedArrowConditions::player),
                        Registries.ITEM.getEntryCodec().optionalFieldOf("item").forGetter(FletchedArrowConditions::item))
                .apply(instance, FletchedArrowConditions::new);
            });


    public static AdvancementCriterion<FletchedArrowConditions> any() {
        return ModCriteria.FLETCHED_ARROW.create(new FletchedArrowConditions(Optional.empty(), Optional.empty()));
    }

    public boolean matches(Item otherItem) {
        return !item.isPresent() || item.get().equals(otherItem);
    }


}
