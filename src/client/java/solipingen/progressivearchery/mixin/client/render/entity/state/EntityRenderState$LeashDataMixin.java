package solipingen.progressivearchery.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.EntityRenderState$LeashDataInterface;


@Mixin(EntityRenderState.LeashData.class)
@Environment(EnvType.CLIENT)
public abstract class EntityRenderState$LeashDataMixin implements EntityRenderState$LeashDataInterface {
    @Unique private boolean isFireproofLeashed;


    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectedInit(CallbackInfo cbi) {
        this.isFireproofLeashed = false;
    }

    @Override
    public boolean getIsFireproofLeashed() {
        return this.isFireproofLeashed;
    }

    @Override
    public void setIsFireproofLeashed(boolean isFireproofLeashed) {
        this.isFireproofLeashed = isFireproofLeashed;
    }


}
