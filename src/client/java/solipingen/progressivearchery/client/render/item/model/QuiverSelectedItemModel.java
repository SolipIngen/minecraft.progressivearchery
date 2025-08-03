package solipingen.progressivearchery.client.render.item.model;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.BundleSelectedItemModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.ResolvableModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.item.QuiverItem;


@Environment(EnvType.CLIENT)
public class QuiverSelectedItemModel implements ItemModel {
    static final ItemModel INSTANCE = new QuiverSelectedItemModel();


    public QuiverSelectedItemModel() {
    }

    @Override
    public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed) {
        state.addModelKey(this);
        ItemStack itemStack = QuiverItem.getSelectedStack(stack);
        if (!itemStack.isEmpty()) {
            resolver.update(state, itemStack, displayContext, world, user, seed);
        }
    }

    @Environment(EnvType.CLIENT)
    public static record Unbaked() implements ItemModel.Unbaked {
        public static final MapCodec<QuiverSelectedItemModel.Unbaked> CODEC = MapCodec.unit(new QuiverSelectedItemModel.Unbaked());

        public Unbaked() {
        }

        @Override
        public MapCodec<QuiverSelectedItemModel.Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public ItemModel bake(ItemModel.BakeContext context) {
            return QuiverSelectedItemModel.INSTANCE;
        }

        @Override
        public void resolve(ResolvableModel.Resolver resolver) {
        }
    }


}
