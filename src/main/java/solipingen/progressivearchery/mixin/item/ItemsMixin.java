package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.ModBundleItem;
import solipingen.progressivearchery.item.arrows.SpectralArrowItem;


@Mixin(Items.class)
public abstract class ItemsMixin {
    
    
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("RETURN"), cancellable = true)
    private static void injectedRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cbireturn) {
        String name = id.getPath();
        int rawId = Item.getRawId(item);
        if (name.matches("spectral_arrow")) {
            Item newSpectralArrowItem = (Item)new SpectralArrowItem(new Item.Settings());
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSpectralArrowItem));
        }
        if (item instanceof PotionItem) {
            if (item instanceof SplashPotionItem) {
                Item newSplashPotionItem = (Item)new SplashPotionItem(new Item.Settings().maxCount(8));
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSplashPotionItem));
            }
            else if (item instanceof LingeringPotionItem) {
                Item newLingeringPotionItem = (Item)new LingeringPotionItem(new Item.Settings().maxCount(8));
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newLingeringPotionItem));
            }
            else {
                Item newPotionItem = (Item)new PotionItem(new Item.Settings().maxCount(16));
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newPotionItem));
            }
        }
        else if (item instanceof BundleItem) {
            Item newBundleItem = (Item)new ModBundleItem(new Item.Settings().maxCount(1));
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newBundleItem));
        }
    }


}
