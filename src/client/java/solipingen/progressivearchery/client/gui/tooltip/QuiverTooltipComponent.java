package solipingen.progressivearchery.client.gui.tooltip;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.component.type.QuiverContentsComponent;

import java.util.List;
import java.util.Objects;


@Environment(EnvType.CLIENT)
public class QuiverTooltipComponent implements TooltipComponent {
    private static final Identifier QUIVER_PROGRESS_BAR_BORDER_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "container/quiver/quiver_progressbar_border");
    private static final Identifier QUIVER_PROGRESS_BAR_FILL_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID,"container/quiver/quiver_progressbar_fill");
    private static final Identifier QUIVER_PROGRESS_BAR_FULL_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID,"container/quiver/quiver_progressbar_full");
    private static final Identifier QUIVER_SLOT_HIGHLIGHT_BACK_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID,"container/quiver/slot_highlight_back");
    private static final Identifier QUIVER_SLOT_HIGHLIGHT_FRONT_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID,"container/quiver/slot_highlight_front");
    private static final Identifier QUIVER_SLOT_BACKGROUND_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID,"container/quiver/slot_background");
    private static final int SLOTS_PER_ROW = 4;
    private static final int SLOT_DIMENSION = 24;
    private static final int ROW_WIDTH = 96;
    private static final int PROGRESS_BAR_WIDTH = 94;
    private static final Text QUIVER_FULL = Text.translatable("item.minecraft.bundle.full");
    private static final Text QUIVER_EMPTY = Text.translatable("item.minecraft.bundle.empty");
    private static final Text QUIVER_EMPTY_DESCRIPTION = Text.translatable("item.minecraft.bundle.empty.description");
    private final QuiverContentsComponent quiverContents;
    private final int selectedItemIndex;


    public QuiverTooltipComponent(QuiverContentsComponent quiverContents, int selectedItemIndex) {
        this.quiverContents = quiverContents;
        this.selectedItemIndex = selectedItemIndex;
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return this.quiverContents.isEmpty() ? QuiverTooltipComponent.getHeightOfEmpty(textRenderer) : this.getHeightOfNonEmpty();
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 96;
    }

    @Override
    public boolean isSticky() {
        return true;
    }

    private static int getHeightOfEmpty(TextRenderer textRenderer) {
        return QuiverTooltipComponent.getDescriptionHeight(textRenderer) + 13 + 8;
    }

    private int getHeightOfNonEmpty() {
        return this.getRowsHeight() + 13 + 8;
    }

    private int getRowsHeight() {
        return this.getRows() * 24;
    }

    private int getXMargin(int width) {
        return (width - 96) / 2;
    }

    private int getRows() {
        return MathHelper.ceilDiv(this.getNumVisibleSlots(), 4);
    }

    private int getNumVisibleSlots() {
        return Math.min(12, this.quiverContents.size());
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        if (this.quiverContents.isEmpty()) {
            this.drawEmptyTooltip(textRenderer, x, y, width, height, context);
        }
        else {
            this.drawNonEmptyTooltip(textRenderer, x, y, width, height, context);
        }
    }

    private void drawEmptyTooltip(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        QuiverTooltipComponent.drawEmptyDescription(x + this.getXMargin(width), y, textRenderer, context);
        this.drawProgressBar(x + this.getXMargin(width), y + QuiverTooltipComponent.getDescriptionHeight(textRenderer) + 4, textRenderer, context);
    }

    private void drawNonEmptyTooltip(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        boolean bl = this.quiverContents.size() > 12;
        List<ItemStack> list = this.firstStacksInContents(this.quiverContents.getNumberOfStacksShown());
        int i = x + this.getXMargin(width) + 96;
        int j = y + this.getRows() * 24;
        int k = 1;
        for(int l = 1; l <= this.getRows(); ++l) {
            for(int m = 1; m <= 4; ++m) {
                int n = i - m * 24;
                int o = j - l * 24;
                if (QuiverTooltipComponent.shouldDrawExtraItemsCount(bl, m, l)) {
                    QuiverTooltipComponent.drawExtraItemsCount(n, o, this.numContentItemsAfter(list), textRenderer, context);
                }
                else if (QuiverTooltipComponent.shouldDrawItem(list, k)) {
                    this.drawItem(k, n, o, list, k, textRenderer, context);
                    ++k;
                }
            }
        }

        this.drawSelectedItemTooltip(textRenderer, context, x, y, width);
        this.drawProgressBar(x + this.getXMargin(width), y + this.getRowsHeight() + 4, textRenderer, context);
    }

    private List<ItemStack> firstStacksInContents(int numberOfStacksShown) {
        int i = Math.min(this.quiverContents.size(), numberOfStacksShown);
        return this.quiverContents.stream().toList().subList(0, i);
    }

    private static boolean shouldDrawExtraItemsCount(boolean hasMoreItems, int column, int row) {
        return hasMoreItems && column * row == 1;
    }

    private static boolean shouldDrawItem(List<ItemStack> items, int itemIndex) {
        return items.size() >= itemIndex;
    }

    private int numContentItemsAfter(List<ItemStack> items) {
        return this.quiverContents.stream().skip(items.size()).mapToInt(ItemStack::getCount).sum();
    }

    private void drawItem(int index, int x, int y, List<ItemStack> stacks, int seed, TextRenderer textRenderer, DrawContext drawContext) {
        int i = stacks.size() - index;
        boolean bl = i == this.selectedItemIndex;
        ItemStack itemStack = stacks.get(i);
        if (bl) {
            drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, QUIVER_SLOT_HIGHLIGHT_BACK_TEXTURE, x, y, 24, 24);
        }
        else {
            drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, QUIVER_SLOT_BACKGROUND_TEXTURE, x, y, 24, 24);
        }
        drawContext.drawItem(itemStack, x + 4, y + 4, seed);
        drawContext.drawStackOverlay(textRenderer, itemStack, x + 4, y + 4);
        if (bl) {
            drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, QUIVER_SLOT_HIGHLIGHT_FRONT_TEXTURE, x, y, 24, 24);
        }

    }

    private static void drawExtraItemsCount(int x, int y, int numExtra, TextRenderer textRenderer, DrawContext drawContext) {
        drawContext.drawCenteredTextWithShadow(textRenderer, "+" + numExtra, x + 12, y + 10, -1);
    }

    private void drawSelectedItemTooltip(TextRenderer textRenderer, DrawContext drawContext, int x, int y, int width) {
        if (this.selectedItemIndex >= 0 && this.selectedItemIndex < this.quiverContents.size()) {
            ItemStack itemStack = this.quiverContents.get(this.selectedItemIndex);
            Text text = itemStack.getFormattedName();
            int i = textRenderer.getWidth(text.asOrderedText());
            int j = x + width / 2 - 12;
            TooltipComponent tooltipComponent = TooltipComponent.of(text.asOrderedText());
            drawContext.drawTooltipImmediately(textRenderer, List.of(tooltipComponent), j - i / 2, y - 15, HoveredTooltipPositioner.INSTANCE, itemStack.get(DataComponentTypes.TOOLTIP_STYLE));
        }

    }

    private void drawProgressBar(int x, int y, TextRenderer textRenderer, DrawContext drawContext) {
        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.getProgressBarFillTexture(), x + 1, y, this.getProgressBarFill(), 13);
        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, QUIVER_PROGRESS_BAR_BORDER_TEXTURE, x, y, 96, 13);
        Text text = this.getProgressBarLabel();
        if (text != null) {
            drawContext.drawCenteredTextWithShadow(textRenderer, text, x + 48, y + 3, -1);
        }
    }

    private static void drawEmptyDescription(int x, int y, TextRenderer textRenderer, DrawContext drawContext) {
        drawContext.drawWrappedTextWithShadow(textRenderer, QUIVER_EMPTY_DESCRIPTION, x, y, 96, -5592406);
    }

    private static int getDescriptionHeight(TextRenderer textRenderer) {
        int var10000 = textRenderer.wrapLines(QUIVER_EMPTY_DESCRIPTION, 96).size();
        Objects.requireNonNull(textRenderer);
        return var10000 * 9;
    }

    private int getProgressBarFill() {
        return MathHelper.clamp(MathHelper.multiplyFraction(this.quiverContents.getOccupancy(), 94), 0, 94);
    }

    private Identifier getProgressBarFillTexture() {
        return this.quiverContents.getOccupancy().compareTo(Fraction.ONE) >= 0 ? QUIVER_PROGRESS_BAR_FULL_TEXTURE : QUIVER_PROGRESS_BAR_FILL_TEXTURE;
    }

    @Nullable
    private Text getProgressBarLabel() {
        if (this.quiverContents.isEmpty()) {
            return QUIVER_EMPTY;
        }
        else {
            return this.quiverContents.getOccupancy().compareTo(Fraction.ONE) >= 0 ? QUIVER_FULL : null;
        }
    }



}
