package solipingen.progressivearchery.mixin.entity.passive;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.RideableInventory;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.passive.AbstractHorseEntityInterface;


@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity implements InventoryChangedListener, RideableInventory, JumpingMount, Saddleable, Shearable, AbstractHorseEntityInterface {
    @Shadow @Final private static int EATING_GRASS_FLAG;
    private static final TrackedData<Boolean> IS_SHEARED = DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    protected AbstractHorseEntityMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Override
    public boolean isShearable() {
        return this.isAlive() && this.getGroup() != EntityGroup.UNDEAD && !this.getIsSheared() && !this.isBaby() && this.invokeIsTame();
    }

    @Override
    public boolean getIsSheared() {
        return this.dataTracker.get(IS_SHEARED);
    }

    @Override
    public void setIsSheared(boolean isSheared) {
        this.dataTracker.set(IS_SHEARED, isSheared);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectedInitDataTracker(CallbackInfo cbi) {
        this.dataTracker.startTracking(IS_SHEARED, false);
    }

    @Override
    public void sheared(SoundCategory shearedSoundCategory) {
        this.setIsSheared(true);
        ItemEntity itemEntity = this.dropItem(ModItems.HORSEHAIR, 1);
        itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
    }

    @Invoker("getHorseFlag")
    public abstract boolean invokeGetHorseFlag(int bitmask);

    @Invoker("isTame")
    public abstract boolean invokeIsTame();

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void injectedInteractMob(PlayerEntity player2, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        ItemStack itemStack = player2.getStackInHand(hand);
        if (!itemStack.isEmpty()) {
            if (itemStack.isOf(Items.SHEARS)) {
                World world = this.getWorld();
                if (!world.isClient && this.isShearable()) {
                    this.sheared(SoundCategory.PLAYERS);
                    this.emitGameEvent(GameEvent.SHEAR, player2);
                    itemStack.damage(1, player2, player -> player.sendToolBreakStatus(hand));
                    cbireturn.setReturnValue(ActionResult.success(world.isClient));
                }
            }
        }
    }

    @Inject(method = "setEatingGrass", at = @At("TAIL"))
    private void injectedSetEatingGrass(boolean eatingGrass, CallbackInfo cbi) {
        World world = this.getWorld();
        if (!world.isClient && this.getIsSheared()) {
            this.setIsSheared(false);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        nbt.putBoolean("Sheared", this.getIsSheared());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.setIsSheared(nbt.getBoolean("Sheared"));
    }

}
