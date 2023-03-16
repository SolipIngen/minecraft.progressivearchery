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


@Environment(value=EnvType.CLIENT)
public class ModEntityRendererRegistry {


    public static void registerModEntityRenderers() {
        
        //Mod Arrows
        EntityRendererRegistry.register(ModEntityTypes.WOODEN_ARROW_ENTITY, (context) -> new WoodenArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.FLINT_ARROW_ENTITY, (context) -> new FlintArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.COPPER_ARROW_ENTITY, (context) -> new CopperArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.IRON_ARROW_ENTITY, (context) -> new IronArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.GOLDEN_ARROW_ENTITY, (context) -> new GoldenArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.SPECTRAL_ARROW_ENTITY, (context) -> new SpectralArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.DIAMOND_ARROW_ENTITY, (context) -> new DiamondArrowEntityRenderer(context));

        // Kid Arrows
        EntityRendererRegistry.register(ModEntityTypes.WOODEN_KID_ARROW_ENTITY, (context) -> new WoodenKidArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.FLINT_KID_ARROW_ENTITY, (context) -> new FlintKidArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.COPPER_KID_ARROW_ENTITY, (context) -> new CopperKidArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.IRON_KID_ARROW_ENTITY, (context) -> new IronKidArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.GOLDEN_KID_ARROW_ENTITY, (context) -> new GoldenKidArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.SPECTRAL_KID_ARROW_ENTITY, (context) -> new SpectralKidArrowEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.DIAMOND_KID_ARROW_ENTITY, (context) -> new DiamondKidArrowEntityRenderer(context));

        ProgressiveArchery.LOGGER.debug("Registering Mod Entity Renderers for " + ProgressiveArchery.MOD_ID);
        
    }
    
}
