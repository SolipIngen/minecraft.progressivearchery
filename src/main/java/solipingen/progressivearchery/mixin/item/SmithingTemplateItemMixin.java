package solipingen.progressivearchery.mixin.item;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import solipingen.progressivearchery.ProgressiveArchery;


@Mixin(SmithingTemplateItem.class)
public abstract class SmithingTemplateItemMixin extends Item {
    @Shadow @Final private List<Identifier> emptyBaseSlotTextures;
    private static final Identifier EMPTY_SLOT_BOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "item/empty_slot_bow");
    private static final Identifier EMPTY_SLOT_HORN_BOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "item/empty_slot_horn_bow");
    private static final Identifier EMPTY_SLOT_LONGBOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "item/empty_slot_longbow");
    private static final Identifier EMPTY_SLOT_TUBULAR_BOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "item/empty_slot_tubular_bow");
    private static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = Identifier.of(ProgressiveArchery.MOD_ID, "item/empty_slot_crossbow");


    public SmithingTemplateItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static List<Identifier> modifiedEmptyBaseSlotTextures(List<Identifier> originalList) {
        ArrayList<Identifier> textures = new ArrayList<Identifier>(originalList);
        textures.add(EMPTY_SLOT_BOW_TEXTURE);
        textures.add(EMPTY_SLOT_HORN_BOW_TEXTURE);
        textures.add(EMPTY_SLOT_LONGBOW_TEXTURE);
        textures.add(EMPTY_SLOT_TUBULAR_BOW_TEXTURE);
        textures.add(EMPTY_SLOT_CROSSBOW_TEXTURE);
        return List.copyOf(textures);
    }
    


}
