package solipingen.progressivearchery.mixin.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.EntityRenderState$LeashDataInterface;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.EntityRenderStateInterface;
import solipingen.progressivearchery.util.interfaces.mixin.entity.EntityInterface;


@Mixin(EntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {


    @Inject(method = "renderLeashSegment", at = @At("HEAD"), cancellable = true)
    private static void injectedRenderLeashSegment(VertexConsumer vertexConsumer, Matrix4f matrix, float leashedEntityX, float leashedEntityY, float leashedEntityZ, float f, float g, float h, float i, int segment, boolean bl, EntityRenderState.LeashData leashData, CallbackInfo cbi) {
        if (leashData != null && ((EntityRenderState$LeashDataInterface)leashData).getIsFireproofLeashed()) {
            float j = (float) segment / 24.0f;
            int k = (int) MathHelper.lerp(j, (float) leashData.leashedEntityBlockLight, (float) leashData.leashHolderBlockLight);
            int l = (int) MathHelper.lerp(j, (float) leashData.leashedEntitySkyLight, (float) leashData.leashHolderSkyLight);
            int m = LightmapTextureManager.pack(k, l);
            boolean darkBl = segment % 2 == (bl ? 1 : 0);
            float o = darkBl ? 205.0f/255.0f : 231.0f/255.0f;
            float p = darkBl ? 168.0f/255.0f : 205.0f/255.0f;
            float q = darkBl ? 134.0f/255.0f : 169.0f/255.0f;
            float r = leashedEntityX * j;
            float s;
            if (leashData.field_60161) {
                s = leashedEntityY > 0.0f ? leashedEntityY * j * j : leashedEntityY - leashedEntityY * (1.0f - j) * (1.0f - j);
            } else {
                s = leashedEntityY * j;
            }
            float t = leashedEntityZ * j;
            vertexConsumer.vertex(matrix, r - h, s + g, t + i).color(o, p, q, 1.0f).light(m);
            vertexConsumer.vertex(matrix, r + h, s + f - g, t - i).color(o, p, q, 1.0f).light(m);
            cbi.cancel();
        }
    }

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void injectedUpdateRenderState(T entity, S state, float tickProgress, CallbackInfo cbi) {
        if (entity instanceof LeashKnotEntity) {
            ((EntityRenderStateInterface)state).setIsFireproofLeashed(((EntityInterface)entity).isFireproofLeashed());
        }
    }

    @ModifyVariable(method = "updateRenderState", at = @At("STORE"), ordinal = 0)
    private EntityRenderState.LeashData modifiedLeashData(EntityRenderState.LeashData leashData, T entity, S state, float tickProgress) {
        if (entity instanceof Leashable leashable && leashable.getLeashData() != null) {
            ((EntityRenderState$LeashDataInterface)leashData).setIsFireproofLeashed(((EntityInterface)entity).isFireproofLeashed());
        }
        return leashData;
    }

    @ModifyVariable(method = "updateRenderState", at = @At("STORE"), ordinal = -1)
    private EntityRenderState.LeashData modifiedLeashData2(EntityRenderState.LeashData leashData2, T entity, S state, float tickProgress) {
        if (entity instanceof Leashable leashable && leashable.getLeashData() != null) {
            ((EntityRenderState$LeashDataInterface)leashData2).setIsFireproofLeashed(((EntityInterface)entity).isFireproofLeashed());
        }
        return leashData2;
    }


}
