package solipingen.progressivearchery.mixin.client.render.item;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModCrossbowItem;


@Mixin(HeldItemRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class HeldItemRendererMixin {
    @Shadow @Final MinecraftClient client;
    @Shadow @Final ItemRenderer itemRenderer;


    @ModifyVariable(method = "getHandRenderType", at = @At("STORE"), ordinal = 0)
    private static boolean modifiedGetHandRenderType2(boolean originalBl, ClientPlayerEntity player) {
        ItemStack itemStack = player.getMainHandStack();
        ItemStack itemStack2 = player.getOffHandStack();
        boolean bl2 = itemStack.getItem() instanceof CrossbowItem || itemStack2.getItem() instanceof CrossbowItem || itemStack.getItem() instanceof ModCrossbowItem || itemStack2.getItem() instanceof ModCrossbowItem;
        return bl2;
    }
    
    @ModifyVariable(method = "getHandRenderType", at = @At("STORE"), ordinal = 1)
    private static boolean modifiedGetHandRenderType(boolean originalBl, ClientPlayerEntity player) {
        ItemStack itemStack = player.getMainHandStack();
        ItemStack itemStack2 = player.getOffHandStack();
        boolean bl = itemStack.getItem() instanceof BowItem || itemStack2.getItem() instanceof BowItem || itemStack.getItem() instanceof ModBowItem || itemStack2.getItem() instanceof ModBowItem;
        return bl;
    }

    @Inject(method = "isChargedCrossbow", at = @At("HEAD"), cancellable = true)
    private static void injectedIsChargedCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (stack.getItem() instanceof ModCrossbowItem) {
            cbireturn.setReturnValue(stack.getItem() instanceof ModCrossbowItem && ModCrossbowItem.isCharged(stack));
        }
    }

    @Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedIsOf(ItemStack originalItemStack, Item originalItem) {
        if (originalItem == Items.CROSSBOW) {
            return originalItemStack.getItem() instanceof CrossbowItem || originalItemStack.getItem() instanceof ModCrossbowItem;
        }
        else {
            return originalItemStack.isOf(originalItem);
        }
    }

    @ModifyArg(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V"), index = 1, 
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;isCharged(Lnet/minecraft/item/ItemStack;)Z"), to = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I")))
    private Arm modifiedArm(Arm originalArm) {
        return originalArm.getOpposite();
    }

    @ModifyArg(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"), index = 0, 
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;isCharged(Lnet/minecraft/item/ItemStack;)Z"), to = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I")))
    private float modifiedXTranslate(float originalXTranslate) {
        return -1.0f*originalXTranslate;
    }

    @ModifyConstant(method = "renderFirstPersonItem", constant = @Constant(floatValue = 65.3f), 
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;isCharged(Lnet/minecraft/item/ItemStack;)Z"), to = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I")))
    private float modifiedYRotation(float originalYRotation) {
        return 90.0f - originalYRotation;
    }


}
