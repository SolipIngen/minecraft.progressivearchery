package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class DiamondKidArrowEntity extends KidArrowEntity {
    private static final double DAMAGE_AMOUNT = 5.0;

    
    public DiamondKidArrowEntity(EntityType<? extends DiamondKidArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public DiamondKidArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.DIAMOND_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public DiamondKidArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.DIAMOND_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.DIAMOND_KID_ARROW);
    }


}
