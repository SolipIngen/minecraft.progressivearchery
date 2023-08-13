package solipingen.progressivearchery.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.MultishotEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;


@Mixin(MultishotEnchantment.class)
public abstract class MultishotEnchantmentMixin extends Enchantment {

    
    protected MultishotEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof RangedWeaponItem;
    }


}

