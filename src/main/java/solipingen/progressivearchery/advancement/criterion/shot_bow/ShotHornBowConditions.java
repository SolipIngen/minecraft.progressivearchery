package solipingen.progressivearchery.advancement.criterion.shot_bow;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;


public class ShotHornBowConditions extends AbstractCriterionConditions {
    static final Identifier ID = ShotHornBowCriterion.ID;
    private final Optional<ItemPredicate> item;

    public ShotHornBowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) {
        super(player);
        this.item = item;
    }

    public static ShotHornBowConditions create(Optional<ItemPredicate> itemPredicate) {
        return new ShotHornBowConditions(Optional.empty(), itemPredicate);
    }

    public static ShotHornBowConditions create(ItemConvertible item) {
        return new ShotHornBowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build()));
    }

    public boolean matches(ItemStack stack) {
        return this.item.isPresent() ? this.item.get().test(stack) : false;
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = super.toJson();
        this.item.ifPresent(itemPredicate -> jsonObject.add("item", itemPredicate.toJson()));
        return jsonObject;
    }

}
