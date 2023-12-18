package solipingen.progressivearchery.advancement.criterion.shot_bow;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;


public record ShotHornBowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = ShotHornBowCriterion.ID;
    public static final Codec<ShotHornBowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(ShotHornBowConditions::player), 
            Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "item").forGetter(ShotHornBowConditions::item))
        .apply(instance, ShotHornBowConditions::new));


    public static ShotHornBowConditions create(Optional<ItemPredicate> itemPredicate) {
        return new ShotHornBowConditions(Optional.empty(), itemPredicate);
    }

    public static ShotHornBowConditions create(ItemConvertible item) {
        return new ShotHornBowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build()));
    }

    public boolean matches(ItemStack stack) {
        return this.item.isPresent() ? this.item.get().test(stack) : false;
    }


}
