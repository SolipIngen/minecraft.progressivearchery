package solipingen.progressivearchery.client.render.entity.projectile.arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;


@Environment(value = EnvType.CLIENT)
public class GoldenArrowEntityRenderer extends ProjectileEntityRenderer<GoldenArrowEntity, ArrowEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/golden_arrow.png");
    public static final Identifier TIPPED_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/tipped_arrow.png");


    public GoldenArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public ArrowEntityRenderState createRenderState() {
        return new ArrowEntityRenderState();
    }

    @Override
    protected Identifier getTexture(ArrowEntityRenderState state) {
        return state.tipped ? TIPPED_TEXTURE : TEXTURE;
    }

    @Override
    public void updateRenderState(GoldenArrowEntity arrowEntity, ArrowEntityRenderState arrowEntityRenderState, float f) {
        super.updateRenderState(arrowEntity, arrowEntityRenderState, f);
        arrowEntityRenderState.tipped = arrowEntity.getColor() > 0;
    }

}

