package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.ModBundleItem;


@Mixin(Items.class)
public abstract class ItemsMixin {
    
    
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("TAIL"), cancellable = true)
    private static void injectedRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cbireturn) {
        if (item instanceof BundleItem) {
            Item newBundleItem = (Item)new ModBundleItem(new Item.Settings().maxCount(1));
            int bundleRawId = Item.getRawId(item);
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, bundleRawId, "bundle", newBundleItem));
        }
    }
    

}
