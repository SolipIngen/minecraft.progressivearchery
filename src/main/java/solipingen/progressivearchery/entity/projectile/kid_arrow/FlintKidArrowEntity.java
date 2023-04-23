package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class FlintKidArrowEntity extends KidArrowEntity {
    private static final double DAMAGE_AMOUNT = 2.5;

    
    public FlintKidArrowEntity(EntityType<? extends FlintKidArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public FlintKidArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.FLINT_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public FlintKidArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.FLINT_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.FLINT_KID_ARROW);
    }

    
}
