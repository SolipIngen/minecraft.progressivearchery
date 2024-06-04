package solipingen.progressivearchery.mixin.entity.raid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.arrow.ModArrowEntity;
import solipingen.progressivearchery.util.interfaces.mixin.server.network.ServerPlayerEntityInterface;


@Mixin(RaiderEntity.class)
public abstract class RaiderEntityMixin extends PatrolEntity {


    protected RaiderEntityMixin(EntityType<? extends PatrolEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PatrolEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"))
    private void injectedOnDeath(DamageSource damageSource, CallbackInfo cbi) {
        if (this.getWorld() instanceof ServerWorld) {
            if ((damageSource.getSource() instanceof ModArrowEntity || damageSource.getSource() instanceof FireworkRocketEntity) && damageSource.getAttacker() != null && damageSource.getAttacker() instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)damageSource.getAttacker();
                if (EnchantmentHelper.getEquipmentLevel(Enchantments.MULTISHOT, serverPlayerEntity) >= 3) {
                    ((ServerPlayerEntityInterface)serverPlayerEntity).addMultishotKilledEntity(this);
                    if (((ServerPlayerEntityInterface)serverPlayerEntity).getMultishotKilledEntities().size() >= 7) {
                        Criteria.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity, ((ServerPlayerEntityInterface)serverPlayerEntity).getMultishotKilledEntities());
                    }
                }
            }
        }
    }

    
}
