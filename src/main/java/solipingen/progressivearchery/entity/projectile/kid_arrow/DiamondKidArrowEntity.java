package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


public class DiamondKidArrowEntity extends KidArrowEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.DIAMOND_KID_ARROW);
    private static final double DAMAGE_AMOUNT = 3.5;

    
    public DiamondKidArrowEntity(EntityType<? extends DiamondKidArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world, DEFAULT_STACK);
    }

    public DiamondKidArrowEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.DIAMOND_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z, stack);
        this.setSound(ModSoundEvents.DIAMOND_ARROW_HIT);
    }

    public DiamondKidArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.DIAMOND_KID_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner, stack);
        this.setSound(ModSoundEvents.DIAMOND_ARROW_HIT);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.DIAMOND_KID_ARROW);
    }

    @Override
    public SoundEvent getHitSound() {
        return ModSoundEvents.DIAMOND_ARROW_HIT;
    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.setSound(ModSoundEvents.DIAMOND_ARROW_HIT);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            iLivingEntity.setStuckDiamondKidArrowCount(iLivingEntity.getStuckDiamondKidArrowCount() + 1);
        }
    }


}
