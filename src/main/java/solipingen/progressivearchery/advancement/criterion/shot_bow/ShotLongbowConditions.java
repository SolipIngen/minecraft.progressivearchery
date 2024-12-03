package solipingen.progressivearchery.advancement.criterion.shot_bow;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;


public record ShotLongbowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = ShotLongbowCriterion.ID;
    public static final Codec<ShotLongbowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(ShotLongbowConditions::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ShotLongbowConditions::item))
        .apply(instance, ShotLongbowConditions::new));


    public static AdvancementCriterion<ShotLongbowConditions> create(Optional<ItemPredicate> itemPredicate) {
        return ModCriteria.SHOT_LONGBOW.create(new ShotLongbowConditions(Optional.empty(), itemPredicate));
    }

    public static AdvancementCriterion<ShotLongbowConditions> create(ItemConvertible item) {
        return ModCriteria.SHOT_LONGBOW.create(new ShotLongbowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build())));
    }

    public boolean matches(ItemStack stack) {
        return this.item.isEmpty() || (this.item.get()).test(stack);
    }

    public Optional<LootContextPredicate> player() {
        return this.player;
    }

    public Optional<ItemPredicate> item() {
        return this.item;
    }


}
