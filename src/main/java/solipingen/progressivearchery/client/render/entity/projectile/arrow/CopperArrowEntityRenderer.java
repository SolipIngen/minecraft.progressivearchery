package solipingen.progressivearchery.client.render.entity.projectile.arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.arrow.CopperArrowEntity;


@Environment(value = EnvType.CLIENT)
public class CopperArrowEntityRenderer extends ProjectileEntityRenderer<CopperArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/copper_arrow.png");


    public CopperArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CopperArrowEntity copperarrowEntity) {
        return TEXTURE;
    }
    
}

