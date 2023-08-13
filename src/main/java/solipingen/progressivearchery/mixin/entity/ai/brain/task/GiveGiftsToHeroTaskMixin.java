package solipingen.progressivearchery.mixin.entity.ai.brain.task;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.GiveGiftsToHeroTask;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import solipingen.progressivearchery.ProgressiveArchery;


@Mixin(GiveGiftsToHeroTask.class)
public abstract class GiveGiftsToHeroTaskMixin extends MultiTickTask<VillagerEntity> {
    private static final Identifier HERO_OF_THE_VILLAGE_FLETCHER_GIFT = new Identifier(ProgressiveArchery.MOD_ID, "gameplay/hero_of_the_village/fletcher_gift");

    
    public GiveGiftsToHeroTaskMixin(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
        super(requiredMemoryState);
    }

    @Inject(method = "getGifts", at = @At("HEAD"), cancellable = true)
    private void injectedModProfessionGifts(VillagerEntity villager, CallbackInfoReturnable<List<ItemStack>> cbireturn) {
        VillagerProfession villagerProfession = villager.getVillagerData().getProfession();
        if (villagerProfession == VillagerProfession.FLETCHER) {
            LootTable lootTable = villager.getWorld().getServer().getLootManager().getLootTable(HERO_OF_THE_VILLAGE_FLETCHER_GIFT);
            LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder((ServerWorld)villager.getWorld()).add(LootContextParameters.ORIGIN, villager.getPos()).add(LootContextParameters.THIS_ENTITY, villager).build(LootContextTypes.GIFT);
            cbireturn.setReturnValue(lootTable.generateLoot(lootContextParameterSet));
        }
    }    

}
