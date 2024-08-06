package solipingen.progressivearchery.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.ModItems;

import java.util.List;


public class ModifyLootTableHandler implements LootTableEvents.Modify {


    // Mob Drop Loot
    private static final RegistryKey<LootTable> SKELETON_ENTITY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("entities/skeleton"));
    private static final RegistryKey<LootTable> STRAY_ENTITY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("entities/stray"));
    private static final RegistryKey<LootTable> BOGGED_ENTITY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("entities/bogged"));
    private static final RegistryKey<LootTable> PILLAGER_ENTITY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("entities/pillager"));
    private static final RegistryKey<LootTable> PIGLIN_ENTITY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("entities/piglin"));

    // Template Added Tables
    private static final List<RegistryKey<LootTable>> TEMPLATE_ADDED= List.of(
            LootTables.VILLAGE_FLETCHER_CHEST,
            SKELETON_ENTITY, STRAY_ENTITY, BOGGED_ENTITY, PILLAGER_ENTITY, PIGLIN_ENTITY,
            LootTables.FISHING_TREASURE_GAMEPLAY);


    @Override
    public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        for (RegistryKey<LootTable> modKey : TEMPLATE_ADDED) {
            if (modKey.getValue().equals(key.getValue())) {
                float probability = key.getValue().getPath().startsWith("entities") ? 0.10f : (key.getValue().getPath().startsWith("gameplay") ? 0.15f : 0.5f);
                LootPool.Builder bowFusionPoolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(probability))
                        .with(ItemEntry.builder(ModItems.BOW_FUSION_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)).build());
                tableBuilder.pool(bowFusionPoolBuilder.build());
            }
        }
    }

}
