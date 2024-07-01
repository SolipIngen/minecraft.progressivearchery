package solipingen.progressivearchery.mixin.entity;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.mixin.entity.accessors.Leashable$LeashDataInvoker;
import solipingen.progressivearchery.util.interfaces.mixin.entity.EntityInterface;
import solipingen.progressivearchery.util.interfaces.mixin.entity.Leashable$LeashDataInterface;


@Mixin(Leashable.class)
public interface LeashableMixin {


    @Inject(method = "resolveLeashData", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;age:I"), cancellable = true)
    private static <E extends Entity> void injectedResolveLeashData(E entity, Leashable.LeashData leashData, CallbackInfo cbi) {
        if (entity.age > 100 && ((Leashable$LeashDataInterface)(Object)leashData).isFireproofLeashed()) {
            entity.dropItem(ModItems.FIREPROOF_LEAD);
            ((Leashable)entity).setLeashData(null);
            cbi.cancel();
        }
    }

    @Inject(method = "detachLeash(Lnet/minecraft/entity/Entity;ZZ)V", at = @At("HEAD"), cancellable = true)
    private static <E extends Entity> void injectedDetachLeash(E entity, boolean sendPacket, boolean dropItem, CallbackInfo cbi) {
        Leashable.LeashData leashData = ((Leashable)entity).getLeashData();
        if (leashData != null && leashData.leashHolder != null && ((Leashable$LeashDataInterface)(Object)leashData).isFireproofLeashed()) {
            if (!entity.getWorld().isClient && dropItem) {
                ((EntityInterface)leashData.leashHolder).setFireproofLeashing(false);
                entity.dropItem(ModItems.FIREPROOF_LEAD);
            }
            ((Leashable)entity).setLeashData(null);
            if (sendPacket && entity.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.getChunkManager().sendToOtherNearbyPlayers(entity, new EntityAttachS2CPacket(entity, null));
            }
            cbi.cancel();
        }
    }

    @Inject(method = "readLeashDataFromNbt", at = @At("HEAD"), cancellable = true)
    private void injectedReadLeashDataFromNbt(NbtCompound nbt, CallbackInfoReturnable<Leashable.LeashData> cbireturn) {
        if (nbt.contains(Leashable.LEASH_NBT_KEY, NbtElement.COMPOUND_TYPE) && nbt.contains("fireproof_leashed")) {
            Leashable.LeashData leashData = Leashable$LeashDataInvoker.invokeLeashDataInit(Either.left(nbt.getCompound(Leashable.LEASH_NBT_KEY).getUuid("UUID")));
            ((Leashable$LeashDataInterface)(Object)leashData).setFireproofLeashed(nbt.getBoolean("fireproof_leashed"));
            cbireturn.setReturnValue(leashData);
        }
        Either either = (Either)NbtHelper.toBlockPos(nbt, Leashable.LEASH_NBT_KEY).map(Either::right).orElse(null);
        if (nbt.contains(Leashable.LEASH_NBT_KEY, NbtElement.INT_ARRAY_TYPE) && either != null && nbt.contains("fireproof_leashed")) {
            Leashable.LeashData leashData = Leashable$LeashDataInvoker.invokeLeashDataInit(either);
            ((Leashable$LeashDataInterface)(Object)leashData).setFireproofLeashed(nbt.getBoolean("fireproof_leashed"));
            cbireturn.setReturnValue(leashData);
        }
    }

    @Inject(method = "writeLeashDataToNbt", at = @At("TAIL"))
    private void injectedWriteLeashDataToNbt(NbtCompound nbt, @Nullable Leashable.LeashData leashData, CallbackInfo cbi) {
        nbt.putBoolean("fireproof_leashed", ((Leashable$LeashDataInterface)(Object)leashData).isFireproofLeashed());
    }



}
