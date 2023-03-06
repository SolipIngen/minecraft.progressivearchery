package solipingen.progressivearchery.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;

public class ModSoundEvents {

    public static final SoundEvent BOW_PULL = registerSoundEvent("bow_pull");
    public static final SoundEvent BOW_PULL_QUICK_CHARGE_1 = registerSoundEvent("bow_pull_quick_charge_1");
    public static final SoundEvent BOW_PULL_QUICK_CHARGE_2 = registerSoundEvent("bow_pull_quick_charge_2");
    public static final SoundEvent BOW_PULL_QUICK_CHARGE_3 = registerSoundEvent("bow_pull_quick_charge_3");

    public static final SoundEvent COPPER_GOLDEN_ARROW_HIT = registerSoundEvent("copper_golden_arrow_hit");
    public static final SoundEvent IRON_ARROW_HIT = registerSoundEvent("iron_arrow_hit");
    public static final SoundEvent DIAMOND_ARROW_HIT = registerSoundEvent("diamond_arrow_hit");

    public static final SoundEvent DRAW_FROM_QUIVER = registerSoundEvent("draw_from_quiver");
    public static final SoundEvent PUT_INTO_QUIVER = registerSoundEvent("put_into_quiver");
    public static final SoundEvent FLETCHING_TABLE_USED = registerSoundEvent("fletching_table_used");
    public static final SoundEvent ARROW_TIPPED = registerSoundEvent("arrow_tipped");

    public static final SoundEvent VILLAGER_SHOOT = registerSoundEvent("villager_shoots_arrow");
    

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(ProgressiveArchery.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSoundEvents() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Sounds for " + ProgressiveArchery.MOD_ID);
    }

}
