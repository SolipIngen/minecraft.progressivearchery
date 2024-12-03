package solipingen.progressivearchery.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.CopperArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.DiamondArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.FlintArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.GoldenArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.IronArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.SpectralArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.arrow.WoodenArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.CopperKidArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.DiamondKidArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.FlintKidArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.GoldenKidArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.IronKidArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.SpectralKidArrowEntityRenderer;
import solipingen.progressivearchery.client.render.entity.projectile.kid_arrow.WoodenKidArrowEntityRenderer;
import solipingen.progressivearchery.entity.ModEntityTypes;


@Environment(value = EnvType.CLIENT)
public class ModEntityRendererRegistry {


    public static void registerModEntityRenderers() {
        
        //Mod Arrows
        EntityRendererRegistry.register(ModEntityTypes.WOODEN_ARROW_ENTITY, WoodenArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.FLINT_ARROW_ENTITY, FlintArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.COPPER_ARROW_ENTITY, CopperArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.IRON_ARROW_ENTITY, IronArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GOLDEN_ARROW_ENTITY, GoldenArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.SPECTRAL_ARROW_ENTITY, SpectralArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.DIAMOND_ARROW_ENTITY, DiamondArrowEntityRenderer::new);

        // Kid Arrows
        EntityRendererRegistry.register(ModEntityTypes.WOODEN_KID_ARROW_ENTITY, WoodenKidArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.FLINT_KID_ARROW_ENTITY, FlintKidArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.COPPER_KID_ARROW_ENTITY, CopperKidArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.IRON_KID_ARROW_ENTITY, IronKidArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GOLDEN_KID_ARROW_ENTITY, GoldenKidArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.SPECTRAL_KID_ARROW_ENTITY, SpectralKidArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.DIAMOND_KID_ARROW_ENTITY, DiamondKidArrowEntityRenderer::new);

        ProgressiveArchery.LOGGER.debug("Registering Mod Entity Renderers for " + ProgressiveArchery.MOD_ID);
        
    }
    
}
