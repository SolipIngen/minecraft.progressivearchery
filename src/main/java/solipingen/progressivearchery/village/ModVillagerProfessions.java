package solipingen.progressivearchery.village;

import java.util.Map;

import net.fabricmc.fabric.api.registry.VillagerInteractionRegistries;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffers.BuyItemFactory;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.TradeOffers.SellEnchantedToolFactory;
import net.minecraft.village.TradeOffers.SellItemFactory;
import net.minecraft.village.TradeOffers.SellPotionHoldingItemFactory;
import net.minecraft.world.poi.PointOfInterestType;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.item.ModItems;


public class ModVillagerProfessions {

    public static final VillagerProfession ARCHER = ModVillagerProfessions.registerVillagerProfession("archer", 
        RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(ProgressiveArchery.MOD_ID, "archer_marker_poi")), null, null, null);

    public static final PointOfInterestType ARCHER_MARKER_POI = ModVillagerProfessions.registerPOI("archer_marker_poi", ModBlocks.ARCHER_MARKER);

    public static final Schedule VILLAGER_SCHEDULE_ARCHER = ModVillagerProfessions.registerSchedule("villager_archer")
            .withActivity(10, Activity.IDLE)
            .withActivity(2000, Activity.REST)
            .withActivity(9000, Activity.MEET)
            .withActivity(11000, Activity.IDLE)
            .withActivity(12000, Activity.WORK).build();


    // Trade Offer Replacements
    public static void replaceFletcherProfessionToLeveledTrade(Map<RegistryKey<VillagerProfession>, Int2ObjectMap<Factory[]>> originalTradeOffers) {
        originalTradeOffers.replace(VillagerProfession.FLETCHER, ModVillagerProfessions.copyToFastUtilMap(
            ImmutableMap.of(
                1, new Factory[]{new BuyItemFactory(Items.STICK, 32, 16, 2), new BuyItemFactory(Items.FEATHER, 24, 16, 2), 
                    new SellItemFactory(ModItems.WOODEN_ARROW, 1, 4, 1), new SellItemFactory(Items.BOW, 2, 1, 1), 
                    new SellItemFactory(ModItems.HORN_BOW, 3, 1, 1), new SellItemFactory(ModItems.LONGBOW, 4, 1, 1), 
                    new SellItemFactory(ModItems.TUBULAR_BOW, 4, 1, 1), new SellItemFactory(Items.CROSSBOW, 4, 1, 1)}, 
                2, new Factory[]{new BuyItemFactory(Items.FLINT, 26, 12, 10), new BuyItemFactory(Items.STRING, 14, 12, 10), 
                    new SellItemFactory(ModItems.FLINT_ARROW, 2, 4, 5), new SellItemFactory(ModItems.COPPER_FUSED_BOW, 5, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_FUSED_HORN_BOW, 6, 1, 5), new SellItemFactory(ModItems.COPPER_FUSED_LONGBOW, 7, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_FUSED_TUBULAR_BOW, 7, 1, 5), new SellItemFactory(ModItems.COPPER_FUSED_CROSSBOW, 7, 1, 5)}, 
                3, new Factory[]{new BuyItemFactory(ModItems.COPPER_ARROWHEAD, 18, 16, 20), new BuyItemFactory(ModItems.GOLDEN_ARROWHEAD, 12, 16, 20), 
                    new SellItemFactory(ModItems.COPPER_ARROW, 3, 4, 10), new SellItemFactory(ModItems.GOLDEN_ARROW, 4, 2, 10), 
                    new SellItemFactory(ModItems.GOLD_FUSED_BOW, 5, 1, 10), 
                    new SellItemFactory(ModItems.GOLD_FUSED_HORN_BOW, 6, 1, 10), new SellItemFactory(ModItems.GOLD_FUSED_LONGBOW, 7, 1, 10), 
                    new SellItemFactory(ModItems.GOLD_FUSED_TUBULAR_BOW, 7, 1, 10), new SellItemFactory(ModItems.GOLD_FUSED_CROSSBOW, 7, 1, 10)}, 
                4, new Factory[]{new BuyItemFactory(Items.GOAT_HORN, 1, 16, 30), new BuyItemFactory(ModItems.IRON_ARROWHEAD, 18, 16, 30), 
                    new SellPotionHoldingItemFactory(ModItems.GOLDEN_ARROW, 4, Items.TIPPED_ARROW, 4, 2, 12, 30), 
                    new SellItemFactory(ModItems.IRON_ARROW, 4, 4, 15), new SellItemFactory(ModItems.IRON_FUSED_BOW, 10, 1, 15), 
                    new SellItemFactory(ModItems.IRON_FUSED_HORN_BOW, 11, 1, 15), new SellItemFactory(ModItems.IRON_FUSED_LONGBOW, 12, 1, 15), 
                    new SellItemFactory(ModItems.IRON_FUSED_TUBULAR_BOW, 12, 1, 15), new SellItemFactory(ModItems.IRON_FUSED_CROSSBOW, 12, 1, 15)}, 
                5, new Factory[]{new BuyItemFactory(ModItems.DIAMOND_ARROWHEAD, 1, 16, 30), 
                    new SellItemFactory(ModItems.DIAMOND_ARROW, 4, 1, 8, 15), 
                    new SellEnchantedToolFactory(ModItems.DIAMOND_FUSED_BOW, 15, 3, 15), new SellEnchantedToolFactory(ModItems.DIAMOND_FUSED_HORN_BOW, 17, 3, 15), 
                    new SellEnchantedToolFactory(ModItems.DIAMOND_FUSED_LONGBOW, 17, 3, 15), new SellEnchantedToolFactory(ModItems.DIAMOND_FUSED_TUBULAR_BOW, 17, 3, 15),
                    new SellEnchantedToolFactory(ModItems.DIAMOND_FUSED_CROSSBOW, 17, 3, 15)}
                )));
    }

    private static VillagerProfession registerVillagerProfession(String name, RegistryKey<PointOfInterestType> poiType, @Nullable ImmutableSet<Item> gatherableItems, @Nullable ImmutableSet<Block> secondaryJobSites, @Nullable SoundEvent workSound) {
        ImmutableSet<Item> gatherableItemsSet = ImmutableSet.of();
        if (gatherableItems != null) {
            gatherableItemsSet = gatherableItems; 
        }
        ImmutableSet<Block> secondaryJobSitesSet = ImmutableSet.of();
        if (secondaryJobSites != null) {
            secondaryJobSitesSet = secondaryJobSites; 
        }
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(ProgressiveArchery.MOD_ID, name), 
            new VillagerProfession(Text.translatable("entity." + ProgressiveArchery.MOD_ID + ".villager." + name), entry -> entry.matchesKey(poiType), entry -> entry.matchesKey(poiType), gatherableItemsSet, secondaryJobSitesSet, workSound));
    }

    private static PointOfInterestType registerPOI(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(ProgressiveArchery.MOD_ID, name), 
            1, 1, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }

    protected static ScheduleBuilder registerSchedule(String id) {
        Schedule schedule = Registry.register(Registries.SCHEDULE, Identifier.of(ProgressiveArchery.MOD_ID, id), new Schedule());
        return new ScheduleBuilder(schedule);
    }

    public static void registerModVillagerProfessions() {
        VillagerInteractionRegistries.registerGiftLootTable(VillagerProfession.FLETCHER,
                RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(ProgressiveArchery.MOD_ID, "gameplay/hero_of_the_village/fletcher_gift")));
        ProgressiveArchery.LOGGER.debug("Registering Villagers for " + ProgressiveArchery.MOD_ID);
    }

    private static Int2ObjectMap<Factory[]> copyToFastUtilMap(ImmutableMap<Integer, Factory[]> map) {
        return new Int2ObjectOpenHashMap<Factory[]>(map);
    }


}
