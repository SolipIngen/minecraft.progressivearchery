package solipingen.progressivearchery.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.screen.fletching.ArrowmakingScreenHandler;


@SuppressWarnings("unused")
@Environment(value = EnvType.CLIENT)
public abstract class ArrowmakingScreen<T extends ArrowmakingScreenHandler> extends HandledScreen<T> implements ScreenHandlerListener {
    private final Identifier texture;
    private boolean narrow;


    public ArrowmakingScreen(T handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title);
        this.texture = texture;
    }

    protected void setup() {
    }

    @Override
    protected void init() {
        super.init();
        this.setup();
        ((ArrowmakingScreenHandler)this.handler).addListener(this);
        this.narrow = this.width < 379;
        this.titleX = 91;
        this.titleY = 10;
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
    }

    @Override
    public void removed() {
        super.removed();
        ((ArrowmakingScreenHandler)this.handler).removeListener(this);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
        if (this.narrow) {
            this.drawBackground(context, delta, mouseX, mouseY);
        }
        else {
            super.render(context, mouseX, mouseY, delta);
        }
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    protected boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        return (!this.narrow) && super.isPointWithinBounds(x, y, width, height, pointX, pointY);
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
    }

}


