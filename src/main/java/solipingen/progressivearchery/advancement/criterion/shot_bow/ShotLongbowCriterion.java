package solipingen.progressivearchery.advancement.criterion.shot_bow;

import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ShotLongbowCriterion extends AbstractCriterion<ShotLongbowConditions> {
    public static final Identifier ID = new Identifier(ProgressiveArchery.MOD_ID, "shot_longbow");


    @Override
    public Codec<ShotLongbowConditions> getConditionsCodec() {
        return ShotLongbowConditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, (conditions) -> conditions.matches(stack));
    }
    
}
