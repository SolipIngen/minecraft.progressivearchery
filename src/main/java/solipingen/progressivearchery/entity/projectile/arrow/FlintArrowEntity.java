package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class FlintArrowEntity extends ModArrowEntity {
    private static final double DAMAGE_AMOUNT = 2.0;


    public FlintArrowEntity(EntityType<? extends FlintArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public FlintArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.FLINT_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public FlintArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.FLINT_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.FLINT_ARROW);
    }


}
