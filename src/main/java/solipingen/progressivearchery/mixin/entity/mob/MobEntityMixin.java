package solipingen.progressivearchery.mixin.entity.mob;

import net.minecraft.entity.Leashable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModCrossbowItem;


@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements Leashable {
    @Shadow @Nullable private Leashable.@Nullable LeashData leashData;


    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "prefersNewEquipment", at = @At("TAIL"), cancellable = true)
    private void injectedPrefersNewEquipment(ItemStack newStack, ItemStack oldStack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (newStack.getItem() instanceof ModBowItem && oldStack.getItem() instanceof ModBowItem) {
            cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
        }
        if (newStack.getItem() instanceof ModCrossbowItem && oldStack.getItem() instanceof ModCrossbowItem) {
            cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
        }
    }

    
}
