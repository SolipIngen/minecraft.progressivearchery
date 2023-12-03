package solipingen.progressivearchery.advancement.criterion.shot_bow;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;


public class ShotModBowConditions extends AbstractCriterionConditions {
    static final Identifier ID = ShotModBowCriterion.ID;
    private final Optional<ItemPredicate> item;

    public ShotModBowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) {
        super(player);
        this.item = item;
    }

    public static ShotModBowConditions create(Optional<ItemPredicate> itemPredicate) {
        return new ShotModBowConditions(Optional.empty(), itemPredicate);
    }

    public static ShotModBowConditions create(ItemConvertible item) {
        return new ShotModBowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build()));
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
