package solipingen.progressivearchery.mixin.client.render.entity.state;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.StriderEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.StriderEntityRenderStateInterface;


@Mixin(StriderEntityRenderState.class)
public abstract class StriderEntityRenderStateMixin extends LivingEntityRenderState implements StriderEntityRenderStateInterface {
    @Unique private boolean isSheared;


    @Override
    public boolean getIsSheared() {
        return this.isSheared;
    }

    @Override
    public void setIsSheared(boolean sheared) {
        this.isSheared = sheared;
    }


}
