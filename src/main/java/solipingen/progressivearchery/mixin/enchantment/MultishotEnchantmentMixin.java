package solipingen.progressivearchery.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
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

    @ModifyConstant(method = "getMaxLevel", constant = @Constant(intValue = 1))
    private int modifiedGetMaxLevel(int originalint) {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof RangedWeaponItem;
    }


}

