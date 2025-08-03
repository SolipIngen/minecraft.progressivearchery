package solipingen.progressivearchery.advancement.criterion.shot_bow;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;


public record ShotTubularBowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = ShotTubularBowCriterion.ID;
    public static final Codec<ShotTubularBowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(ShotTubularBowConditions::player),
           ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ShotTubularBowConditions::item))
        .apply(instance, ShotTubularBowConditions::new));


    public static AdvancementCriterion<ShotTubularBowConditions> create(Optional<ItemPredicate> itemPredicate) {
        return ModCriteria.SHOT_TUBULAR_BOW.create(new ShotTubularBowConditions(Optional.empty(), itemPredicate));
    }

    public static AdvancementCriterion<ShotTubularBowConditions> create(RegistryEntryLookup<Item> itemRegistry, ItemConvertible item) {
        return ModCriteria.SHOT_TUBULAR_BOW.create(new ShotTubularBowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(itemRegistry, item).build())));
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
