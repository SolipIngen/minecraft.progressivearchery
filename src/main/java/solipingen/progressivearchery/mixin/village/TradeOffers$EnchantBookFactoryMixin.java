package solipingen.progressivearchery.mixin.village;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;


@Mixin(TradeOffers.EnchantBookFactory.class)
public abstract class TradeOffers$EnchantBookFactoryMixin implements TradeOffers.Factory {


    @ModifyVariable(method = "create", at = @At("STORE"), ordinal = 0)
    private Optional<RegistryEntry<Enchantment>> modifiedEnchantmentRegistryEntry(Optional<RegistryEntry<Enchantment>> optional) {
        optional.map(enchantmentEntry -> {
            if (enchantmentEntry.matchesKey(Enchantments.QUICK_CHARGE)) {
                return Optional.empty();
            }
            else {
                return optional;
            }
        });
        return optional;
    }

}
