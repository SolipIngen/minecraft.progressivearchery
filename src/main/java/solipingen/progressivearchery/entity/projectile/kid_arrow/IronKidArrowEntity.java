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


public class IronKidArrowEntity extends KidArrowEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.IRON_KID_ARROW);
    private static final double DAMAGE_AMOUNT = 3.5;

    
    public IronKidArrowEntity(EntityType<? extends IronKidArrowEntity> entityType, World world) {
        super(entityType, world, DAMAGE_AMOUNT);
    }

    public IronKidArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.IRON_KID_ARROW_ENTITY, world, x, y, z, stack, shotFrom, DAMAGE_AMOUNT);
        this.setSound(ModSoundEvents.IRON_ARROW_HIT);
    }

    public IronKidArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityTypes.IRON_KID_ARROW_ENTITY, world, owner, stack, shotFrom, DAMAGE_AMOUNT);
        this.setSound(ModSoundEvents.IRON_ARROW_HIT);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.IRON_KID_ARROW);
    }

    @Override
    public SoundEvent getHitSound() {
        return ModSoundEvents.IRON_ARROW_HIT;
    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.setSound(ModSoundEvents.IRON_ARROW_HIT);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entityHitResult.getEntity();
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            iLivingEntity.setStuckIronKidArrowCount(iLivingEntity.getStuckIronKidArrowCount() + 1);
        }
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }


}
