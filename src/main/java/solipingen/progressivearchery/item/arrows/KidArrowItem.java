package solipingen.progressivearchery.item.arrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.kid_arrow.CopperKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.DiamondKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.FlintKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.IronKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.WoodenKidArrowEntity;
import solipingen.progressivearchery.item.ModItems;

public class KidArrowItem extends Item {


    public KidArrowItem(Item.Settings settings) {
        super(settings);
    }

    public KidArrowEntity createKidArrow(World world, ItemStack stack, LivingEntity shooter) {
        KidArrowEntity kidArrowEntity = new WoodenKidArrowEntity(world, shooter);
        ((WoodenKidArrowEntity)kidArrowEntity).initFromStack(stack);
        if (stack.isOf(ModItems.FLINT_KID_ARROW)) {
            kidArrowEntity = new FlintKidArrowEntity(world, shooter);
            ((FlintKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.COPPER_KID_ARROW)) {
            kidArrowEntity = new CopperKidArrowEntity(world, shooter);
            ((CopperKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.GOLDEN_KID_ARROW) || stack.isOf(ModItems.TIPPED_KID_ARROW)) {
            kidArrowEntity = new GoldenKidArrowEntity(world, shooter);
            ((GoldenKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.IRON_KID_ARROW)) {
            kidArrowEntity = new IronKidArrowEntity(world, shooter);
            ((IronKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.DIAMOND_KID_ARROW)) {
            kidArrowEntity = new DiamondKidArrowEntity(world, shooter);
            ((DiamondKidArrowEntity)kidArrowEntity).initFromStack(stack);
        }
        return kidArrowEntity;
    }

    
}
