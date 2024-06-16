package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


public class WoodenArrowEntity extends ModArrowEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.WOODEN_ARROW);
    private static final double DAMAGE_AMOUNT = 2.0;


    public WoodenArrowEntity(EntityType<? extends WoodenArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world, DEFAULT_STACK);
    }

    public WoodenArrowEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.WOODEN_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z, stack);
    }

    public WoodenArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.WOODEN_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner, stack);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.WOODEN_ARROW);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            iLivingEntity.setStuckWoodenArrowCount(iLivingEntity.getStuckWoodenArrowCount() + 1);
        }
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.WOODEN_ARROW);
    }


}
