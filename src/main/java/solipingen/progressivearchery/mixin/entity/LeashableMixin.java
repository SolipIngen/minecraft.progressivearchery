package solipingen.progressivearchery.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.util.interfaces.mixin.entity.EntityInterface;


@Mixin(Leashable.class)
public interface LeashableMixin {


    @Inject(method = "resolveLeashData", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;age:I"), cancellable = true)
    private static <E extends Entity> void injectedResolveLeashData(E entity, Leashable.LeashData leashData, CallbackInfo cbi) {
        if (entity.getWorld() instanceof ServerWorld serverWorld && entity.age > 100 && ((EntityInterface)entity).isFireproofLeashed()) {
            entity.dropItem(serverWorld, ModItems.FIREPROOF_LEAD);
            ((Leashable)entity).setLeashData(null);
            cbi.cancel();
        }
    }

    @Inject(method = "detachLeash(Lnet/minecraft/entity/Entity;ZZ)V", at = @At("HEAD"), cancellable = true)
    private static <E extends Entity> void injectedDetachLeash(E entity, boolean sendPacket, boolean dropItem, CallbackInfo cbi) {
        Leashable.LeashData leashData = ((Leashable)entity).getLeashData();
        if (leashData != null && leashData.leashHolder != null && ((EntityInterface)entity).isFireproofLeashed()) {
            ((Leashable)entity).setLeashData(null);
            ((Leashable)entity).onLeashRemoved();
            if (entity.getWorld() instanceof ServerWorld serverWorld) {
                if (dropItem) {
                    entity.dropItem(serverWorld, ModItems.FIREPROOF_LEAD);
                }
                if (sendPacket) {
                    serverWorld.getChunkManager().sendToOtherNearbyPlayers(entity, new EntityAttachS2CPacket(entity, null));
                }
                leashData.leashHolder.onHeldLeashUpdate((Leashable)entity);
            }
            ((EntityInterface)entity).setFireproofLeashed(false);
            cbi.cancel();
        }
    }

    @Inject(method = "attachLeash(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Z)V", at = @At("HEAD"))
    private static <E extends Entity & Leashable> void injectedAttachLeash(E entity, Entity leashHolder, boolean sendPacket, CallbackInfo cbi) {
        if (entity.getLeashData() != null && ((EntityInterface)entity).isFireproofLeashed() && leashHolder instanceof LeashKnotEntity) {
            ((EntityInterface)leashHolder).setFireproofLeashed(((EntityInterface)entity).isFireproofLeashed());
        }
    }



}
