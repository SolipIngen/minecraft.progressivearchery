package solipingen.progressivearchery.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.progressivearchery.util.interfaces.mixin.entity.EntityInterface;
import solipingen.progressivearchery.util.interfaces.mixin.entity.Leashable$LeashDataInterface;


@Mixin(Leashable.LeashData.class)
public abstract class Leashable$LeashDataMixin implements Leashable$LeashDataInterface {
    @Unique private boolean fireproofLeashed = false;


    @Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void injectedInitLeashHolder(Entity leashHolder, CallbackInfo cbi) {
        this.fireproofLeashed = ((EntityInterface)leashHolder).isFireproofLeashing();
    }

    @Override
    public boolean isFireproofLeashed() {
        return this.fireproofLeashed;
    }

    @Override
    public void setFireproofLeashed(boolean fireproofLeashed) {
        this.fireproofLeashed = fireproofLeashed;
    }


}
