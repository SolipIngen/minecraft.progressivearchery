package solipingen.progressivearchery.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.screen.fletching.FletcherScreenHandler;


@Environment(EnvType.CLIENT)
public class FletcherScreen extends HandledScreen<FletcherScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID,"textures/gui/container/fletcher_gui.png");
    private static final Identifier POWERED_REDSTONE_TEXTURE = Identifier.of("container/crafter/powered_redstone");
    private static final Identifier UNPOWERED_REDSTONE_TEXTURE = Identifier.of("container/crafter/unpowered_redstone");


    public FletcherScreen(FletcherScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = 91;
        this.titleY = 10;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawArrowTexture(context);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void drawArrowTexture(DrawContext context) {
        int i = this.width / 2 + 9;
        int j = this.height / 2 - 46;
        Identifier identifier;
        if (this.handler.isTriggered()) {
            identifier = POWERED_REDSTONE_TEXTURE;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, (this.width - this.backgroundWidth) / 2 + 18, (this.height - this.backgroundHeight) / 2 + 8, 177, 0, 66, 11, 256, 256);
        }
        else {
            identifier = UNPOWERED_REDSTONE_TEXTURE;
        }
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier, i, j, 16, 16);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

//    @Override
//    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
//        RenderSystem.disableBlend();
//        super.drawForeground(context, mouseX, mouseY);
//    }


}
