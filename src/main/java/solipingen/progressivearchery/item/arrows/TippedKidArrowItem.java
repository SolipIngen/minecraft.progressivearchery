package solipingen.progressivearchery.item.arrows;

import java.util.List;
import java.util.Objects;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;


public class TippedKidArrowItem extends KidArrowItem {


    public TippedKidArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.POISON));
        return itemStack;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContentsComponent = (PotionContentsComponent)stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            Objects.requireNonNull(tooltip);
            potionContentsComponent.buildTooltip(tooltip::add, 0.125F, context.getUpdateTickRate());
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return Potion.finishTranslationKey(((PotionContentsComponent)stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)).potion(), this.getTranslationKey() + ".effect.");
    }

}
