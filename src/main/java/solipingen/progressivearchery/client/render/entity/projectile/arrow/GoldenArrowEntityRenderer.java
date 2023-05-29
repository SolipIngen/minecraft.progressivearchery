package solipingen.progressivearchery.client.render.entity.projectile.arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;


@Environment(value = EnvType.CLIENT)
public class GoldenArrowEntityRenderer extends ProjectileEntityRenderer<GoldenArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/golden_arrow.png");
    public static final Identifier TIPPED_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/tipped_arrow.png");


    public GoldenArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(GoldenArrowEntity goldenarrowEntity) {
        return goldenarrowEntity.getColor() > 0 ? TIPPED_TEXTURE : TEXTURE;
    }

}

