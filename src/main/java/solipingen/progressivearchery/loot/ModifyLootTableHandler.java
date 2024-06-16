package solipingen.progressivearchery.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.ModItems;


public class ModifyLootTableHandler implements LootTableEvents.Modify {


    // Mob Drop Loot
    private static final Identifier SKELETON_DROP_ID = new Identifier("entities/skeleton");
    private static final Identifier STRAY_DROP_ID = new Identifier("entities/stray");
    private static final Identifier BOGGED_DROP_ID = new Identifier("entities/bogged");
    private static final Identifier PILLAGER_DROP_ID = new Identifier("entities/pillager");

    // Mod Loot Tables
    private static final Identifier[] ID_ARRAY = new Identifier[]{
        SKELETON_DROP_ID, STRAY_DROP_ID, BOGGED_DROP_ID, PILLAGER_DROP_ID};


    @Override
    public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source) {
        Identifier id = key.getValue();
        for (Identifier identifier : ID_ARRAY) {
            if (identifier.getPath().matches(id.getPath())) {
                float probability = identifier.getPath().startsWith("entities") ? 0.12f : (identifier.getPath().startsWith("gameplay") ? 0.15f : 0.33f);
                LootPool.Builder bowFusionPoolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceWithLootingLootCondition.builder(probability, identifier.getPath().startsWith("entities") ? 0.03f : 0.0f))
                    .with(ItemEntry.builder(ModItems.BOW_FUSION_SMITHING_TEMPLATE))
                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)).build());
                tableBuilder.pool(bowFusionPoolBuilder.build());
            }
        }
    }
}
