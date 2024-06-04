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
import solipingen.progressivearchery.entity.projectile.arrow.CopperArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.DiamondArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.FlintArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.IronArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.ModArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.WoodenArrowEntity;
import solipingen.progressivearchery.item.ModItems;


public class ModArrowItem extends Item implements ProjectileItem {

    
    public ModArrowItem(Item.Settings settings) {
        super(settings);
    }

    public ModArrowEntity createModArrow(World world, ItemStack stack, LivingEntity shooter) {
        ModArrowEntity arrowEntity = new WoodenArrowEntity(world, shooter, stack.copyWithCount(1));
        ((WoodenArrowEntity)arrowEntity).initFromStack(stack);
        if (stack.isOf(ModItems.FLINT_ARROW)) {
            arrowEntity = new FlintArrowEntity(world, shooter, stack.copyWithCount(1));
            ((FlintArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.COPPER_ARROW)) {
            arrowEntity = new CopperArrowEntity(world, shooter, stack.copyWithCount(1));
            ((CopperArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.GOLDEN_ARROW)) {
            arrowEntity = new GoldenArrowEntity(world, shooter, stack.copyWithCount(1));
            ((GoldenArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.IRON_ARROW)) {
            arrowEntity = new IronArrowEntity(world, shooter, stack.copyWithCount(1));
            ((IronArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.DIAMOND_ARROW)) {
            arrowEntity = new DiamondArrowEntity(world, shooter, stack.copyWithCount(1));
            ((DiamondArrowEntity)arrowEntity).initFromStack(stack);
        }
        return arrowEntity;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ModArrowEntity arrowEntity = new WoodenArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
        ((WoodenArrowEntity)arrowEntity).initFromStack(stack);
        if (stack.isOf(ModItems.FLINT_ARROW)) {
            arrowEntity = new FlintArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
            ((FlintArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.COPPER_ARROW)) {
            arrowEntity = new CopperArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
            ((CopperArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.GOLDEN_ARROW)) {
            arrowEntity = new GoldenArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
            ((GoldenArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.IRON_ARROW)) {
            arrowEntity = new IronArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
            ((IronArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.DIAMOND_ARROW)) {
            arrowEntity = new DiamondArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
            ((DiamondArrowEntity)arrowEntity).initFromStack(stack);
        }
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrowEntity;
    }


}
