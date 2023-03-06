package solipingen.progressivearchery.mixin.entity.passive;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends AnimalEntity {

    
    protected ChickenEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void injectedDropFeather(CallbackInfo info) {
        float featherrandomf = this.world.getRandom().nextFloat();
        if (!this.world.isClient && this.isAlive() && !this.isBaby() && featherrandomf < 0.00005f) {
            ItemEntity itemEntity = this.dropItem(Items.FEATHER);
            itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.025f, this.random.nextFloat() * 0.0125f, (this.random.nextFloat() - this.random.nextFloat()) * 0.025f));
            this.emitGameEvent(GameEvent.ENTITY_PLACE);
        }
    }

}
