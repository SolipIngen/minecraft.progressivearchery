package solipingen.progressivearchery.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;


public abstract class ModArrowEntity extends PersistentProjectileEntity {
    private final double damageAmount;


    protected ModArrowEntity(EntityType<? extends ModArrowEntity> entityType, double damageAmount, World world) {
        super(entityType, world);
        this.damageAmount = damageAmount;
    }

    protected ModArrowEntity(EntityType<? extends ModArrowEntity> entityType, double damageAmount, World world, double x, double y, double z) {
        super(entityType, x, y, z, world);
        this.damageAmount = damageAmount;
        this.setSound(SoundEvents.ENTITY_ARROW_HIT);
    }

    protected ModArrowEntity(EntityType<? extends ModArrowEntity> entityType, double damageAmount, World world, LivingEntity owner) {
        super(entityType, owner, world);
        this.damageAmount = damageAmount;
        this.setSound(SoundEvents.ENTITY_ARROW_HIT);
    }

    public void initFromStack(ItemStack stack) {
        this.setDamage(this.damageAmount);
    }

    @Override
    public SoundEvent getHitSound() {
        return SoundEvents.ENTITY_ARROW_HIT;
    }


}
