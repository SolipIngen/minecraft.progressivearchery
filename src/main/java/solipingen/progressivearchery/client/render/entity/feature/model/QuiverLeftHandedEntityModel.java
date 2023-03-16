package solipingen.progressivearchery.client.render.entity.feature.model;

import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;


@Environment(value=EnvType.CLIENT)
public class QuiverLeftHandedEntityModel<T extends LivingEntity> extends AnimalModel<T> {
	private final ModelPart body;
	private final ModelPart rim;
	private final ModelPart belt;
	private final ModelPart arrows;

	
	public QuiverLeftHandedEntityModel(ModelPart root) {
		this.body = root.getChild("body");
		this.rim = root.getChild("rim");
		this.belt = root.getChild("belt");
		this.arrows = root.getChild("arrows");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("body", ModelPartBuilder.create()
			.uv(0, 0).cuboid(3.0f, -3.0f, 1.0f, 3.0f, 12.0f, 3.0f, new Dilation(0.0f)), ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7854f));
		modelPartData.addChild("rim", ModelPartBuilder.create()
			.uv(11, 13).cuboid(2.0f, -3.0f, 1.0f, 1.0f, 1.0f, 3.0f, new Dilation(0.0f))
			.uv(14, 9).cuboid(3.0f, -3.0f, 0.0f, 3.0f, 1.0f, 1.0f, new Dilation(0.0f))
			.uv(14, 5).cuboid(6.0f, -3.0f, 1.0f, 1.0f, 1.0f, 3.0f, new Dilation(0.0f))
			.uv(14, 11).cuboid(3.0f, -3.0f, 4.0f, 3.0f, 1.0f, 1.0f, new Dilation(0.0f)), ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7854f));
		modelPartData.addChild("belt", ModelPartBuilder.create()
			.uv(0, 0).cuboid(4.0f, -4.0f, 1.0f, 1.0f, 1.0f, 0.0f, new Dilation(0.0f))
			.uv(10, 0).cuboid(4.0f, -4.0f, -4.0f, 1.0f, 0.0f, 5.0f, new Dilation(0.0f))
			.uv(12, 0).cuboid(4.0f, -4.0f, -4.0f, 1.0f, 15.0f, 0.0f, new Dilation(0.0f))
			.uv(10, 4).cuboid(4.0f, 11.0f, -4.0f, 1.0f, 0.0f, 5.0f, new Dilation(0.0f))
			.uv(0, 15).cuboid(4.0f, 7.0f, 1.0f, 1.0f, 4.0f, 0.0f, new Dilation(0.0f)), ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7854f));
		modelPartData.addChild("arrows", ModelPartBuilder.create()
			.uv(0, 21).cuboid(4.0f, -5.0f, 1.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(3.0f, -5.0f, 1.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(5.0f, -5.0f, 2.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(4.0f, -5.0f, 2.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(3.0f, -5.0f, 2.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(5.0f, -5.0f, 3.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(4.0f, -5.0f, 3.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(3.0f, -5.0f, 3.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(0, 21).cuboid(5.0f, -5.0f, 1.0f, 1.0f, 3.0f, 1.0f, new Dilation(0.0f)), ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7854f));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
		float k = 0.0f;
        float l = 0.7854f;
        float m = 0.0f;
        float n = 0.0f;
		if (livingEntity.isInSneakingPose()) {
            k = 0.5f;
            l = 0.74f;
            m = 3.2f;
            n = 0.08726646f;
		}
		this.body.pivotY = m;
        this.body.pitch = k;
        this.body.roll = l;
        this.body.yaw = n;
		this.rim.pivotY = m;
        this.rim.pitch = k;
        this.rim.roll = l;
        this.rim.yaw = n;
		this.belt.pivotY = m;
        this.belt.pitch = k;
        this.belt.roll = l;
        this.belt.yaw = n;
		this.arrows.pivotY = m;
        this.arrows.pitch = k;
        this.arrows.roll = l;
        this.arrows.yaw = n;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.rim.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.belt.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.arrows.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.body, this.rim, this.belt, this.arrows);
	}
	
}