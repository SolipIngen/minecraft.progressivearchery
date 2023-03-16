package solipingen.progressivearchery.advancement.criterion.shot_bow;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;


public class ShotLongbowConditions extends AbstractCriterionConditions {
    static final Identifier ID = ShotLongbowCriterion.ID;
    private final ItemPredicate item;

    public ShotLongbowConditions(EntityPredicate.Extended player, ItemPredicate item) {
        super(ID, player);
        this.item = item;
    }

    public static ShotLongbowConditions create(ItemPredicate itemPredicate) {
        return new ShotLongbowConditions(EntityPredicate.Extended.EMPTY, itemPredicate);
    }

    public static ShotLongbowConditions create(ItemConvertible item) {
        return new ShotLongbowConditions(EntityPredicate.Extended.EMPTY, ItemPredicate.Builder.create().items(item).build());
    }

    public boolean matches(ItemStack stack) {
        return this.item.test(stack);
    }

    @Override
    public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
        JsonObject jsonObject = super.toJson(predicateSerializer);
        jsonObject.add("item", this.item.toJson());
        return jsonObject;
    }

}
