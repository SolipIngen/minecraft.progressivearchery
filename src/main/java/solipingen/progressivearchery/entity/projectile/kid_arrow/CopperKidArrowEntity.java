package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


public class CopperKidArrowEntity extends KidArrowEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.COPPER_KID_ARROW);
    private static final double DAMAGE_AMOUNT = 3.0;

    
    public CopperKidArrowEntity(EntityType<? extends CopperKidArrowEntity> entityType, World world) {
        super(entityType, world, DAMAGE_AMOUNT);
    }

    public CopperKidArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.COPPER_KID_ARROW_ENTITY, world, x, y, z, stack, shotFrom, DAMAGE_AMOUNT);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    public CopperKidArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.COPPER_KID_ARROW_ENTITY, world, owner, stack, shotFrom, DAMAGE_AMOUNT);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.COPPER_KID_ARROW);
    }

    @Override
    public SoundEvent getHitSound() {
        return ModSoundEvents.COPPER_GOLDEN_ARROW_HIT;
    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            iLivingEntity.setStuckCopperKidArrowCount(iLivingEntity.getStuckCopperKidArrowCount() + 1);
        }
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }

    
}
