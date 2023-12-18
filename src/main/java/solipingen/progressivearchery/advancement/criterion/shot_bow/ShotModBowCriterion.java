package solipingen.progressivearchery.advancement.criterion.shot_bow;

import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ShotModBowCriterion extends AbstractCriterion<ShotModBowConditions> {
    public static final Identifier ID = new Identifier(ProgressiveArchery.MOD_ID, "shot_bow");


    @Override
    public Codec<ShotModBowConditions> getConditionsCodec() {
        return ShotModBowConditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, (conditions) -> conditions.matches(stack));
    }
    
}
