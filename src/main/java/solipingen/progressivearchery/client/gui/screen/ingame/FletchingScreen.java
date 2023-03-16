package solipingen.progressivearchery.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.screen.fletching.FletchingScreenHandler;


@Environment(value=EnvType.CLIENT)
public class FletchingScreen extends ArrowmakingScreen<FletchingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ProgressiveArchery.MOD_ID,"textures/gui/container/fletching_gui.png");


    public FletchingScreen(FletchingScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, TEXTURE);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        FletchingScreen.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        Slot headSlot = this.handler.getSlot(0);
        Slot bodySlot = this.handler.getSlot(1);
        Slot additionSlot = this.handler.getSlot(3);
        if (bodySlot.getStack().getItem() instanceof ModArrowItem) {
            if (bodySlot.getStack().isOf(ModItems.WOODEN_ARROW) || bodySlot.getStack().isOf(ModItems.FLINT_ARROW)) {
                FletchingScreen.drawTexture(matrices, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 0, 16, 16);
            }
            else {
                FletchingScreen.drawTexture(matrices, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 48, 16, 16);
            }
        }
        else {
            if (headSlot.getStack().isOf(Items.STICK)) {
                FletchingScreen.drawTexture(matrices, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 0, 16, 16);
            }
            else if (headSlot.getStack().isOf(Items.FLINT)) {
                FletchingScreen.drawTexture(matrices, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 16, 16, 16);
            }
            else if (headSlot.getStack().isOf(ModItems.COPPER_NUGGET) || headSlot.getStack().isOf(Items.GOLD_NUGGET) || headSlot.getStack().isOf(Items.IRON_NUGGET) || headSlot.getStack().isOf(Items.DIAMOND)) {
                FletchingScreen.drawTexture(matrices, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 32, 16, 16);
            }
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.drawForeground(matrices, mouseX, mouseY);
    }

}

