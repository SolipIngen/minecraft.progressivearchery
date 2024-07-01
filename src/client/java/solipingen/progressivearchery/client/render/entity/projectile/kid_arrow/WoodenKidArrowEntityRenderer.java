package solipingen.progressivearchery.client.render.entity.projectile.kid_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.kid_arrow.WoodenKidArrowEntity;


@Environment(value = EnvType.CLIENT)
public class WoodenKidArrowEntityRenderer extends ProjectileEntityRenderer<WoodenKidArrowEntity> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/wooden_kid_arrow.png");


    public WoodenKidArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(WoodenKidArrowEntity kidarrowEntity) {
        return TEXTURE;
    }
}

