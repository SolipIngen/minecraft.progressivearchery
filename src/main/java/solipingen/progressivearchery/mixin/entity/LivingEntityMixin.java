package solipingen.progressivearchery.mixin.entity;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.util.interfaces.mixin.entity.LivingEntityInterface;
import solipingen.progressivearchery.village.ModVillagerProfessions;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInterface {
    private static final TrackedData<Integer> STUCK_WOODEN_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_WOODEN_KID_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_FLINT_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_FLINT_KID_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_COPPER_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_COPPER_KID_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_GOLDEN_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_GOLDEN_KID_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_SPECTRAL_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_SPECTRAL_KID_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_IRON_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_IRON_KID_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_DIAMOND_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STUCK_DIAMOND_KID_ARROW_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectedInitDataTracker(CallbackInfo cbi) {
        this.dataTracker.startTracking(STUCK_WOODEN_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_WOODEN_KID_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_FLINT_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_FLINT_KID_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_COPPER_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_COPPER_KID_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_GOLDEN_ARROW_COUNT, 0);        
        this.dataTracker.startTracking(STUCK_GOLDEN_KID_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_SPECTRAL_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_SPECTRAL_KID_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_IRON_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_IRON_KID_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_DIAMOND_ARROW_COUNT, 0);
        this.dataTracker.startTracking(STUCK_DIAMOND_KID_ARROW_COUNT, 0);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateLeaningPitch()V", shift = At.Shift.AFTER))
    private void injectedTick(CallbackInfo cbi) {
        World world = this.getWorld();
        if (!world.isClient) {
            int i = this.getStuckWoodenArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckWoodenArrowCount(i - 1);
                }
            }
            i = this.getStuckWoodenKidArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckWoodenKidArrowCount(i - 1);
                }
            }
            i = this.getStuckFlintArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckFlintArrowCount(i - 1);
                }
            }
            i = this.getStuckFlintKidArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckFlintKidArrowCount(i - 1);
                }
            }
            i = this.getStuckCopperArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckCopperArrowCount(i - 1);
                }
            }
            i = this.getStuckCopperKidArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckCopperKidArrowCount(i - 1);
                }
            }
            i = this.getStuckGoldenArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckGoldenArrowCount(i - 1);
                }
            }
            i = this.getStuckGoldenKidArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckGoldenKidArrowCount(i - 1);
                }
            }
            i = this.getStuckSpectralArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckSpectralArrowCount(i - 1);
                }
            }
            i = this.getStuckSpectralKidArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckSpectralKidArrowCount(i - 1);
                }
            }
            i = this.getStuckIronArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckIronArrowCount(i - 1);
                }
            }
            i = this.getStuckIronKidArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckIronKidArrowCount(i - 1);
                }
            }
            i = this.getStuckDiamondArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckDiamondArrowCount(i - 1);
                }
            }
            i = this.getStuckDiamondKidArrowCount();
            if (i > 0) {
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    ((LivingEntity)(Object)this).stuckArrowTimer = 20 * (30 - i);
                }
                --((LivingEntity)(Object)this).stuckArrowTimer;
                if (((LivingEntity)(Object)this).stuckArrowTimer <= 0) {
                    this.setStuckDiamondKidArrowCount(i - 1);
                }
            }

        }
    }
    
    @Inject(method = "getPreferredEquipmentSlot", at = @At("TAIL"), cancellable = true)
    private static void injectedGetPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cbireturn) {
        if (stack.getItem() instanceof QuiverItem) {
            cbireturn.setReturnValue(EquipmentSlot.CHEST);
        }
    }

    @Inject(method = "onKilledBy", at = @At("TAIL"))
    private void injectedOnKilledBy(@Nullable LivingEntity adversary, CallbackInfo cbi) {
        if (adversary instanceof VillagerEntity && adversary.getWorld() instanceof ServerWorld) {
            VillagerEntity villager = (VillagerEntity)adversary;
            VillagerProfession profession = villager.getVillagerData().getProfession();
            if (profession == ModVillagerProfessions.ARCHER) {
                int i = villager.getVillagerData().getLevel();
                if (((LivingEntity)(Object)this) instanceof MobEntity && ((MobEntity)(Object)this).getXpToDrop() >= 1) {
                    villager.setExperience(villager.getExperience() + i*((MobEntity)(Object)this).getXpToDrop());
                }
                else if (((LivingEntity)(Object)this) instanceof PlayerEntity && ((PlayerEntity)(Object)this).getXpToDrop() >= 1) {
                    villager.setExperience(villager.getExperience() + i*((PlayerEntity)(Object)this).getXpToDrop());
                }
                villager.reinitializeBrain((ServerWorld)adversary.getWorld());
            }
        }
    }

    @Override
    public int getStuckWoodenArrowCount() {
        return this.dataTracker.get(STUCK_WOODEN_ARROW_COUNT);
    }
    
    @Override
    public int getStuckWoodenKidArrowCount() {
        return this.dataTracker.get(STUCK_WOODEN_KID_ARROW_COUNT);
    }

    @Override
    public int getStuckFlintArrowCount() {
        return this.dataTracker.get(STUCK_FLINT_ARROW_COUNT);
    }

    @Override
    public int getStuckFlintKidArrowCount() {
        return this.dataTracker.get(STUCK_FLINT_KID_ARROW_COUNT);
    }

    @Override
    public int getStuckCopperArrowCount() {
        return this.dataTracker.get(STUCK_COPPER_ARROW_COUNT);
    }

    @Override
    public int getStuckCopperKidArrowCount() {
        return this.dataTracker.get(STUCK_COPPER_KID_ARROW_COUNT);
    }

    @Override
    public int getStuckGoldenArrowCount() {
        return this.dataTracker.get(STUCK_GOLDEN_ARROW_COUNT);
    }

    @Override
    public int getStuckGoldenKidArrowCount() {
        return this.dataTracker.get(STUCK_GOLDEN_KID_ARROW_COUNT);
    }

    @Override
    public int getStuckSpectralArrowCount() {
        return this.dataTracker.get(STUCK_SPECTRAL_ARROW_COUNT);
    }

    @Override
    public int getStuckSpectralKidArrowCount() {
        return this.dataTracker.get(STUCK_SPECTRAL_KID_ARROW_COUNT);
    }

    @Override
    public int getStuckIronArrowCount() {
        return this.dataTracker.get(STUCK_IRON_ARROW_COUNT);
    }

    @Override
    public int getStuckIronKidArrowCount() {
        return this.dataTracker.get(STUCK_IRON_KID_ARROW_COUNT);
    }

    @Override
    public int getStuckDiamondArrowCount() {
        return this.dataTracker.get(STUCK_DIAMOND_ARROW_COUNT);
    }

    @Override
    public int getStuckDiamondKidArrowCount() {
        return this.dataTracker.get(STUCK_DIAMOND_KID_ARROW_COUNT);
    }

    @Override
    public void setStuckWoodenArrowCount(int count) {
        this.dataTracker.set(STUCK_WOODEN_ARROW_COUNT, count);
    }
    
    @Override
    public void setStuckWoodenKidArrowCount(int count) {
        this.dataTracker.set(STUCK_WOODEN_KID_ARROW_COUNT, count);
    }

    @Override
    public void setStuckFlintArrowCount(int count) {
        this.dataTracker.set(STUCK_FLINT_ARROW_COUNT, count);
    }

    @Override
    public void setStuckFlintKidArrowCount(int count) {
        this.dataTracker.set(STUCK_FLINT_KID_ARROW_COUNT, count);
    }

    @Override
    public void setStuckCopperArrowCount(int count) {
        this.dataTracker.set(STUCK_COPPER_ARROW_COUNT, count);
    }

    @Override
    public void setStuckCopperKidArrowCount(int count) {
        this.dataTracker.set(STUCK_COPPER_KID_ARROW_COUNT, count);
    }

    @Override
    public void setStuckGoldenArrowCount(int count) {
        this.dataTracker.set(STUCK_GOLDEN_ARROW_COUNT, count);
    }

    @Override
    public void setStuckGoldenKidArrowCount(int count) {
        this.dataTracker.set(STUCK_GOLDEN_KID_ARROW_COUNT, count);
    }

    @Override
    public void setStuckSpectralArrowCount(int count) {
        this.dataTracker.set(STUCK_SPECTRAL_ARROW_COUNT, count);
    }

    @Override
    public void setStuckSpectralKidArrowCount(int count) {
        this.dataTracker.set(STUCK_SPECTRAL_KID_ARROW_COUNT, count);
    }

    @Override
    public void setStuckIronArrowCount(int count) {
        this.dataTracker.set(STUCK_IRON_ARROW_COUNT, count);
    }

    @Override
    public void setStuckIronKidArrowCount(int count) {
        this.dataTracker.set(STUCK_IRON_KID_ARROW_COUNT, count);
    }

    @Override
    public void setStuckDiamondArrowCount(int count) {
        this.dataTracker.set(STUCK_DIAMOND_ARROW_COUNT, count);
    }

    @Override
    public void setStuckDiamondKidArrowCount(int count) {
        this.dataTracker.set(STUCK_DIAMOND_KID_ARROW_COUNT, count);
    }

}
