package solipingen.progressivearchery.mixin.enchantment;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;


@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {


    @Inject(method = "getPossibleEntries", at = @At("HEAD"), cancellable = true)
    private static void injectedGetPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cbireturn) {
        ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
        boolean bl = stack.isOf(Items.BOOK);
        block0: for (Enchantment enchantment : Registries.ENCHANTMENT) {
            if (enchantment.isTreasure() && !treasureAllowed || !enchantment.isAvailableForRandomSelection() || !(enchantment.isAcceptableItem(stack)) && !bl) continue;
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                if (power < enchantment.getMinPower(i) || power > enchantment.getMaxPower(i)) continue;
                list.add(new EnchantmentLevelEntry(enchantment, i));
                continue block0;
            }
        }
        cbireturn.setReturnValue(list);
    }
    
}
