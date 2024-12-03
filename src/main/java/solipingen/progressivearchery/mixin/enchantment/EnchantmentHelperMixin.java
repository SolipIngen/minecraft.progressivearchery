package solipingen.progressivearchery.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.stream.Stream;


@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {


    @ModifyVariable(method = "generateEnchantments", at = @At("HEAD"), index = 3)
    private static Stream<RegistryEntry<Enchantment>> modifiedPossibleEnchantments(Stream<RegistryEntry<Enchantment>> possibleEnchantments) {
        return possibleEnchantments.filter(enchantmentEntry -> !(enchantmentEntry.matchesKey(Enchantments.INFINITY) || enchantmentEntry.matchesKey(Enchantments.QUICK_CHARGE)));
    }



}
