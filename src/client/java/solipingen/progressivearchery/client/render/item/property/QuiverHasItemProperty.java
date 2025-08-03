package solipingen.progressivearchery.client.render.item.property;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.item.QuiverItem;


@Environment(EnvType.CLIENT)
public record QuiverHasItemProperty() implements BooleanProperty {
    public static final MapCodec<QuiverHasItemProperty> CODEC = MapCodec.unit(new QuiverHasItemProperty());


    @Override
    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return QuiverItem.getAmountFilled(stack) > 0.0f;
    }

    @Override
    public MapCodec<QuiverHasItemProperty> getCodec() {
        return CODEC;
    }

}
