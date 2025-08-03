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


public record ShotHornBowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = ShotHornBowCriterion.ID;
    public static final Codec<ShotHornBowConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(ShotHornBowConditions::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ShotHornBowConditions::item))
        .apply(instance, ShotHornBowConditions::new));


    public static AdvancementCriterion<ShotHornBowConditions> create(Optional<ItemPredicate> itemPredicate) {
        return ModCriteria.SHOT_HORN_BOW.create(new ShotHornBowConditions(Optional.empty(), itemPredicate));
    }

    public static AdvancementCriterion<ShotHornBowConditions> create(RegistryEntryLookup<Item> itemRegistry, ItemConvertible item) {
        return ModCriteria.SHOT_HORN_BOW.create(new ShotHornBowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(itemRegistry, item).build())));
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
