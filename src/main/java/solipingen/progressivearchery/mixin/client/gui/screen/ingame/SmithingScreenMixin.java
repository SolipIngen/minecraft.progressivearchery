package solipingen.progressivearchery.mixin.client.gui.screen.ingame;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


@Mixin(SmithingScreen.class)
@Environment(value=EnvType.CLIENT)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {
    @Shadow @Final private static Identifier EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE_TEXTURE;
    private static final Identifier EMPTY_SLOT_SMITHING_TEMPLATE_BOW_FUSION_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_smithing_template_bow_fusion");
    private static final List<Identifier> MOD_EMPTY_SLOT_TEXTURES = List.of(EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM_TEXTURE, EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE_TEXTURE, EMPTY_SLOT_SMITHING_TEMPLATE_BOW_FUSION_TEXTURE);


    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }
    
    @Redirect(method = "handledScreenTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;EMPTY_SLOT_TEXTURES:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private List<Identifier> redirectedEmptySlotTextures() {
        return MOD_EMPTY_SLOT_TEXTURES;
    }

    
}
