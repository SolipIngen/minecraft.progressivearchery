package solipingen.progressivearchery.item.arrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.entity.projectile.kid_arrow.CopperKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.DiamondKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.FlintKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.IronKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.WoodenKidArrowEntity;
import solipingen.progressivearchery.item.ModItems;


public class KidArrowItem extends Item implements ProjectileItem {


    public KidArrowItem(Item.Settings settings) {
        super(settings);
    }

    public KidArrowEntity createKidArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        KidArrowEntity kidArrowEntity = new WoodenKidArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
        ((WoodenKidArrowEntity)kidArrowEntity).initFromStack(stack);
        if (stack.isOf(ModItems.FLINT_KID_ARROW)) {
            kidArrowEntity = new FlintKidArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
            ((FlintKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.COPPER_KID_ARROW)) {
            kidArrowEntity = new CopperKidArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
            ((CopperKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.GOLDEN_KID_ARROW) || stack.isOf(ModItems.TIPPED_KID_ARROW)) {
            kidArrowEntity = new GoldenKidArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
            ((GoldenKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.IRON_KID_ARROW)) {
            kidArrowEntity = new IronKidArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
            ((IronKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.DIAMOND_KID_ARROW)) {
            kidArrowEntity = new DiamondKidArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
            ((DiamondKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        return kidArrowEntity;
    }


    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        KidArrowEntity kidArrowEntity = new WoodenKidArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        ((WoodenKidArrowEntity)kidArrowEntity).initFromStack(stack);
        if (stack.isOf(ModItems.FLINT_KID_ARROW)) {
            kidArrowEntity = new FlintKidArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
            ((FlintKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.COPPER_KID_ARROW)) {
            kidArrowEntity = new CopperKidArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
            ((CopperKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.GOLDEN_KID_ARROW) || stack.isOf(ModItems.TIPPED_KID_ARROW)) {
            kidArrowEntity = new GoldenKidArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
            ((GoldenKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.IRON_KID_ARROW)) {
            kidArrowEntity = new IronKidArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
            ((IronKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.DIAMOND_KID_ARROW)) {
            kidArrowEntity = new DiamondKidArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
            ((DiamondKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        return kidArrowEntity;
    }
}
