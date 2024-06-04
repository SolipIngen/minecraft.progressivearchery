package solipingen.progressivearchery.item;

import java.util.List;

import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import solipingen.progressivearchery.ProgressiveArchery;


public class BowFusionTemplateItem extends SmithingTemplateItem {
    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    private static final String TRANSLATION_KEY = Util.createTranslationKey("item", new Identifier("smithing_template"));
    private static final Text BOW_UPGRADE_TEXT = Text.translatable(Util.createTranslationKey("upgrade", new Identifier(ProgressiveArchery.MOD_ID, "bow_fusion"))).formatted(TITLE_FORMATTING);
    private static final Text BOW_UPGRADE_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.applies_to"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text BOW_UPGRADE_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.ingredients"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text BOW_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.base_slot_description")));
    private static final Text BOW_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(ProgressiveArchery.MOD_ID,"smithing_template.bow_fusion.additions_slot_description")));
    private static final Identifier EMPTY_SLOT_BOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_bow");
    private static final Identifier EMPTY_SLOT_HORN_BOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_horn_bow");
    private static final Identifier EMPTY_SLOT_LONGBOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_longbow");
    private static final Identifier EMPTY_SLOT_TUBULAR_BOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_tubular_bow");
    private static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_crossbow");
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");
    private static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = new Identifier("item/empty_slot_diamond");
    private final Text baseSlotDescriptionText;
    private final Text additionsSlotDescriptionText;
    private final List<Identifier> emptyBaseSlotTextures;
    private final List<Identifier> emptyAdditionsSlotTextures;

    
    public BowFusionTemplateItem(Text appliesToText, Text ingredientsText, Text titleText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures) {
        super(appliesToText, ingredientsText, titleText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures);
        this.baseSlotDescriptionText = baseSlotDescriptionText;
        this.additionsSlotDescriptionText = additionsSlotDescriptionText;
        this.emptyBaseSlotTextures = emptyBaseSlotTextures;
        this.emptyAdditionsSlotTextures = emptyAdditionsSlotTextures;
    }

    public static BowFusionTemplateItem createBowFusionTemplate() {
        return new BowFusionTemplateItem(BOW_UPGRADE_APPLIES_TO_TEXT, BOW_UPGRADE_INGREDIENTS_TEXT, BOW_UPGRADE_TEXT, BOW_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, BOW_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT, 
            BowFusionTemplateItem.getFusionEmptyBaseSlotTextures(), BowFusionTemplateItem.getFusionEmptyAdditionsSlotTextures());
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

    @Override
    public String getTranslationKey() {
        return TRANSLATION_KEY;
    }
}


