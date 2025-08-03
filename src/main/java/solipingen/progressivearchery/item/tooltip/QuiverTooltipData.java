package solipingen.progressivearchery.item.tooltip;

import net.minecraft.item.tooltip.TooltipData;
import solipingen.progressivearchery.component.type.QuiverContentsComponent;


public record QuiverTooltipData(QuiverContentsComponent contents, int selectedItemIndex) implements TooltipData {


    public QuiverContentsComponent contents() {
        return this.contents;
    }

    public int selectedItemIndex() {
        return this.selectedItemIndex;
    }

}
