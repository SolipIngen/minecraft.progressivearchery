package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public abstract class KidArrowEntity extends PersistentProjectileEntity {
    private final double damageAmount;


    protected KidArrowEntity(EntityType<? extends KidArrowEntity> entityType, World world, double damageAmount) {
        super(entityType, world);
        this.damageAmount = damageAmount;
    }

    protected KidArrowEntity(EntityType<? extends KidArrowEntity> entityType, World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom, double damageAmount) {
        super(entityType, x, y, z, world, stack, shotFrom);
        this.damageAmount = damageAmount;
        this.setSound(SoundEvents.ENTITY_ARROW_HIT);
    }

    protected KidArrowEntity(EntityType<? extends KidArrowEntity> entityType,World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom, double damageAmount) {
        super(entityType, owner, world, stack, shotFrom);
        this.damageAmount = damageAmount;
        this.setSound(SoundEvents.ENTITY_ARROW_HIT);
    }

    public void initFromStack(ItemStack stack) {
        this.setDamage(this.damageAmount);
    }

    @Override
    protected float getDragInWater() {
        return 0.72f;
    }

    @Override
    public SoundEvent getHitSound() {
        return SoundEvents.ENTITY_ARROW_HIT;
    }


}
