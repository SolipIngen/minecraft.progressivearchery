package solipingen.progressivearchery.client.render.entity.model.projectile.kid_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.math.MathHelper;

import java.util.function.UnaryOperator;


@Environment(EnvType.CLIENT)
public class KidArrowEntityModel extends EntityModel<ProjectileEntityRenderState> {


    public KidArrowEntityModel(ModelPart modelPart) {
        super(modelPart, RenderLayer::getEntityCutout);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("back", ModelPartBuilder.create().uv(0, 0).cuboid(0.0f, -2.5f, -2.5f, 0.0f, 5.0f, 5.0f),
                ModelTransform.of(-6.0f, 0.0f, 0.0f, 0.7853982f, 0.0f, 0.0f).withScale(0.8f));
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(-12.0f, -2.0f, 0.0f, 16.0f, 4.0f, 0.0f, Dilation.NONE, 1.0f, 0.8f);
        modelPartData.addChild("cross_1", modelPartBuilder, ModelTransform.rotation(0.7853982f, 0.0f, 0.0f));
        modelPartData.addChild("cross_2", modelPartBuilder, ModelTransform.rotation(2.3561945f, 0.0f, 0.0f));
        return TexturedModelData.of(modelData.transform((UnaryOperator<ModelTransform>)(modelTransform -> modelTransform.scaled(0.9f))), 32, 32);
    }

    public void setAngles(ProjectileEntityRenderState projectileEntityRenderState) {
        super.setAngles(projectileEntityRenderState);
        if (projectileEntityRenderState.shake > 0.0f) {
            float f = -MathHelper.sin(projectileEntityRenderState.shake * 3.0f) * projectileEntityRenderState.shake;
            this.root.roll += f * 0.017453292f;
        }
    }


}
