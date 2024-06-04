package solipingen.progressivearchery.entity.projectile.arrow;

import java.util.Iterator;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;


public class GoldenArrowEntity extends ModArrowEntity {
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.GOLDEN_ARROW);
    private static final TrackedData<Integer> COLOR = DataTracker.registerData(GoldenArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final double DAMAGE_AMOUNT = 2.25;

    
    public GoldenArrowEntity(EntityType<? extends GoldenArrowEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, world, DEFAULT_STACK);
    }

    public GoldenArrowEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.GOLDEN_ARROW_ENTITY, DAMAGE_AMOUNT, world, x, y, z, stack);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    public GoldenArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.GOLDEN_ARROW_ENTITY, DAMAGE_AMOUNT, world, owner, stack);
        this.setSound(ModSoundEvents.COPPER_GOLDEN_ARROW_HIT);
    }

    @Override
    public void initFromStack(ItemStack stack) {
        super.initFromStack(stack);
        if (stack.isOf(Items.TIPPED_ARROW)) {
            this.setPotionContents(stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT));
        }
    }

    private PotionContentsComponent getPotionContents() {
        return this.getItemStack().getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
    }

    private void setPotionContents(PotionContentsComponent potionContentsComponent) {
        this.getItemStack().set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
        this.initColor();
    }

    private void initColor() {
        PotionContentsComponent potionContentsComponent = this.getPotionContents();
        this.dataTracker.set(COLOR, potionContentsComponent.equals(PotionContentsComponent.DEFAULT) ? -1 : potionContentsComponent.getColor());
    }

    public void addEffect(StatusEffectInstance effect) {
        this.setPotionContents(this.getPotionContents().with(effect));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COLOR, -1);
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
        if (world.isClient) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.spawnParticles(1);
                }
            }
            else {
                this.spawnParticles(2);
            }
        } 
        else if (this.inGround && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContentsComponent.DEFAULT) && this.inGroundTime >= 600) {
            world.sendEntityStatus(this, (byte)0);
            this.setStack(new ItemStack(ModItems.GOLDEN_ARROW));
        }
    }

    private void spawnParticles(int amount) {
        int i = this.getColor();
        if (i != -1 && amount > 0) {
            for(int j = 0; j < amount; ++j) {
                this.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, i),
                        this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
            }
        }
    }

    public int getColor() {
        return this.dataTracker.get(COLOR);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        Entity entity = this.getEffectCause();
        PotionContentsComponent potionContentsComponent = this.getPotionContents();
        Iterator var4;
        StatusEffectInstance statusEffectInstance;
        if (potionContentsComponent.potion().isPresent()) {
            var4 = ((Potion)((RegistryEntry)potionContentsComponent.potion().get()).value()).getEffects().iterator();
            while(var4.hasNext()) {
                statusEffectInstance = (StatusEffectInstance)var4.next();
                target.addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(), Math.max(statusEffectInstance.mapDuration((i) -> {
                    return i / 8;
                }), 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
            }
        }
        var4 = potionContentsComponent.customEffects().iterator();
        while(var4.hasNext()) {
            statusEffectInstance = (StatusEffectInstance)var4.next();
            target.addStatusEffect(statusEffectInstance, entity);
        }
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
            iLivingEntity.setStuckGoldenArrowCount(iLivingEntity.getStuckGoldenArrowCount() + 1);
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (i >> 16 & 255) / 255.0f;
                float g = (i >> 8 & 255) / 255.0f;
                float h = (i >> 0 & 255) / 255.0f;
                for (int j = 0; j < 20; ++j) {
                    this.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, g, h),
                            this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
                }
            }
        } 
        else {
            super.handleStatus(status);
        }
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.GOLDEN_ARROW);
    }
}
