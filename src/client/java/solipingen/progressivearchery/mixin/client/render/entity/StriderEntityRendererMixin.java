package solipingen.progressivearchery.mixin.client.render.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.StriderEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.StriderEntityModel;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.util.interfaces.mixin.entity.passive.StriderEntityInterface;


@Mixin(StriderEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class StriderEntityRendererMixin extends MobEntityRenderer<StriderEntity, StriderEntityModel<StriderEntity>> {
    private static final Identifier SHEARED_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/strider/strider_sheared.png");
    private static final Identifier COLD_SHEARED_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/strider/strider_sheared_cold.png");


    public StriderEntityRendererMixin(Context context, StriderEntityModel<StriderEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "getTexture(Lnet/minecraft/entity/passive/StriderEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    private void injectedTexture(StriderEntity striderEntity, CallbackInfoReturnable<Identifier> cbireturn) {
        if (((StriderEntityInterface)(striderEntity)).getIsSheared()) {
            cbireturn.setReturnValue(striderEntity.isCold() ? COLD_SHEARED_TEXTURE : SHEARED_TEXTURE);
        }
    }


}
