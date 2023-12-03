package solipingen.progressivearchery.client.render.entity.projectile.kid_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.kid_arrow.SpectralKidArrowEntity;


@Environment(value = EnvType.CLIENT)
public class SpectralKidArrowEntityRenderer extends ProjectileEntityRenderer<SpectralKidArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/spectral_kid_arrow.png");


    public SpectralKidArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(SpectralKidArrowEntity spectralkidArrowEntity) {
        return TEXTURE;
    }


}

