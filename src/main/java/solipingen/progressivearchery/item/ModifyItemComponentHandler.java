package solipingen.progressivearchery.item;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Unit;


public class ModifyItemComponentHandler implements DefaultItemComponentEvents.ModifyCallback {


    @Override
    public void modify(DefaultItemComponentEvents.ModifyContext context) {
        context.modify(Items.MAGMA_CREAM, builder -> builder.add(DataComponentTypes.FIRE_RESISTANT, Unit.INSTANCE));
        context.modify(Items.BOW, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, 184));
        context.modify(Items.CROSSBOW, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, 212));
    }


}
