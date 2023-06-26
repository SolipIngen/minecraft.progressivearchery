package solipingen.progressivearchery.mixin.client.render.entity;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.StriderEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.StriderEntityModel;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.render.entity.feature.StriderhairFeatureRenderer;
import solipingen.progressivearchery.client.render.entity.feature.model.StriderhairEntityModel;


@Mixin(StriderEntityRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class StriderEntityRendererMixin extends MobEntityRenderer<StriderEntity, StriderEntityModel<StriderEntity>> {
    private static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/strider/strider.png");
    private static final Identifier COLD_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "textures/entity/strider/strider_cold.png");


    public StriderEntityRendererMixin(Context context, StriderEntityModel<StriderEntity> entityModel, float f) {
        super(context, entityModel, f);
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectedInit(EntityRendererFactory.Context context, CallbackInfo cbi) {
        this.addFeature(new StriderhairFeatureRenderer(this, new StriderhairEntityModel<>(context.getPart(EntityModelLayers.STRIDER_SADDLE))));
    }

    @Redirect(method = "getTexture", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/StriderEntityRenderer;TEXTURE:Lnet/minecraft/util/Identifier;", opcode = Opcodes.GETSTATIC))
    private Identifier redirectedTexture() {
        return TEXTURE;
    }

    @Redirect(method = "getTexture", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/StriderEntityRenderer;COLD_TEXTURE:Lnet/minecraft/util/Identifier;", opcode = Opcodes.GETSTATIC))
    private Identifier redirectedColdTexture() {
        return COLD_TEXTURE;
    }


}
