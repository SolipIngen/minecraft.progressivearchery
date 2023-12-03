package solipingen.progressivearchery.mixin.entity.ai.brain.task;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.HoldTradeOffersTask;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import solipingen.progressivearchery.village.ModVillagerProfessions;


@Mixin(HoldTradeOffersTask.class)
public abstract class HoldTradeOffersTaskMixin extends MultiTickTask<VillagerEntity> {


    public HoldTradeOffersTaskMixin(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
        super(requiredMemoryState);
    }

    @Inject(method = "shouldRun", at = @At("HEAD"), cancellable = true)
    private void injectedShouldRun(ServerWorld serverWorld, VillagerEntity villagerEntity, CallbackInfoReturnable<Boolean> cbireturn) {
        if (villagerEntity.getVillagerData().getProfession() == ModVillagerProfessions.ARCHER) {
            cbireturn.setReturnValue(false);
        }
    }
    


}
