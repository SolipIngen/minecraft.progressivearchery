package solipingen.progressivearchery.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import solipingen.progressivearchery.ProgressiveArchery;


public class ModSoundEvents {

    public static final SoundEvent BOW_PULL = ModSoundEvents.registerSoundEvent("bow_pull");
    public static final RegistryEntry<SoundEvent> BOW_PULL_QUICK_CHARGE_1 = ModSoundEvents.registerReference("bow_pull_quick_charge_1");
    public static final RegistryEntry<SoundEvent> BOW_PULL_QUICK_CHARGE_2 = ModSoundEvents.registerReference("bow_pull_quick_charge_2");
    public static final RegistryEntry<SoundEvent> BOW_PULL_QUICK_CHARGE_3 = ModSoundEvents.registerReference("bow_pull_quick_charge_3");

    public static final SoundEvent COPPER_GOLDEN_ARROW_HIT = ModSoundEvents.registerSoundEvent("copper_golden_arrow_hit");
    public static final SoundEvent IRON_ARROW_HIT = ModSoundEvents.registerSoundEvent("iron_arrow_hit");
    public static final SoundEvent DIAMOND_ARROW_HIT = ModSoundEvents.registerSoundEvent("diamond_arrow_hit");

    public static final SoundEvent DRAW_FROM_QUIVER = ModSoundEvents.registerSoundEvent("draw_from_quiver");
    public static final SoundEvent PUT_INTO_QUIVER = ModSoundEvents.registerSoundEvent("put_into_quiver");
    public static final SoundEvent FLETCHING_TABLE_USED = ModSoundEvents.registerSoundEvent("fletching_table_used");
    public static final SoundEvent ARROW_TIPPED = ModSoundEvents.registerSoundEvent("arrow_tipped");

    public static final SoundEvent VILLAGER_SHOOT = ModSoundEvents.registerSoundEvent("villager_shoots_arrow");
    public static final SoundEvent VILLAGER_ATTACK = ModSoundEvents.registerSoundEvent("villager_attack");

    public static final SoundEvent FLETCHER_FLETCH = ModSoundEvents.registerSoundEvent("fletcher_fletch");
    public static final SoundEvent FLETCHER_FAIL = ModSoundEvents.registerSoundEvent("fletcher_fail");
    

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(ProgressiveArchery.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String name) {
        return Registry.registerReference(Registries.SOUND_EVENT, Identifier.of(ProgressiveArchery.MOD_ID, name), SoundEvent.of(Identifier.of(ProgressiveArchery.MOD_ID, name)));
    }

    public static void registerModSoundEvents() {
        ProgressiveArchery.LOGGER.debug("Registering Mod Sounds for " + ProgressiveArchery.MOD_ID);
    }

}
