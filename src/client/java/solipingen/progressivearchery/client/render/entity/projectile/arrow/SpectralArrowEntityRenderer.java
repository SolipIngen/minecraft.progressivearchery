package solipingen.progressivearchery.client.render.entity.projectile.arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.entity.projectile.arrow.SpectralArrowEntity;


@Environment(value = EnvType.CLIENT)
public class SpectralArrowEntityRenderer extends ProjectileEntityRenderer<SpectralArrowEntity> {
    public static final Identifier TEXTURE = Identifier.of("textures/entity/projectiles/spectral_arrow.png");


    public SpectralArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(SpectralArrowEntity spectralarrowEntity) {
        return TEXTURE;
    }

}

