package solipingen.progressivearchery.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Lifecycle;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BowItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.BowMaterials;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModBundleItem;
import solipingen.progressivearchery.item.ModCrossbowItem;


@Mixin(Items.class)
public abstract class ItemsMixin {
    

    @SuppressWarnings("all")
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("RETURN"), cancellable = true)
    private static void injectedRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cbireturn) {
        String name = id.getPath();
        int rawId = item.getRawId(item);
        if (item instanceof BowItem) {
            Item woodenBowItem = (Item)new ModBowItem(BowMaterials.WOOD, 0, new FabricItemSettings());
            cbireturn.setReturnValue(((SimpleRegistry<Item>)Registries.ITEM).set(rawId, RegistryKey.of(Registries.ITEM.getKey(), id), woodenBowItem, Lifecycle.stable()).value());
        }
        else if (item instanceof CrossbowItem) {
            Item woodenCrossbowItem = (Item)new ModCrossbowItem(BowMaterials.WOOD, new FabricItemSettings());
            cbireturn.setReturnValue(((SimpleRegistry<Item>)Registries.ITEM).set(rawId, RegistryKey.of(Registries.ITEM.getKey(), id), woodenCrossbowItem, Lifecycle.stable()).value());
        }
        else if (item instanceof PotionItem) {
            if (item instanceof SplashPotionItem) {
                Item newSplashPotionItem = (Item)new SplashPotionItem(new Item.Settings().maxCount(8));
                cbireturn.setReturnValue(((SimpleRegistry<Item>)Registries.ITEM).set(rawId, RegistryKey.of(Registries.ITEM.getKey(), id), newSplashPotionItem, Lifecycle.stable()).value());
            }
            else if (item instanceof LingeringPotionItem) {
                Item newLingeringPotionItem = (Item)new LingeringPotionItem(new Item.Settings().maxCount(8));
                cbireturn.setReturnValue(((SimpleRegistry<Item>)Registries.ITEM).set(rawId, RegistryKey.of(Registries.ITEM.getKey(), id), newLingeringPotionItem, Lifecycle.stable()).value());
            }
            else {
                Item newPotionItem = (Item)new PotionItem(new Item.Settings().maxCount(16));
                cbireturn.setReturnValue(((SimpleRegistry<Item>)Registries.ITEM).set(rawId, RegistryKey.of(Registries.ITEM.getKey(), id), newPotionItem, Lifecycle.stable()).value());
            }
        }
        else if (item instanceof BundleItem) {
            Item newBundleItem = (Item)new ModBundleItem(new Item.Settings().maxCount(1));
            cbireturn.setReturnValue(((SimpleRegistry<Item>)Registries.ITEM).set(rawId, RegistryKey.of(Registries.ITEM.getKey(), id), newBundleItem, Lifecycle.stable()).value());
        }
        if (name.matches("magma_cream")) {
            Item newMagmaCreamItem = (Item)new Item(new Item.Settings().fireproof());
            cbireturn.setReturnValue(((SimpleRegistry<Item>)Registries.ITEM).set(rawId, RegistryKey.of(Registries.ITEM.getKey(), id), newMagmaCreamItem, Lifecycle.stable()).value());
        }
    }


}
