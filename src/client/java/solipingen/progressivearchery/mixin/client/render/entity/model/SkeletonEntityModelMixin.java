package solipingen.progressivearchery.mixin.client.render.entity.model;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


@Mixin(SkeletonEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class SkeletonEntityModelMixin<T extends MobEntity> extends BipedEntityModel<T> {


    public SkeletonEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Redirect(method = "animateModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedAnimateModel(ItemStack mainHandStack, Item originalItem) {
        return mainHandStack.getItem() instanceof BowItem;
    }

    @Redirect(method = "setAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedSetAngles(ItemStack mainHandStack, Item originalItem) {
        return mainHandStack.getItem() instanceof BowItem;
    }
    

    
}
