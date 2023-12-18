package solipingen.progressivearchery.item.arrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.arrow.ModArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.SpectralArrowEntity;


public class SpectralArrowItem extends ModArrowItem {


    public SpectralArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ModArrowEntity createModArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new SpectralArrowEntity(world, shooter, stack.copyWithCount(1));
    }

    
}


