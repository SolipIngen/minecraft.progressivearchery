package solipingen.progressivearchery.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
        Identifier.of(ProgressiveArchery.MOD_ID, "wooden_arrow"),
        EntityType.Builder.<WoodenArrowEntity>create(WoodenArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "wooden_arrow")))
        );

    public static final EntityType<FlintArrowEntity> FLINT_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "flint_arrow"),
        EntityType.Builder.<FlintArrowEntity>create(FlintArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "flint_arrow")))
        );

    public static final EntityType<CopperArrowEntity> COPPER_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "copper_arrow"),
        EntityType.Builder.<CopperArrowEntity>create(CopperArrowEntity::new,SpawnGroup.MISC)
                .dimensions(0.5f, 0.5f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "copper_arrow")))
        );
    
    public static final EntityType<IronArrowEntity> IRON_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "iron_arrow"),
        EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "iron_arrow")))
        );

    public static final EntityType<GoldenArrowEntity> GOLDEN_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "golden_arrow"),
        EntityType.Builder.<GoldenArrowEntity>create(GoldenArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "golden_arrow")))
        );

    public static final EntityType<SpectralArrowEntity> SPECTRAL_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "spectral_arrow"),
        EntityType.Builder.<SpectralArrowEntity>create(SpectralArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "spectral_arrow")))
        );

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "diamond_arrow"),
        EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "diamond_arrow")))
        );

// Kid Arrows
    public static final EntityType<WoodenKidArrowEntity> WOODEN_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "wooden_kid_arrow"),
        EntityType.Builder.<WoodenKidArrowEntity>create(WoodenKidArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "wooden_kid_arrow")))
        );

    public static final EntityType<FlintKidArrowEntity> FLINT_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "flint_kid_arrow"),
        EntityType.Builder.<FlintKidArrowEntity>create(FlintKidArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "flint_kid_arrow")))
        );

    public static final EntityType<CopperKidArrowEntity> COPPER_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "copper_kid_arrow"),
        EntityType.Builder.<CopperKidArrowEntity>create(CopperKidArrowEntity::new,SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "copper_kid_arrow")))
        );
    
    public static final EntityType<IronKidArrowEntity> IRON_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "iron_kid_arrow"),
        EntityType.Builder.<IronKidArrowEntity>create(IronKidArrowEntity::new,SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "iron_kid_arrow")))
        );
    
    public static final EntityType<GoldenKidArrowEntity> GOLDEN_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "golden_kid_arrow"),
        EntityType.Builder.<GoldenKidArrowEntity>create(GoldenKidArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "golden_kid_arrow")))
        );
    
    public static final EntityType<SpectralKidArrowEntity> SPECTRAL_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "spectral_kid_arrow"),
        EntityType.Builder.<SpectralKidArrowEntity>create(SpectralKidArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "spectral_kid_arrow")))
        );
    
    public static final EntityType<DiamondKidArrowEntity> DIAMOND_KID_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(ProgressiveArchery.MOD_ID, "diamond_kid_arrow"),
        EntityType.Builder.<DiamondKidArrowEntity>create(DiamondKidArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,Identifier.of(ProgressiveArchery.MOD_ID, "diamond_kid_arrow")))
        );


    public static void registerModEntities() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Entity Types for " + ProgressiveArchery.MOD_ID);
    }


}