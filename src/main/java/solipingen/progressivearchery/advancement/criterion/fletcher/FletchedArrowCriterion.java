package solipingen.progressivearchery.advancement.criterion.fletcher;

import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class FletchedArrowCriterion extends AbstractCriterion<FletchedArrowConditions> {
    public static final Identifier ID = Identifier.of(ProgressiveArchery.MOD_ID, "fletched_arrow");


    @Override
    public Codec<FletchedArrowConditions> getConditionsCodec() {
        return FletchedArrowConditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Item item) {
        this.trigger(player, (conditions) -> conditions.matches(item));
    }


}
