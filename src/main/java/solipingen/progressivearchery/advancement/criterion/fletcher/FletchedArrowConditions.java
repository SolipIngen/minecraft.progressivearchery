package solipingen.progressivearchery.advancement.criterion.fletcher;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;


public class FletchedArrowConditions extends AbstractCriterionConditions {
    static final Identifier ID = FletchedArrowCriterion.ID;
    
    
    @Nullable
    private final Item item;

    public FletchedArrowConditions(EntityPredicate.Extended player, @Nullable Item item) {
        super(ID, player);
        this.item = item;
    }

    public static FletchedArrowConditions any() {
        return new FletchedArrowConditions(EntityPredicate.Extended.EMPTY, null);
    }

    public boolean matches(Item item) {
        return this.item == null || this.item == item;
    }

    @Override
    public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
        JsonObject jsonObject = super.toJson(predicateSerializer);
        if (this.item != null) {
            jsonObject.addProperty("item", Registries.ITEM.getId(this.item).toString());
        }
        return jsonObject;
    }
}
