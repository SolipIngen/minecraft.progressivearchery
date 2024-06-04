package solipingen.progressivearchery.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.item.ItemStack;
import solipingen.progressivearchery.item.QuiverItem;


@Mixin(InfinityEnchantment.class)
public class InfinityEnchantmentMixin extends Enchantment {

    protected InfinityEnchantmentMixin(Enchantment.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
    
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof QuiverItem;
    }
    

}
