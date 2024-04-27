package solipingen.progressivearchery.loot;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ReplaceLootTableHandler implements LootTableEvents.Replace {

    // Structure Chest Loot
    private static final Identifier JUNGLE_TEMPLE_DISPENSER_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/jungle_temple_dispenser");
    private static final Identifier FLETCHER_CHEST_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/village/village_fletcher");
    private static final Identifier PILLAGER_OUTPOST_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/pillager_outpost");
    private static final Identifier BASTION_GENERIC_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/bastion_other");
    private static final Identifier BASTION_BRIDGE_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/bastion_bridge");
    private static final Identifier BASTION_HOGLIN_STABLE_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/bastion_hoglin_stable");

    // Mob Drop Loot
    private static final Identifier SKELETON_DROP_ID = new Identifier(ProgressiveArchery.MOD_ID, "entities/skeleton");
    private static final Identifier STRAY_DROP_ID = new Identifier(ProgressiveArchery.MOD_ID, "entities/stray");
    private static final Identifier PILLAGER_DROP_ID = new Identifier(ProgressiveArchery.MOD_ID, "entities/pillager");

    // Fishing Treasure Loot
    private static final Identifier FISHING_TREASURE_ID = new Identifier(ProgressiveArchery.MOD_ID, "gameplay/fishing/treasure");

    // Trial Chamber Loot
    private static final Identifier TRIAL_CHAMBERS_DISPENSER_CHAMBER_ID = new Identifier(ProgressiveArchery.MOD_ID, "dispensers/trial_chambers/chamber");
    private static final Identifier TRIAL_CHAMBERS_DISPENSER_CORRIDOR_ID = new Identifier(ProgressiveArchery.MOD_ID, "dispensers/trial_chambers/corridor");
    private static final Identifier TRIAL_CHAMBERS_CHEST_ENTRANCE_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/trial_chambers/entrance");
    private static final Identifier TRIAL_CHAMBERS_CHEST_SUPPLY_ID = new Identifier(ProgressiveArchery.MOD_ID, "chests/trial_chambers/supply");
    private static final Identifier TRIAL_CHAMBERS_POT_CORRIDOR_ID = new Identifier(ProgressiveArchery.MOD_ID, "pots/trial_chambers/corridor");

    // Mod Loot Tables
    private static final Identifier[] ID_ARRAY = new Identifier[]{ 
        JUNGLE_TEMPLE_DISPENSER_ID, FLETCHER_CHEST_ID, PILLAGER_OUTPOST_ID, BASTION_GENERIC_ID, BASTION_BRIDGE_ID, BASTION_HOGLIN_STABLE_ID, 
        SKELETON_DROP_ID, STRAY_DROP_ID, PILLAGER_DROP_ID, FISHING_TREASURE_ID, 
        TRIAL_CHAMBERS_DISPENSER_CHAMBER_ID, TRIAL_CHAMBERS_DISPENSER_CORRIDOR_ID, TRIAL_CHAMBERS_CHEST_ENTRANCE_ID, TRIAL_CHAMBERS_CHEST_SUPPLY_ID, TRIAL_CHAMBERS_POT_CORRIDOR_ID};


    @Override
    @Nullable
    public LootTable replaceLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable original, LootTableSource source) {
        for (Identifier modIdentifier : ID_ARRAY) {
            if (modIdentifier.getPath().matches(id.getPath()) && (source.isBuiltin() || source == LootTableSource.DATA_PACK)) {
                LootTable newTable = lootManager.getLootTable(modIdentifier);
                return newTable;
            }
        }
        return null;
    }

}
