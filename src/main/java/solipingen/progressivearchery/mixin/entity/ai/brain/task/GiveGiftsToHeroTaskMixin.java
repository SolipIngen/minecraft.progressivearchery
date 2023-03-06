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
import net.minecraft.loot.context.LootContext.*;
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
    private void injectedModProfessionGifts(VillagerEntity villager, CallbackInfoReturnable<List<ItemStack>> value) {
        VillagerProfession villagerProfession = villager.getVillagerData().getProfession();
        if (villagerProfession == VillagerProfession.FLETCHER) {
            LootTable lootTable = villager.world.getServer().getLootManager().getTable(HERO_OF_THE_VILLAGE_FLETCHER_GIFT);
            Builder builder = new Builder((ServerWorld)villager.world).parameter(LootContextParameters.ORIGIN, villager.getPos()).parameter(LootContextParameters.THIS_ENTITY, villager).random(villager.getRandom());
            value.setReturnValue(lootTable.generateLoot(builder.build(LootContextTypes.GIFT)));
        }
    }    

}
