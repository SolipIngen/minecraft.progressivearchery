package solipingen.progressivearchery.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.entity.projectile.arrow.CopperArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.DiamondArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.FlintArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.GoldenArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.IronArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.SpectralArrowEntity;
import solipingen.progressivearchery.entity.projectile.arrow.WoodenArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.CopperKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.DiamondKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.FlintKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.IronKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.SpectralKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.WoodenKidArrowEntity;


public class ModEntityTypes {
    
// Arrows
    public static final EntityType<WoodenArrowEntity> WOODEN_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "wooden_arrow"),
        FabricEntityTypeBuilder.<WoodenArrowEntity>create(SpawnGroup.MISC, WoodenArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build()
        );

    public static final EntityType<FlintArrowEntity> FLINT_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "flint_arrow"),
        FabricEntityTypeBuilder.<FlintArrowEntity>create(SpawnGroup.MISC, FlintArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build()
        );

    public static final EntityType<CopperArrowEntity> COPPER_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "copper_arrow"),
        FabricEntityTypeBuilder.<CopperArrowEntity>create(SpawnGroup.MISC, CopperArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build()
        );
    
    public static final EntityType<IronArrowEntity> IRON_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "iron_arrow"),
        FabricEntityTypeBuilder.<IronArrowEntity>create(SpawnGroup.MISC, IronArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build()
        );

    public static final EntityType<GoldenArrowEntity> GOLDEN_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "golden_arrow"),
        FabricEntityTypeBuilder.<GoldenArrowEntity>create(SpawnGroup.MISC, GoldenArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build()
        );

    public static final EntityType<SpectralArrowEntity> SPECTRAL_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "spectral_arrow"),
        FabricEntityTypeBuilder.<SpectralArrowEntity>create(SpawnGroup.MISC, SpectralArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build()
        );

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "diamond_arrow"),
        FabricEntityTypeBuilder.<DiamondArrowEntity>create(SpawnGroup.MISC, DiamondArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build()
        );

// Kid Arrows
    public static final EntityType<WoodenKidArrowEntity> WOODEN_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "wooden_kid_arrow"),
        FabricEntityTypeBuilder.<WoodenKidArrowEntity>create(SpawnGroup.MISC, WoodenKidArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
        );

    public static final EntityType<FlintKidArrowEntity> FLINT_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "flint_kid_arrow"),
        FabricEntityTypeBuilder.<FlintKidArrowEntity>create(SpawnGroup.MISC, FlintKidArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
        );

    public static final EntityType<CopperKidArrowEntity> COPPER_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "copper_kid_arrow"),
        FabricEntityTypeBuilder.<CopperKidArrowEntity>create(SpawnGroup.MISC, CopperKidArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
        );
    
    public static final EntityType<IronKidArrowEntity> IRON_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "iron_kid_arrow"),
        FabricEntityTypeBuilder.<IronKidArrowEntity>create(SpawnGroup.MISC, IronKidArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
        );
    
    public static final EntityType<GoldenKidArrowEntity> GOLDEN_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "golden_kid_arrow"),
        FabricEntityTypeBuilder.<GoldenKidArrowEntity>create(SpawnGroup.MISC, GoldenKidArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
        );
    
    public static final EntityType<SpectralKidArrowEntity> SPECTRAL_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "spectral_kid_arrow"),
        FabricEntityTypeBuilder.<SpectralKidArrowEntity>create(SpawnGroup.MISC, SpectralKidArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
        );
    
    public static final EntityType<DiamondKidArrowEntity> DIAMOND_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(ProgressiveArchery.MOD_ID, "diamond_kid_arrow"),
        FabricEntityTypeBuilder.<DiamondKidArrowEntity>create(SpawnGroup.MISC, DiamondKidArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
        );


    public static void registerModEntities() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Entity Types for " + ProgressiveArchery.MOD_ID);
    }


}