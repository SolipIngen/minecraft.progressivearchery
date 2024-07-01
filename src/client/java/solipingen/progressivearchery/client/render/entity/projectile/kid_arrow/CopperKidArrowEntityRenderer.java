package solipingen.progressivearchery.client.render.entity.projectile.kid_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.kid_arrow.CopperKidArrowEntity;


@Environment(value = EnvType.CLIENT)
public class CopperKidArrowEntityRenderer extends ProjectileEntityRenderer<CopperKidArrowEntity> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/copper_kid_arrow.png");


    public CopperKidArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CopperKidArrowEntity kidarrowEntity) {
        return TEXTURE;
    }


}

