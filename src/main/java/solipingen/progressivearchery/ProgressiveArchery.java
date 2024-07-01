package solipingen.progressivearchery;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;
import solipingen.progressivearchery.block.ModBlocks;
import solipingen.progressivearchery.block.entity.ModBlockEntityTypes;
import solipingen.progressivearchery.component.type.ModDataComponentTypes;
import solipingen.progressivearchery.entity.ModEntityTypes;
import solipingen.progressivearchery.item.ModItemGroups;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.ModifyItemComponentHandler;
import solipingen.progressivearchery.loot.ModifyLootTableHandler;
import solipingen.progressivearchery.loot.ReplaceLootTableHandler;
import solipingen.progressivearchery.recipe.ModRecipes;
import solipingen.progressivearchery.resource.ModDataPacks;
import solipingen.progressivearchery.screen.ModScreenHandlers;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.village.ModVillagerProfessions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProgressiveArchery implements ModInitializer {
	
	public static final String MOD_ID = "progressivearchery";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	
	@Override
	public void onInitialize() {

		ModBlocks.registerModBlocks();
		ModBlockEntityTypes.registerModBlockEntities();
		ModCriteria.registerModAdvancementCriteria();
		ModDataComponentTypes.registerModDataComponentTypes();
		ModDataPacks.registerModDataPacks();
		ModEntityTypes.registerModEntities();
		ModItems.registerModItems();
		ModItemGroups.registerModItemsToVanillaGroups();
		ModRecipes.registerModRecipes();
		ModSoundEvents.registerModSoundEvents();
		ModScreenHandlers.registerScreenHandlers();
		ModVillagerProfessions.registerModVillagerProfessions();

		DefaultItemComponentEvents.MODIFY.register(new ModifyItemComponentHandler());

		LootTableEvents.REPLACE.register(new ReplaceLootTableHandler());
		LootTableEvents.MODIFY.register(new ModifyLootTableHandler());

	}
	
}
