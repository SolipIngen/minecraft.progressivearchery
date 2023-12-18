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


public record ShotModBowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = ShotModBowCriterion.ID;
    public static final Codec<ShotModBowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(ShotModBowConditions::player), 
            Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "item").forGetter(ShotModBowConditions::item))
        .apply(instance, ShotModBowConditions::new));



    public static ShotModBowConditions create(Optional<ItemPredicate> itemPredicate) {
        return new ShotModBowConditions(Optional.empty(), itemPredicate);
    }

    public static ShotModBowConditions create(ItemConvertible item) {
        return new ShotModBowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build()));
    }

    public boolean matches(ItemStack stack) {
        return this.item.isPresent() ? this.item.get().test(stack) : false;
    }


}
