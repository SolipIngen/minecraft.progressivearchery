package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


public class FlintKidArrowEntity extends KidArrowEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.FLINT_KID_ARROW);
    private static final double DAMAGE_AMOUNT = 2.5;

    
    public FlintKidArrowEntity(EntityType<? extends FlintKidArrowEntity> entityType, World world) {
        super(entityType, world, DAMAGE_AMOUNT);
    }

    public FlintKidArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.FLINT_KID_ARROW_ENTITY, world, x, y, z, stack, shotFrom, DAMAGE_AMOUNT);
    }

    public FlintKidArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.FLINT_KID_ARROW_ENTITY,world, owner, stack, shotFrom, DAMAGE_AMOUNT);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.FLINT_KID_ARROW);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            iLivingEntity.setStuckFlintKidArrowCount(iLivingEntity.getStuckFlintKidArrowCount() + 1);
        }
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }

    
}
