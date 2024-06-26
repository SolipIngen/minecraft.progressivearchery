package solipingen.progressivearchery.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup.StackVisibility;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.block.ModBlocks;


public class ModItemGroups {

    public static void registerModItemsToVanillaGroups() {

        // Arrows
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.WOODEN_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.FLINT_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLDEN_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_ARROW));

        // Kid Arrows
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.WOODEN_KID_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.FLINT_KID_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_KID_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_KID_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLDEN_KID_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries ->
            ModItemGroups.addPotions(entries, ModItems.TIPPED_KID_ARROW, StackVisibility.PARENT_AND_SEARCH_TABS));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.SPECTRAL_KID_ARROW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_KID_ARROW));

        // Bows
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_FUSED_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_FUSED_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLD_FUSED_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_FUSED_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_FUSED_BOW));

        // Horn Bows
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.HORN_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_FUSED_HORN_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_FUSED_HORN_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLD_FUSED_HORN_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_FUSED_HORN_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_FUSED_HORN_BOW));

        // Longbows
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.LONGBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_FUSED_LONGBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_FUSED_LONGBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLD_FUSED_LONGBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_FUSED_LONGBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_FUSED_LONGBOW));

        // Tubular Bows
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.TUBULAR_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_FUSED_TUBULAR_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_FUSED_TUBULAR_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLD_FUSED_TUBULAR_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_FUSED_TUBULAR_BOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_FUSED_TUBULAR_BOW));

        // Crossbows
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_FUSED_CROSSBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_FUSED_CROSSBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLD_FUSED_CROSSBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_FUSED_CROSSBOW));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_FUSED_CROSSBOW));

        // Quiver and Bundle
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.QUIVER));

        // Bow Fusion Template
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.BOW_FUSION_SMITHING_TEMPLATE));

        // Metal Arrowheads
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.COPPER_ARROWHEAD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.IRON_ARROWHEAD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.GOLDEN_ARROWHEAD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.DIAMOND_ARROWHEAD));

        // Horse, Strider and Hoglin Hairs
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.HORSEHAIR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.STRIDERHAIR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.HOGLINHAIR));

        // Fireproof Lead
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ModItems.FIREPROOF_LEAD));

        // Archer Marker
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(ModBlocks.ARCHER_MARKER));

        // Fletcher
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> entries.add(ModBlocks.FLETCHER));

        ProgressiveArchery.LOGGER.debug("Registering Mod Items to Vanilla Groups for " + ProgressiveArchery.MOD_ID);

    }


    private static void addPotions(ItemGroup.Entries entries, Item item, ItemGroup.StackVisibility visibility) {
        for (Potion potion : Registries.POTION) {
            RegistryEntry<Potion> potionEntry = Registries.POTION.getEntry(potion);
            ItemStack itemStack = PotionContentsComponent.createStack(item, potionEntry);
            entries.add(itemStack,  visibility);
        }
    }
    
}
