package solipingen.progressivearchery.mixin.entity.passive;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import solipingen.progressivearchery.util.interfaces.mixin.entity.passive.StriderEntityInterface;


@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity implements ItemSteerable, Saddleable, Shearable, StriderEntityInterface {
    private static final TrackedData<Boolean> IS_SHEARED = DataTracker.registerData(StriderEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> HAIR_GROWTH_TIME = DataTracker.registerData(StriderEntity.class, TrackedDataHandlerRegistry.INTEGER);


    protected StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isShearable() {
        return this.isAlive() && !this.getIsSheared() && !this.isBaby();
    }

    @Override
    public boolean getIsSheared() {
        return this.dataTracker.get(IS_SHEARED);
    }

    @Override
    public void setIsSheared(boolean isSheared) {
        this.dataTracker.set(IS_SHEARED, isSheared);
    }

    @Override
    public void sheared(SoundCategory shearedSoundCategory) {
        this.setIsSheared(true);
        ItemEntity itemEntity = this.dropItem(ModItems.STRIDERHAIR, 1);
        itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void injectedInteractMob(PlayerEntity player2, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        ItemStack itemStack = player2.getStackInHand(hand);
        if (itemStack.isOf(Items.SHEARS)) {
            World world = this.getWorld();
            if (!world.isClient && this.isShearable()) {
                this.sheared(SoundCategory.PLAYERS);
                this.emitGameEvent(GameEvent.SHEAR, player2);
                itemStack.damage(1, player2, player -> player.sendToolBreakStatus(hand));
                cbireturn.setReturnValue(ActionResult.SUCCESS);
            }
            else {
                cbireturn.setReturnValue(ActionResult.PASS);
            }
        }
    }

    @Override
    public int getHairGrowthTime() {
        return this.dataTracker.get(HAIR_GROWTH_TIME);
    }

    @Override
    public void setHairGrowthTime(int hairGrowthTime) {
        this.dataTracker.set(HAIR_GROWTH_TIME, hairGrowthTime);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectedInitDataTracker(CallbackInfo cbi) {
        this.dataTracker.startTracking(IS_SHEARED, false);
        this.dataTracker.startTracking(HAIR_GROWTH_TIME, this.random.nextBetween(6000, 12000));
    }

    @Invoker("isCold")
    public abstract boolean invokeIsCold();

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectedTick(CallbackInfo cbi) {
        if (this.getIsSheared()) {
            if (this.invokeIsCold()) {
                this.setHairGrowthTime(this.getHairGrowthTime() - 2);
            }
            this.setHairGrowthTime(this.getHairGrowthTime() - 1);
            if (this.getHairGrowthTime() <= 0) {
                this.setHairGrowthTime(this.random.nextBetween(6000, 12000));
                this.setIsSheared(false);
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        nbt.putBoolean("Sheared", this.getIsSheared());
        nbt.putInt("HairGrowthTime", this.getHairGrowthTime());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.setIsSheared(nbt.getBoolean("Sheared"));
        this.setHairGrowthTime(nbt.getInt("HairGrowthTime"));
    }

}
