package solipingen.progressivearchery.client.render.item.property;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.render.item.property.numeric.UseDurationProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.item.ModBowItem;


@Environment(EnvType.CLIENT)
public class BowPullProperty implements NumericProperty {
    public static final MapCodec<BowPullProperty> CODEC = MapCodec.unit(new BowPullProperty());


    public BowPullProperty() {
    }

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        if (holder != null) {
            if (stack.getItem() instanceof ModBowItem modBowItem) {
                return modBowItem.getPullProgress(UseDurationProperty.getTicksUsedSoFar(stack, holder), stack, holder.getWorld());
            }
            else if (stack.getItem() instanceof BowItem) {
                return ModBowItem.getVanillaBowPullProgress(UseDurationProperty.getTicksUsedSoFar(stack, holder), stack, holder.getWorld());
            }
        }
        return 0.0f;
    }

    @Override
    public MapCodec<BowPullProperty> getCodec() {
        return CODEC;
    }



}
