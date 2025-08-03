package solipingen.progressivearchery.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.item.ModItems;


@Mixin(HoglinEntity.class)
public abstract class HoglinEntityMixin extends AnimalEntity implements Monster, Hoglin {
    
    
    protected HoglinEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "zombify", at = @At("HEAD"))
    private void injectedZombify(CallbackInfo cbi) {
        if (this.getWorld() instanceof ServerWorld serverWorld && !this.isBaby()) {
            ItemEntity itemEntity = this.dropItem(serverWorld, ModItems.HOGLINHAIR, 1);
            if (itemEntity != null) {
                itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.025f, this.random.nextFloat() * 0.0125f, (this.random.nextFloat() - this.random.nextFloat()) * 0.025f));
            }
            this.emitGameEvent(GameEvent.ENTITY_PLACE);
        }
    }

}
