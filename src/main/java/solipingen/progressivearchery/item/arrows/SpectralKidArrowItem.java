package solipingen.progressivearchery.item.arrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.SpectralKidArrowEntity;


public class SpectralKidArrowItem extends KidArrowItem {


    public SpectralKidArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public KidArrowEntity createKidArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new SpectralKidArrowEntity(world, shooter, stack.copyWithCount(1));
    }


}

