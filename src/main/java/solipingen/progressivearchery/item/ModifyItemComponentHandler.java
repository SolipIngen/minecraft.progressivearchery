package solipingen.progressivearchery.item;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DamageResistantComponent;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Unit;


public class ModifyItemComponentHandler implements DefaultItemComponentEvents.ModifyCallback {


    @Override
    public void modify(DefaultItemComponentEvents.ModifyContext context) {
        context.modify(Items.BOW, builder -> {
                    builder.add(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(15));
                    builder.add(DataComponentTypes.MAX_DAMAGE, 184);
                });
        context.modify(Items.CROSSBOW, builder -> {
            builder.add(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(15));
            builder.add(DataComponentTypes.MAX_DAMAGE, 212);
        });
        context.modify(Items.ENDER_PEARL, builder -> builder.add(DataComponentTypes.USE_COOLDOWN,
                new UseCooldownComponent(0.5f)));
        context.modify(Items.MAGMA_CREAM, builder -> builder.add(DataComponentTypes.DAMAGE_RESISTANT,
                new DamageResistantComponent(DamageTypeTags.IS_FIRE)));
    }


}
