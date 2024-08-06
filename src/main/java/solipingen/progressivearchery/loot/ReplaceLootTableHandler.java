package solipingen.progressivearchery.loot;

import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
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
    public @Nullable LootTable replaceLootTable(RegistryKey<LootTable> key, LootTable original, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        // Fill in if necessary.
        return null;
    }

}
