package solipingen.progressivearchery.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import solipingen.progressivearchery.item.QuiverItem;


@Mixin(InfinityEnchantment.class)
public class InfinityEnchantmentMixin extends Enchantment {

    protected InfinityEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
    
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(Items.BOW) || stack.getItem() instanceof QuiverItem;
    }
    

}
