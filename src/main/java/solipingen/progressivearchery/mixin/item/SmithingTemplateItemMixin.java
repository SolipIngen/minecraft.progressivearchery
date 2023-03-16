package solipingen.progressivearchery.mixin.item;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


@Mixin(SmithingTemplateItem.class)
public abstract class SmithingTemplateItemMixin extends Item {
    @Shadow @Final private static Identifier EMPTY_ARMOR_SLOT_HELMET_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_ARMOR_SLOT_BOOTS_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_SLOT_HOE_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_SLOT_AXE_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_SLOT_SWORD_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_SLOT_SHOVEL_TEXTURE;
    @Shadow @Final private static Identifier EMPTY_SLOT_PICKAXE_TEXTURE;
    private static final Identifier EMPTY_ARMOR_SLOT_SHIELD_TEXTURE = new Identifier("item/empty_armor_slot_shield");
    private static final Identifier EMPTY_SLOT_BOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_bow");
    private static final Identifier EMPTY_SLOT_HORN_BOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_horn_bow");
    private static final Identifier EMPTY_SLOT_LONGBOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_longbow");
    private static final Identifier EMPTY_SLOT_TUBULAR_BOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_tubular_bow");
    private static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = new Identifier(ProgressiveArchery.MOD_ID, "item/empty_slot_crossbow");


    public SmithingTemplateItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getNetheriteUpgradeEmptyBaseSlotTextures", at = @At("HEAD"), cancellable = true)
    private static void injectedGetNetheriteUpgradeEmptyBaseSlotTextures(CallbackInfoReturnable<List<Identifier>> cbireturn) {
        cbireturn.setReturnValue(List.of(EMPTY_ARMOR_SLOT_HELMET_TEXTURE, EMPTY_SLOT_SWORD_TEXTURE, EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE, EMPTY_SLOT_PICKAXE_TEXTURE, EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE, EMPTY_SLOT_AXE_TEXTURE, EMPTY_ARMOR_SLOT_BOOTS_TEXTURE, EMPTY_SLOT_HOE_TEXTURE, EMPTY_ARMOR_SLOT_SHIELD_TEXTURE, EMPTY_SLOT_SHOVEL_TEXTURE, 
            EMPTY_SLOT_BOW_TEXTURE, EMPTY_SLOT_HORN_BOW_TEXTURE, EMPTY_SLOT_LONGBOW_TEXTURE, EMPTY_SLOT_TUBULAR_BOW_TEXTURE, EMPTY_SLOT_CROSSBOW_TEXTURE));
    }
    


}
