package solipingen.progressivearchery.advancement.criterion.fletcher;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import solipingen.progressivearchery.ProgressiveArchery;


public class FletchedArrowCriterion extends AbstractCriterion<FletchedArrowConditions> {
    static final Identifier ID = new Identifier(ProgressiveArchery.MOD_ID, "fletched_arrow");

    
    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public FletchedArrowConditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        Item item = null;
        if (jsonObject.has("item")) {
            Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "item"));
            item = (Item)Registries.ITEM.getOrEmpty(identifier).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + identifier + "'"));
        }
        return new FletchedArrowConditions(lootContextPredicate, item);
    }

    public void trigger(ServerPlayerEntity player, Item item) {
        this.trigger(player, (conditions) -> conditions.matches(item));
    }
}
