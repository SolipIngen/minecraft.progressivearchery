package solipingen.progressivearchery.entity.projectile.arrow;

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


public class DiamondArrowEntity extends ModArrowEntity {
    private static final double DAMAGE_AMOUNT = 5.0;

    
    public DiamondArrowEntity(EntityType<? extends DiamondArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world);
    }

    public DiamondArrowEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.DIAMOND_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z);
        this.setSound(ModSoundEvents.DIAMOND_ARROW_HIT);
    }

    public DiamondArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.DIAMOND_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner);
        this.setSound(ModSoundEvents.DIAMOND_ARROW_HIT);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.DIAMOND_ARROW);
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
            iLivingEntity.setStuckDiamondArrowCount(iLivingEntity.getStuckDiamondArrowCount() + 1);
        }
    }

    
}
