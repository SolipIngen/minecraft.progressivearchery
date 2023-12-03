package solipingen.progressivearchery.advancement.criterion.shot_bow;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;


public class ShotLongbowConditions extends AbstractCriterionConditions {
    static final Identifier ID = ShotLongbowCriterion.ID;
    private final Optional<ItemPredicate> item;

    public ShotLongbowConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) {
        super(player);
        this.item = item;
    }

    public static ShotLongbowConditions create(Optional<ItemPredicate> itemPredicate) {
        return new ShotLongbowConditions(Optional.empty(), itemPredicate);
    }

    public static ShotLongbowConditions create(ItemConvertible item) {
        return new ShotLongbowConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build()));
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
