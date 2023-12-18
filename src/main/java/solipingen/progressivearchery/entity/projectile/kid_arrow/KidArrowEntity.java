package solipingen.progressivearchery.entity.projectile.kid_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;


public abstract class KidArrowEntity extends PersistentProjectileEntity {
    private final double damageAmount;


    public KidArrowEntity(EntityType<? extends KidArrowEntity> entityType, double damageAmount, World world, ItemStack stack) {
        super(entityType, world, stack);
        this.damageAmount = damageAmount;
    }

    public KidArrowEntity(EntityType<? extends KidArrowEntity> entityType, double damageAmount, World world, double x, double y, double z, ItemStack stack) {
        super(entityType, x, y, z, world, stack);
        this.damageAmount = damageAmount;
        this.setSound(SoundEvents.ENTITY_ARROW_HIT);
    }

    public KidArrowEntity(EntityType<? extends KidArrowEntity> entityType, double damageAmount, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack);
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
