package solipingen.progressivearchery.item;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import solipingen.progressivearchery.ProgressiveArchery;


public class BowFusionTemplateItem extends SmithingTemplateItem {
    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    private static final Text BOW_UPGRADE_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.applies_to"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text BOW_UPGRADE_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.ingredients"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text BOW_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.base_slot_description")));
    private static final Text BOW_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.additions_slot_description")));
    private static final Identifier EMPTY_SLOT_BOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "container/slot/bow");
    private static final Identifier EMPTY_SLOT_HORN_BOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "container/slot/horn_bow");
    private static final Identifier EMPTY_SLOT_LONGBOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "container/slot/longbow");
    private static final Identifier EMPTY_SLOT_TUBULAR_BOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "container/slot/tubular_bow");
    private static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "container/slot/crossbow");
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = Identifier.ofVanilla("container/slot/ingot");
    private static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = Identifier.ofVanilla("container/slot/diamond");
    private final Text baseSlotDescriptionText;
    private final Text additionsSlotDescriptionText;
    private final List<Identifier> emptyBaseSlotTextures;
    private final List<Identifier> emptyAdditionsSlotTextures;

    
    public BowFusionTemplateItem(Text appliesToText, Text ingredientsText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures, Item.Settings settings) {
        super(appliesToText, ingredientsText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures, settings);
        this.baseSlotDescriptionText = baseSlotDescriptionText;
        this.additionsSlotDescriptionText = additionsSlotDescriptionText;
        this.emptyBaseSlotTextures = emptyBaseSlotTextures;
        this.emptyAdditionsSlotTextures = emptyAdditionsSlotTextures;
    }

    public static BowFusionTemplateItem createBowFusionTemplate(Item.Settings settings) {
        return new BowFusionTemplateItem(BOW_UPGRADE_APPLIES_TO_TEXT, BOW_UPGRADE_INGREDIENTS_TEXT, BOW_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, BOW_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
            BowFusionTemplateItem.getFusionEmptyBaseSlotTextures(), BowFusionTemplateItem.getFusionEmptyAdditionsSlotTextures(), settings);
    }

    private static List<Identifier> getFusionEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_BOW_TEXTURE, EMPTY_SLOT_HORN_BOW_TEXTURE, EMPTY_SLOT_LONGBOW_TEXTURE, EMPTY_SLOT_TUBULAR_BOW_TEXTURE, EMPTY_SLOT_CROSSBOW_TEXTURE);
    }

    private static List<Identifier> getFusionEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT_TEXTURE, EMPTY_SLOT_DIAMOND_TEXTURE);
    }

    @Override
    public Text getBaseSlotDescription() {
        return this.baseSlotDescriptionText;
    }

    @Override
    public Text getAdditionsSlotDescription() {
        return this.additionsSlotDescriptionText;
    }

    @Override
    public List<Identifier> getEmptyBaseSlotTextures() {
        return this.emptyBaseSlotTextures;
    }

    @Override
    public List<Identifier> getEmptyAdditionsSlotTextures() {
        return this.emptyAdditionsSlotTextures;
    }

//    @Override
//    public String getTranslationKey() {
//        return TRANSLATION_KEY;
//    }
}


