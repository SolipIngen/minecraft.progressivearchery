package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class CopperArrowEntity extends ModArrowEntity {
    private static final double DAMAGE_AMOUNT = 3.0;

    
    public CopperArrowEntity(EntityType<? extends CopperArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public CopperArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.COPPER_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public CopperArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.COPPER_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.COPPER_ARROW);
    }

    
}
