package solipingen.progressivearchery.client.render.entity.projectile.arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.arrow.WoodenArrowEntity;


@Environment(value=EnvType.CLIENT)
public class WoodenArrowEntityRenderer extends ProjectileEntityRenderer<WoodenArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/wooden_arrow.png");


    public WoodenArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(WoodenArrowEntity woodenarrowEntity) {
        return TEXTURE;
    }
}

