package solipingen.progressivearchery.item.arrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.SpectralKidArrowEntity;


public class SpectralKidArrowItem extends KidArrowItem implements ProjectileItem {


    public SpectralKidArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public KidArrowEntity createKidArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new SpectralKidArrowEntity(world, shooter, stack.copyWithCount(1));
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        SpectralKidArrowEntity arrowEntity = new SpectralKidArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrowEntity;
    }


}

