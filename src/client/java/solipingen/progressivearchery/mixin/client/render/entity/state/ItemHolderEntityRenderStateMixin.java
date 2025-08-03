package solipingen.progressivearchery.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Arm;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.ItemHolderEntityRenderStateInterface;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.village.ModVillagerProfessions;


@Mixin(ItemHolderEntityRenderState.class)
@Environment(EnvType.CLIENT)
public abstract class ItemHolderEntityRenderStateMixin extends LivingEntityRenderState implements ItemHolderEntityRenderStateInterface {
    @Unique private boolean isHoldingLongbow;


    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private static void injectedUpdate(LivingEntity entity, ItemHolderEntityRenderState state, ItemModelManager itemModelManager, CallbackInfo cbi) {
        if (entity instanceof VillagerDataContainer villagerDataContainer) {
            VillagerProfession profession = villagerDataContainer.getVillagerData().profession().value();
            ((ItemHolderEntityRenderStateInterface)state).setIsHoldingLongbow(entity.isHolding(stack -> stack.getItem() instanceof ModBowItem modBowItem && modBowItem.getBowType() == 2));
            if (profession == ModVillagerProfessions.ARCHER) {
                if (entity.getMainArm() == Arm.LEFT) {
                    itemModelManager.updateForLivingEntity(state.itemRenderState, entity.getMainHandStack(), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
                }
                else {
                    itemModelManager.updateForLivingEntity(state.itemRenderState, entity.getMainHandStack(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
                }
                cbi.cancel();
            }
        }
    }

    @Override
    public boolean getIsHoldingLongbow() {
        return this.isHoldingLongbow;
    }

    public void setIsHoldingLongbow(boolean isHoldingLongbow) {
        this.isHoldingLongbow = isHoldingLongbow;
    }

}
