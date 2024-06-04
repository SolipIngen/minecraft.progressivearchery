package solipingen.progressivearchery.mixin.client.render.entity.feature;

import java.util.Collection;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import solipingen.progressivearchery.village.ModVillagerProfessions;


@Mixin(VillagerHeldItemFeatureRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class VillagerHeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    @Shadow @Final private HeldItemRenderer heldItemRenderer;


    public VillagerHeldItemFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER), cancellable = true)
    private void injectedRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo cbi) {
        ItemStack itemStack = ((LivingEntity)livingEntity).getEquippedStack(EquipmentSlot.MAINHAND);
        if (livingEntity instanceof VillagerEntity) {
            VillagerEntity villager = (VillagerEntity)livingEntity;
            if (!itemStack.isEmpty() && (villager.getVillagerData().getProfession() == ModVillagerProfessions.ARCHER)) {
                matrixStack.scale(-1.0f, -1.0f, 1.0f);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
                matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(10.0f));
                matrixStack.translate(0.0, 0.2, -0.15);
                if (VillagerHeldItemFeatureRendererMixin.isFreshAnimationsEnabled()) {
                    EntityModel<T> entityModel = this.getContextModel();
                    if (((VillagerResemblingModel<T>)entityModel).getPart().hasChild("arms")) {
                        ModelPart freshArmsModel = ((VillagerResemblingModel<T>)entityModel).getPart().getChild("arms");
                        matrixStack.translate(0.0f, -0.05f*freshArmsModel.pivotY, 0.0f);
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(freshArmsModel.pitch));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(freshArmsModel.yaw));
                        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(freshArmsModel.roll));
                    }
                }
                if (villager.getMainArm() == Arm.LEFT) {
                    this.heldItemRenderer.renderItem((LivingEntity)livingEntity, itemStack, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, true, matrixStack, vertexConsumerProvider, i);
                }
                else {
                    this.heldItemRenderer.renderItem((LivingEntity)livingEntity, itemStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
                }
                matrixStack.pop();
                cbi.cancel();
            }
        }
    }

    private static boolean isFreshAnimationsEnabled() {
        boolean freshAnimationsEnabled = false;
        ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
        Collection<String> enabledResourcePackNames = resourcePackManager.getEnabledIds();
        for (String enabledPackName : enabledResourcePackNames) {
            if (enabledPackName.contains("FreshAnimations")) {
                freshAnimationsEnabled |= true;
            }
        }
        return freshAnimationsEnabled;
   }

    
}
