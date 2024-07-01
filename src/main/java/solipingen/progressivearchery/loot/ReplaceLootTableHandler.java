package solipingen.progressivearchery.loot;

import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootTable;

import java.util.List;


public class ReplaceLootTableHandler implements LootTableEvents.Replace {

    private static final List<RegistryKey<LootTable>> LOOT_TABLE_LIST = List.of(
            LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST,
            LootTables.PILLAGER_OUTPOST_CHEST, LootTables.BASTION_OTHER_CHEST, LootTables.BASTION_BRIDGE_CHEST, LootTables.BASTION_HOGLIN_STABLE_CHEST,
            LootTables.VILLAGE_FLETCHER_CHEST,
            LootTables.TRIAL_CHAMBERS_CHAMBER_DISPENSER, LootTables.TRIAL_CHAMBERS_CORRIDOR_DISPENSER,
            LootTables.TRIAL_CHAMBERS_ENTRANCE_CHEST, LootTables.TRIAL_CHAMBERS_SUPPLY_CHEST, LootTables.TRIAL_CHAMBERS_CORRIDOR_POT);


    @Override
    @Nullable
    public LootTable replaceLootTable(RegistryKey<LootTable> key, LootTable original, LootTableSource source) {
//        Identifier id = key.getValue();
//        for (Identifier modIdentifier : ID_ARRAY) {
//            if (modIdentifier.getPath().matches(id.getPath()) && (source.isBuiltin() || source == LootTableSource.DATA_PACK)) {
//                original.
//                LootTable newTable = lootManager.getLootTable(modIdentifier);
//                return newTable;
//            }
//        }
        return null;
    }

}
