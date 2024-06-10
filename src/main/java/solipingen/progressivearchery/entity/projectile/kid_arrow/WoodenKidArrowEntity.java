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
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.WOODEN_KID_ARROW);
    private static final double DAMAGE_AMOUNT = 2.0;

    
    public WoodenKidArrowEntity(EntityType<? extends WoodenKidArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world, DEFAULT_STACK);
    }

    public WoodenKidArrowEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.WOODEN_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z, stack);
    }

    public WoodenKidArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.WOODEN_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner, stack);
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

    @Override
    public ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.WOODEN_KID_ARROW);
    }


}

