package solipingen.progressivearchery.mixin.entity.decoration;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.decoration.BlockAttachedEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.util.interfaces.mixin.entity.EntityInterface;

import java.util.List;


@Mixin(LeashKnotEntity.class)
public abstract class LeashKnotEntityMixin extends BlockAttachedEntity {


    protected LeashKnotEntityMixin(EntityType<? extends BlockAttachedEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interact", at = @At("RETURN"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;shouldCancelInteraction()Z")), cancellable = true)
    private void injectedInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        List<Leashable> list3 = Leashable.collectLeashablesHeldBy((LeashKnotEntity)(Object)this);
        if (!list3.isEmpty()) {
            boolean bl = false;
            for (Leashable leashable : list3) {
                bl |= ((EntityInterface)leashable).isFireproofLeashed();
            }
            ((EntityInterface)this).setFireproofLeashed(bl);
        }
    }




}
