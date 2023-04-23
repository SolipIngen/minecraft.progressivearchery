package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class WoodenArrowEntity extends ModArrowEntity {
    private static final double DAMAGE_AMOUNT = 1.5;


    public WoodenArrowEntity(EntityType<? extends WoodenArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public WoodenArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.WOODEN_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public WoodenArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.WOODEN_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.WOODEN_ARROW);
    }


}
