package solipingen.progressivearchery.item.arrows;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.arrow.CopperArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.DiamondArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.FlintArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.IronArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.ModArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.WoodenArrowEntity;
import solipingen.progressivearchery.item.ModItems;

public class ModArrowItem extends Item {

    
    public ModArrowItem(Item.Settings settings) {
        super(settings);
    }

    public ModArrowEntity createModArrow(World world, ItemStack stack, LivingEntity shooter) {
        ModArrowEntity arrowEntity = new WoodenArrowEntity(world, shooter);
        ((WoodenArrowEntity)arrowEntity).initFromStack(stack);
        if (stack.isOf(ModItems.FLINT_ARROW)) {
            arrowEntity = new FlintArrowEntity(world, shooter);
            ((FlintArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.COPPER_ARROW)) {
            arrowEntity = new CopperArrowEntity(world, shooter);
            ((CopperArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.GOLDEN_ARROW)) {
            arrowEntity = new GoldenArrowEntity(world, shooter);
            ((GoldenArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.IRON_ARROW)) {
            arrowEntity = new IronArrowEntity(world, shooter);
            ((IronArrowEntity)arrowEntity).initFromStack(stack);
        }
        else if (stack.isOf(ModItems.DIAMOND_ARROW)) {
            arrowEntity = new DiamondArrowEntity(world, shooter);
            ((DiamondArrowEntity)arrowEntity).initFromStack(stack);
        }
        return arrowEntity;
    }


}
