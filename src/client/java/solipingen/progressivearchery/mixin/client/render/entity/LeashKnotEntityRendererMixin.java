package solipingen.progressivearchery.mixin.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LeashKnotEntityRenderer;
import net.minecraft.client.render.entity.model.LeashKnotEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.EntityRenderStateInterface;


@Mixin(LeashKnotEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class LeashKnotEntityRendererMixin extends EntityRenderer<LeashKnotEntity, EntityRenderState> {
    @Shadow @Final private LeashKnotEntityModel field_53192;
    @Unique private static final Identifier FIREPROOF_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "textures/entity/fireproof_lead_knot.png");


    protected LeashKnotEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0)
    private VertexConsumer modifiedVertexConsumer(VertexConsumer vertexConsumer, EntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (((EntityRenderStateInterface)state).getIsFireproofLeashed()) {
            return vertexConsumers.getBuffer(this.field_53192.getLayer(FIREPROOF_TEXTURE));
        }
        return vertexConsumer;
    }



}
