package solipingen.progressivearchery.mixin.entity.passive;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

@Mixin(ParrotEntity.class)
public abstract class ParrotEntityMixin extends TameableShoulderEntity implements Flutterer {

    
    protected ParrotEntityMixin(EntityType<? extends TameableShoulderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void injectedDropFeather(CallbackInfo info) {
        float featherrandomf = this.world.getRandom().nextFloat();
        if (!this.world.isClient && this.isAlive() && featherrandomf < 0.0001f) {
            ItemEntity itemEntity = this.dropItem(Items.FEATHER);
            itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.025f, this.random.nextFloat() * 0.0125f, (this.random.nextFloat() - this.random.nextFloat()) * 0.025f));
            this.emitGameEvent(GameEvent.ENTITY_PLACE);
        }
    }

}
