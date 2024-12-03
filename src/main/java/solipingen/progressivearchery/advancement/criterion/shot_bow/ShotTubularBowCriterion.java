package solipingen.progressivearchery.advancement.criterion.shot_bow;

import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ShotTubularBowCriterion extends AbstractCriterion<ShotTubularBowConditions> {
    public static final Identifier ID = Identifier.of(ProgressiveArchery.MOD_ID, "shot_tubular_bow");


    @Override
    public Codec<ShotTubularBowConditions> getConditionsCodec() {
        return ShotTubularBowConditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, (conditions) -> conditions.matches(stack));
    }
    
}
