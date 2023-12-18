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


public record ShotLongbowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = ShotLongbowCriterion.ID;
    public static final Codec<ShotLongbowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(ShotLongbowConditions::player), 
            Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "item").forGetter(ShotLongbowConditions::item))
        .apply(instance, ShotLongbowConditions::new));


    public static ShotLongbowConditions create(Optional<ItemPredicate> itemPredicate) {
        return new ShotLongbowConditions(Optional.empty(), itemPredicate);
    }

    public static ShotLongbowConditions create(ItemConvertible item) {
        return new ShotLongbowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build()));
    }

    public boolean matches(ItemStack stack) {
        return this.item.isPresent() ? this.item.get().test(stack) : false;
    }


}
