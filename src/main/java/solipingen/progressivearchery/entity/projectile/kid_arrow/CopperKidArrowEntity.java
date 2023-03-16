package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class CopperKidArrowEntity extends KidArrowEntity {
    private static final double DAMAGE_AMOUNT = 3.0;

    
    public CopperKidArrowEntity(EntityType<? extends CopperKidArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public CopperKidArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.COPPER_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public CopperKidArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.COPPER_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.COPPER_KID_ARROW);
    }

    
}
