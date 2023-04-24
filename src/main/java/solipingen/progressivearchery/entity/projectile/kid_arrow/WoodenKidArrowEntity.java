package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


public class WoodenKidArrowEntity extends KidArrowEntity {
    private static final double DAMAGE_AMOUNT = 1.5;

    
    public WoodenKidArrowEntity(EntityType<? extends WoodenKidArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public WoodenKidArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.WOODEN_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
    }

    public WoodenKidArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.WOODEN_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.WOODEN_KID_ARROW);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            iLivingEntity.setStuckWoodenKidArrowCount(iLivingEntity.getStuckWoodenKidArrowCount() + 1);
        }
    }


}

