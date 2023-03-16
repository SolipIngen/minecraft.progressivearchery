package solipingen.progressivearchery.client.render.entity.feature.model;

import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.math.MathHelper;
import net.fabricmc.api.EnvType;


@Environment(value=EnvType.CLIENT)
public class StriderhairEntityModel<T extends StriderEntity> extends SinglePartEntityModel<T> {
    private static final String RIGHT_BOTTOM_HAIR = "right_bottom_bristle";
    private static final String RIGHT_MIDDLE_HAIR = "right_middle_bristle";
    private static final String RIGHT_TOP_HAIR = "right_top_bristle";
    private static final String LEFT_TOP_HAIR = "left_top_bristle";
    private static final String LEFT_MIDDLE_HAIR = "left_middle_bristle";
    private static final String LEFT_BOTTOM_HAIR = "left_bottom_bristle";

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightBottomHair;
    private final ModelPart rightMiddleHair;
    private final ModelPart rightTopHair;
    private final ModelPart leftTopHair;
    private final ModelPart leftMiddleHair;
    private final ModelPart leftBottomHair;

    
    public StriderhairEntityModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.rightBottomHair = this.body.getChild(RIGHT_BOTTOM_HAIR);
        this.rightMiddleHair = this.body.getChild(RIGHT_MIDDLE_HAIR);
        this.rightTopHair = this.body.getChild(RIGHT_TOP_HAIR);
        this.leftTopHair = this.body.getChild(LEFT_TOP_HAIR);
        this.leftMiddleHair = this.body.getChild(LEFT_MIDDLE_HAIR);
        this.leftBottomHair = this.body.getChild(LEFT_BOTTOM_HAIR);

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0f, -6.0f, -8.0f, 16.0f, 14.0f, 16.0f), ModelTransform.pivot(0.0f, 1.0f, 0.0f));
        modelPartData2.addChild(RIGHT_BOTTOM_HAIR, ModelPartBuilder.create().uv(16, 65).cuboid(-12.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, true), ModelTransform.of(-8.0f, 4.0f, -8.0f, 0.0f, 0.0f, -1.2217305f));
        modelPartData2.addChild(RIGHT_MIDDLE_HAIR, ModelPartBuilder.create().uv(16, 49).cuboid(-12.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, true), ModelTransform.of(-8.0f, -1.0f, -8.0f, 0.0f, 0.0f, -1.134464f));
        modelPartData2.addChild(RIGHT_TOP_HAIR, ModelPartBuilder.create().uv(16, 33).cuboid(-12.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, true), ModelTransform.of(-8.0f, -5.0f, -8.0f, 0.0f, 0.0f, -0.87266463f));
        modelPartData2.addChild(LEFT_TOP_HAIR, ModelPartBuilder.create().uv(16, 33).cuboid(0.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f), ModelTransform.of(8.0f, -6.0f, -8.0f, 0.0f, 0.0f, 0.87266463f));
        modelPartData2.addChild(LEFT_MIDDLE_HAIR, ModelPartBuilder.create().uv(16, 49).cuboid(0.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f), ModelTransform.of(8.0f, -2.0f, -8.0f, 0.0f, 0.0f, 1.134464f));
        modelPartData2.addChild(LEFT_BOTTOM_HAIR, ModelPartBuilder.create().uv(16, 65).cuboid(0.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f), ModelTransform.of(8.0f, 3.0f, -8.0f, 0.0f, 0.0f, 1.2217305f));
        return TexturedModelData.of(modelData, 64, 128);
    }

    @Override
    public void setAngles(StriderEntity striderEntity, float f, float g, float h, float i, float j) {
        g = Math.min(0.25f, g);
        if (!striderEntity.hasPassengers()) {
            this.body.pitch = j * ((float)Math.PI / 180);
            this.body.yaw = i * ((float)Math.PI / 180);
        } else {
            this.body.pitch = 0.0f;
            this.body.yaw = 0.0f;
        }
        this.body.roll = 0.1f * MathHelper.sin(f * 1.5f) * 4.0f * g;
        this.body.pivotY = 2.0f;
        this.body.pivotY -= 2.0f * MathHelper.cos(f * 1.5f) * 2.0f * g;
        this.rightBottomHair.roll = -1.2217305f;
        this.rightMiddleHair.roll = -1.134464f;
        this.rightTopHair.roll = -0.87266463f;
        this.leftTopHair.roll = 0.87266463f;
        this.leftMiddleHair.roll = 1.134464f;
        this.leftBottomHair.roll = 1.2217305f;
        float l = MathHelper.cos(f * 1.5f + (float)Math.PI) * g;
        this.rightBottomHair.roll += l * 1.3f;
        this.rightMiddleHair.roll += l * 1.2f;
        this.rightTopHair.roll += l * 0.6f;
        this.leftTopHair.roll += l * 0.6f;
        this.leftMiddleHair.roll += l * 1.2f;
        this.leftBottomHair.roll += l * 1.3f;
        this.rightBottomHair.roll += 0.05f * MathHelper.sin(h * 1.0f * -0.4f);
        this.rightMiddleHair.roll += 0.1f * MathHelper.sin(h * 1.0f * 0.2f);
        this.rightTopHair.roll += 0.1f * MathHelper.sin(h * 1.0f * 0.4f);
        this.leftTopHair.roll += 0.1f * MathHelper.sin(h * 1.0f * 0.4f);
        this.leftMiddleHair.roll += 0.1f * MathHelper.sin(h * 1.0f * 0.2f);
        this.leftBottomHair.roll += 0.05f * MathHelper.sin(h * 1.0f * -0.4f);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
    
}
