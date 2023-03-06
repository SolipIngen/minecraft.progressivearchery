package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;


public class DiamondArrowEntity extends ModArrowEntity {
    private static final double DAMAGE_AMOUNT = 5.0;

    
    public DiamondArrowEntity(EntityType<? extends DiamondArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public DiamondArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.DIAMOND_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public DiamondArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.DIAMOND_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.DIAMOND_ARROW);
    }

    
}
