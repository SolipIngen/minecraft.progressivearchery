package solipingen.progressivearchery.mixin.entity.passive;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.progressivearchery.registry.tag.ModEntityTypeTags;


@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {


    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Inject(method = "mobTick", at = @At("TAIL"))
    private void injectedDropFeather(CallbackInfo info) {
        if (this.getType().isIn(ModEntityTypeTags.BIRDS)) {
            float featherThreshold = ((AnimalEntity)(Object)this) instanceof ChickenEntity ? 0.0001f : 0.00005f;
            World world = this.getWorld();
            if (!world.isClient && this.isAlive() && !this.isBaby() && this.getRandom().nextFloat() < featherThreshold) {
                ItemEntity itemEntity = this.dropStack(new ItemStack(Items.FEATHER, 1 + (int)Math.round(Math.abs(this.getRandom().nextTriangular(0.0, 1.75)))));
                if (itemEntity != null) {
                    itemEntity.addVelocity((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.025f, this.getRandom().nextFloat() * 0.0125f, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.025f);
                }
                this.emitGameEvent(GameEvent.ENTITY_PLACE);
            }
        }
    }
    


}
