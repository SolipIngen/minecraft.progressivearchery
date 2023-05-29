package solipingen.progressivearchery.client.render.entity.projectile.kid_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.kid_arrow.IronKidArrowEntity;


@Environment(value = EnvType.CLIENT)
public class IronKidArrowEntityRenderer extends ProjectileEntityRenderer<IronKidArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/projectiles/iron_kid_arrow.png");


    public IronKidArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(IronKidArrowEntity kidarrowEntity) {
        return TEXTURE;
    }


}

