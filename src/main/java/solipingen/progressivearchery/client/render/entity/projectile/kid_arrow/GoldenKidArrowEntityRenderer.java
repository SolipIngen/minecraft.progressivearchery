package solipingen.progressivearchery.client.render.entity.projectile.kid_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;


@Environment(value=EnvType.CLIENT)
public class GoldenKidArrowEntityRenderer extends ProjectileEntityRenderer<GoldenKidArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/golden_kid_arrow.png");
    public static final Identifier TIPPED_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/tipped_kid_arrow.png");


    public GoldenKidArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(GoldenKidArrowEntity kidarrowEntity) {
        return kidarrowEntity.getColor() > 0 ? TIPPED_TEXTURE : TEXTURE;
    }

}

