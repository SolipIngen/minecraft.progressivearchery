package solipingen.progressivearchery.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.item.ModItems;


public class ModifyLootTableHandler implements LootTableEvents.Modify {

    // Structure Chest Loot
    private static final Identifier FLETCHER_CHEST_ID = new Identifier("chests/village/village_fletcher");
    private static final Identifier PILLAGER_OUTPOST_ID = new Identifier("chests/pillager_outpost");
    private static final Identifier BASTION_GENERIC_ID = new Identifier("chests/bastion_other");
    private static final Identifier BASTION_BRIDGE_ID = new Identifier("chests/bastion_bridge");
    private static final Identifier BASTION_HOGLIN_STABLE_ID = new Identifier("chests/bastion_hoglin_stable");

    // Mob Drop Loot
    private static final Identifier SKELETON_DROP_ID = new Identifier("entities/skeleton");
    private static final Identifier STRAY_DROP_ID = new Identifier("entities/stray");
    private static final Identifier PILLAGER_DROP_ID = new Identifier("entities/pillager");

    // Hero of the Village Gift Loot
    private static final Identifier HERO_OF_THE_VILLAGE_FLETCHER_ID = new Identifier("gameplay/hero_of_the_village/fletcher_gift");

    // Mod Loot Tables
    private static final Identifier[] ID_ARRAY = new Identifier[]{ 
        FLETCHER_CHEST_ID, PILLAGER_OUTPOST_ID, BASTION_GENERIC_ID, BASTION_BRIDGE_ID, BASTION_HOGLIN_STABLE_ID, 
        SKELETON_DROP_ID, STRAY_DROP_ID, PILLAGER_DROP_ID, 
        HERO_OF_THE_VILLAGE_FLETCHER_ID};


    @Override
    public void modifyLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable.Builder tableBuilder, LootTableSource source) {
        for (Identifier identifier : ID_ARRAY) {
            if (identifier.getPath().matches(id.getPath())) {
                float probability = identifier.getPath().startsWith("entities") ? 0.04f : (identifier.getPath().startsWith("gameplay") ? 0.12f : 0.33f);
                LootPool.Builder bowFusionPoolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceWithLootingLootCondition.builder(probability, identifier.getPath().startsWith("entities") ? 0.02f : 0.0f))
                    .with(ItemEntry.builder(ModItems.BOW_FUSION_SMITHING_TEMPLATE))
                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)).build());
                tableBuilder.pool(bowFusionPoolBuilder.build());
                LootPool.Builder trimPoolBuilder = LootPool.builder();
                LootPool.Builder upgradePoolBuilder = LootPool.builder();
                if (identifier.getPath().matches(PILLAGER_OUTPOST_ID.getPath())) {
                    trimPoolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(EmptyEntry.builder().weight(3))
                        .with(ItemEntry.builder(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0f)).build());
                    tableBuilder.pool(bowFusionPoolBuilder.build());
                }
                else if (identifier.getPath().matches(BASTION_HOGLIN_STABLE_ID.getPath()) || identifier.getPath().matches(BASTION_BRIDGE_ID.getPath()) || identifier.getPath().matches(BASTION_GENERIC_ID.getPath())) {
                    trimPoolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(EmptyEntry.builder().weight(11))
                        .with(ItemEntry.builder(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1));
                    upgradePoolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(EmptyEntry.builder().weight(9))
                        .with(ItemEntry.builder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).weight(1));
                    tableBuilder.pool(trimPoolBuilder.build()).pool(upgradePoolBuilder.build());
                }
            }
        }
    }
}
