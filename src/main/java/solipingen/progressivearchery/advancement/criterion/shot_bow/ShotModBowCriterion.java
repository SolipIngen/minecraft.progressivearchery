package solipingen.progressivearchery.advancement.criterion.shot_bow;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ShotModBowCriterion extends AbstractCriterion<ShotModBowConditions> {
    static final Identifier ID = new Identifier(ProgressiveArchery.MOD_ID, "shot_bow");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public ShotModBowConditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        return new ShotModBowConditions(extended, itemPredicate);
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, (conditions) -> conditions.matches(stack));
    }
    
}
