package solipingen.progressivearchery.integration.rei;

import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import solipingen.progressivearchery.item.ModItems;


public class ModREIPlugin implements BuiltinPlugin, REIServerPlugin {
    

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.registerNbt(ModItems.TIPPED_KID_ARROW);
    }

}
