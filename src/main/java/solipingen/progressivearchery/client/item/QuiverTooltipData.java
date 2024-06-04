package solipingen.progressivearchery.client.item;

import net.minecraft.client.item.TooltipData;
import solipingen.progressivearchery.component.QuiverContentsComponent;


public record QuiverTooltipData(QuiverContentsComponent contents) implements TooltipData {

    public QuiverTooltipData(QuiverContentsComponent contents) {
        this.contents = contents;
    }

    public QuiverContentsComponent contents() {
        return this.contents;
    }

}
