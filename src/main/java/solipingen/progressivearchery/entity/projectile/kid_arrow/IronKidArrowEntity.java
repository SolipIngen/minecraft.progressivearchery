package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class IronKidArrowEntity extends KidArrowEntity {
    private static final double DAMAGE_AMOUNT = 4.0;

    
    public IronKidArrowEntity(EntityType<? extends IronKidArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public IronKidArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.IRON_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public IronKidArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.IRON_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.IRON_KID_ARROW);
    }


}
