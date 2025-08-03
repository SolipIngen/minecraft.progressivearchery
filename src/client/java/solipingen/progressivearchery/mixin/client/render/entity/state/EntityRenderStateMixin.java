package solipingen.progressivearchery.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.EntityRenderStateInterface;


@Mixin(EntityRenderState.class)
@Environment(EnvType.CLIENT)
public abstract class EntityRenderStateMixin implements EntityRenderStateInterface {
    @Unique private boolean fireproofLeashed;


    @Override
    public boolean getIsFireproofLeashed() {
        return this.fireproofLeashed;
    }

    @Override
    public void setIsFireproofLeashed(boolean fireproofLeashed) {
        this.fireproofLeashed = fireproofLeashed;
    }


}
