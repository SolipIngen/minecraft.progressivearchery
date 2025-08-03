package solipingen.progressivearchery.mixin.client.render.entity.feature;

import java.util.Collection;

import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.VillagerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.math.RotationAxis;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.ItemHolderEntityRenderStateInterface;
import solipingen.progressivearchery.village.ModVillagerProfessions;


@Mixin(VillagerHeldItemFeatureRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class VillagerHeldItemFeatureRendererMixin<S extends ItemHolderEntityRenderState, M extends EntityModel<S> & ModelWithHat> extends FeatureRenderer<S, M> {


    public VillagerHeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method = "applyTransforms", at = @At("TAIL"))
    private void injectedApplyTransforms(S state, MatrixStack matrices, CallbackInfo cbi) {
        if (state instanceof VillagerEntityRenderState villagerRenderState && villagerRenderState.getVillagerData() != null) {
            if (villagerRenderState.getVillagerData().profession().value() == ModVillagerProfessions.ARCHER) {
                if (!((ItemHolderEntityRenderStateInterface)state).getIsHoldingLongbow()) {
                    matrices.scale(-1.0f, -1.0f, 1.0f);
                }
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(((ItemHolderEntityRenderStateInterface)state).getIsHoldingLongbow() ?
                        160.0f : 180.0f));
                matrices.translate(0.0, 0.2, -0.15);
                if (VillagerHeldItemFeatureRendererMixin.isFreshAnimationsEnabled()) {
                    EntityModel<S> entityModel = this.getContextModel();
                    if (entityModel.getParts().stream().anyMatch(modelPart -> modelPart.hasChild("arms"))) {
                        ModelPart freshArmsModel = entityModel.getParts().stream().findAny().orElseThrow().getChild("arms");
                        matrices.translate(0.0f, -0.05f*freshArmsModel.originY, 0.0f);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotation(freshArmsModel.pitch));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(freshArmsModel.yaw));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(freshArmsModel.roll));
                    }
                }
            }
        }
    }

    @Unique
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
