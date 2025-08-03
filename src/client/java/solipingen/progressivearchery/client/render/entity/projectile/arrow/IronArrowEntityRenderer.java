package solipingen.progressivearchery.client.render.entity.projectile.arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.arrow.IronArrowEntity;


@Environment(value = EnvType.CLIENT)
public class IronArrowEntityRenderer extends ProjectileEntityRenderer<IronArrowEntity, ArrowEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/iron_arrow.png");


    public IronArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public ArrowEntityRenderState createRenderState() {
        return new ArrowEntityRenderState();
    }

    @Override
    protected Identifier getTexture(ArrowEntityRenderState state) {
        return TEXTURE;
    }
}

