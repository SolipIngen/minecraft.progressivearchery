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
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;


public record FletchedArrowConditions(Optional<LootContextPredicate> player, Optional<Item> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = FletchedArrowCriterion.ID;
    public static final Codec<FletchedArrowConditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(FletchedArrowConditions::player), 
        Codecs.createStrictOptionalFieldCodec(Registries.ITEM.getCodec(), "item").forGetter(FletchedArrowConditions::item))
    .apply(instance, FletchedArrowConditions::new));


    public static AdvancementCriterion<FletchedArrowConditions> any() {
        return ModCriteria.FLETCHED_ARROW.create(new FletchedArrowConditions(Optional.empty(), Optional.empty()));
    }

    public boolean matches(Item otherItem) {
        return !item.isPresent() || item.get().equals(otherItem);
    }


}
