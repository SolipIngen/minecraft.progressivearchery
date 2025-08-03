package solipingen.progressivearchery.client.gui.screen.ingame;

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



@Environment(value = EnvType.CLIENT)
public abstract class ArrowmakingScreen<T extends ArrowmakingScreenHandler> extends HandledScreen<T> implements ScreenHandlerListener {


    public ArrowmakingScreen(T handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title);
    }

    protected void setup() {
    }

    @Override
    protected void init() {
        super.init();
        this.setup();
        this.handler.addListener(this);
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
        this.handler.removeListener(this);
    }

    protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
    }

}


