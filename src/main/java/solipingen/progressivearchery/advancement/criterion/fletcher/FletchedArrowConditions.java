package solipingen.progressivearchery.advancement.criterion.fletcher;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;


public class FletchedArrowConditions extends AbstractCriterionConditions {
    static final Identifier ID = FletchedArrowCriterion.ID;
    @Nullable private final Item item;


    public FletchedArrowConditions(Optional<LootContextPredicate> player, @Nullable Item item) {
        super(player);
        this.item = item;
    }

    public static AdvancementCriterion<FletchedArrowConditions> any() {
        return ModCriteria.FLETCHED_ARROW.create(new FletchedArrowConditions(Optional.empty(), null));
    }

    public boolean matches(Item item) {
        return this.item == null || this.item == item;
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = super.toJson();
        if (this.item != null) {
            jsonObject.addProperty("item", Registries.ITEM.getId(this.item).toString());
        }
        return jsonObject;
    }

}
