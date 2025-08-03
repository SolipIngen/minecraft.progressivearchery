package solipingen.progressivearchery.item;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import solipingen.progressivearchery.registry.tag.ModItemTags;


public record BowMaterial(int level, int durability, int enchantmentValue, TagKey<Item> repairItems) {
    public static final BowMaterial WOOD = new BowMaterial(0, 184, 15, ItemTags.WOODEN_TOOL_MATERIALS);
    public static final BowMaterial COPPER = new BowMaterial(1, 350, 14, ModItemTags.REPAIRS_COPPER_FUSED_BOWS);
    public static final BowMaterial IRON = new BowMaterial(2, 600, 14, ItemTags.IRON_TOOL_MATERIALS);
    public static final BowMaterial DIAMOND = new BowMaterial(3, 2442, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
    public static final BowMaterial GOLD = new BowMaterial(1, 246, 22, ItemTags.GOLD_TOOL_MATERIALS);
    public static final BowMaterial NETHERITE = new BowMaterial(4, 3147, 15, ItemTags.NETHERITE_TOOL_MATERIALS);


    public int level() {
        return this.level;
    }

    public int durability() {
        return this.durability;
    }

    public int enchantanmentValue() {
        return this.enchantmentValue;
    }

    public TagKey<Item> repairItems() {
        return this.repairItems;
    }

}
