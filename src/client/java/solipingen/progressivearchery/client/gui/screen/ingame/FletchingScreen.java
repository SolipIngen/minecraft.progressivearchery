package solipingen.progressivearchery.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.registry.tag.ModItemTags;
import solipingen.progressivearchery.screen.fletching.FletchingScreenHandler;


@Environment(value = EnvType.CLIENT)
public class FletchingScreen extends ArrowmakingScreen<FletchingScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID,"textures/gui/container/fletching_gui.png");


    public FletchingScreen(FletchingScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, TEXTURE);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        Slot headSlot = this.handler.getSlot(0);
        Slot bodySlot = this.handler.getSlot(1);
        Slot additionSlot = this.handler.getSlot(3);
        if (bodySlot.getStack().getItem() instanceof ModArrowItem) {
            if (bodySlot.getStack().isIn(ModItemTags.CUTTABLE_METAL_ARROWS)) {
                context.drawTexture(TEXTURE, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 48, 16, 16);
            }
            else {
                context.drawTexture(TEXTURE, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 0, 16, 16);
            }
        }
        else {
            if (headSlot.getStack().isOf(Items.STICK)) {
                context.drawTexture(TEXTURE, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 0, 16, 16);
            }
            else if (headSlot.getStack().isOf(Items.FLINT)) {
                context.drawTexture(TEXTURE, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 16, 16, 16);
            }
            else if (headSlot.getStack().isIn(ModItemTags.CRAFTED_ARROWHEADS)) {
                context.drawTexture(TEXTURE, i + additionSlot.x, j + additionSlot.y, this.backgroundWidth + 1, 32, 16, 16);
            }
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.drawForeground(context, mouseX, mouseY);
    }

}

