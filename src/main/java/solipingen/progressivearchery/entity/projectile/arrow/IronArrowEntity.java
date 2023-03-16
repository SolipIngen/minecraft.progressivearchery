package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class IronArrowEntity extends ModArrowEntity {
    private static final double DAMAGE_AMOUNT = 4.0;

    
    public IronArrowEntity(EntityType<? extends IronArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public IronArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.IRON_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public IronArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.IRON_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.IRON_ARROW);
    }

    
}
