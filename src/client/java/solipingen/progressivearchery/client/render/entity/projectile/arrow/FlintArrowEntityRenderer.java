package solipingen.progressivearchery.client.render.entity.projectile.arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.arrow.FlintArrowEntity;


@Environment(value = EnvType.CLIENT)
public class FlintArrowEntityRenderer extends ProjectileEntityRenderer<FlintArrowEntity> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/flint_arrow.png");


    public FlintArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(FlintArrowEntity flintarrowEntity) {
        return TEXTURE;
    }


}

