package solipingen.progressivearchery.mixin.loot.function;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Optional;


@Mixin(EnchantWithLevelsLootFunction.Builder.class)
public abstract class EnchantWithLevelsLootFunction$BuilderMixin extends ConditionalLootFunction.Builder<EnchantWithLevelsLootFunction.Builder> {
    @Shadow private Optional<RegistryEntryList<Enchantment>> options;


    @Inject(method = "build", at = @At("HEAD"), cancellable = true)
    private void injectedBuild(CallbackInfoReturnable<LootFunction> cbireturn) {
        if (this.options.isPresent()) {
            RegistryEntryList<Enchantment> enchantmentList = this.options.get();
            ArrayList<RegistryEntry<Enchantment>> replacementList = new ArrayList<>(enchantmentList.stream().toList());
            replacementList.removeIf(enchantmentEntry -> enchantmentEntry.matchesKey(Enchantments.QUICK_CHARGE));
            this.options = Optional.of(RegistryEntryList.of(replacementList));
        }
    }

}
