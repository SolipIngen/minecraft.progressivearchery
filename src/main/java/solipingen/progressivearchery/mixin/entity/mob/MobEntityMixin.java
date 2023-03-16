package solipingen.progressivearchery.mixin.entity.mob;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.mob.MobEntityInterface;


@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements MobEntityInterface {
    @Shadow @Nullable private Entity holdingEntity;
    @Shadow @Nullable private NbtCompound leashNbt;
    @Shadow @Final public static String LEASH_KEY;
    private static final TrackedData<Boolean> FIREPROOF_LEASHED = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean getLeashedByFireproofLead() {
        return this.dataTracker.get(FIREPROOF_LEASHED);
    }

    @Override
    public void setLeashedByFireproofLead(boolean fireproofLeashed) {
        this.dataTracker.set(FIREPROOF_LEASHED, fireproofLeashed);
    }

    @Invoker("canbeLeashedBy")
    public abstract boolean invokeCanBeLeashedBy(PlayerEntity player);

    @Invoker("attachLeash")
    public abstract void invokeAttachLeash(Entity entity, boolean sendPacket);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectedInitDataTracker(CallbackInfo cbi) {
        this.dataTracker.startTracking(FIREPROOF_LEASHED, false);
    }

    @Inject(method = "interactWithItem", at = @At("HEAD"), cancellable = true)
    private void injectedInteractWithItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ModItems.FIREPROOF_LEAD) && this.invokeCanBeLeashedBy(player)) {
            this.invokeAttachLeash(player, true);
            this.setLeashedByFireproofLead(true);
            itemStack.decrement(1);
            cbireturn.setReturnValue(ActionResult.success(this.world.isClient));
        }
    }

    @Inject(method = "detachLeash", at = @At("HEAD"))
    private void injectedDetachLeash(boolean sendPacket, boolean dropItem, CallbackInfo cbi) {
        if (this.holdingEntity != null) {
            this.holdingEntity = null;
            this.leashNbt = null;
            if (!this.world.isClient && dropItem && this.getLeashedByFireproofLead()) {
                this.dropItem(ModItems.FIREPROOF_LEAD);
                this.setLeashedByFireproofLead(false);
            }
            if (!this.world.isClient && sendPacket && this.world instanceof ServerWorld) {
                ((ServerWorld)this.world).getChunkManager().sendToOtherNearbyPlayers(this, new EntityAttachS2CPacket(this, null));
            }
        }
    }

    @Inject(method = "readLeashNbt", at = @At("HEAD"))
    private void injectedReadLeashNbt(CallbackInfo cbi) {
        if (this.leashNbt != null && this.world instanceof ServerWorld && this.getLeashedByFireproofLead()) {
            if (this.leashNbt.containsUuid("UUID")) {
                UUID uUID = this.leashNbt.getUuid("UUID");
                Entity entity = ((ServerWorld)this.world).getEntity(uUID);
                if (entity != null) {
                    this.invokeAttachLeash(entity, true);
                    return;
                }
            } else if (this.leashNbt.contains("X", NbtElement.NUMBER_TYPE) && this.leashNbt.contains("Y", NbtElement.NUMBER_TYPE) && this.leashNbt.contains("Z", NbtElement.NUMBER_TYPE)) {
                BlockPos blockPos = NbtHelper.toBlockPos(this.leashNbt);
                this.invokeAttachLeash(LeashKnotEntity.getOrCreate(this.world, blockPos), true);
                return;
            }
            if (this.age > 100) {
                this.dropItem(ModItems.FIREPROOF_LEAD);
                this.leashNbt = null;
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        nbt.putBoolean("Fireproof_leashed", this.getLeashedByFireproofLead());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.setLeashedByFireproofLead(nbt.getBoolean("Fireproof_leashed"));
    }
    
}
