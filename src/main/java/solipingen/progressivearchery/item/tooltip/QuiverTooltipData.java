package solipingen.progressivearchery.item.tooltip;

import net.minecraft.item.tooltip.TooltipData;
import solipingen.progressivearchery.component.QuiverContentsComponent;


public record QuiverTooltipData(QuiverContentsComponent contents) implements TooltipData {

    public QuiverTooltipData(QuiverContentsComponent contents) {
        this.contents = contents;
    }

    public QuiverContentsComponent contents() {
        return this.contents;
    }

}
