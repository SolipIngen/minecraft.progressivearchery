package solipingen.progressivearchery.mixin.server.network;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;
import solipingen.progressivearchery.util.interfaces.mixin.server.network.ServerPlayerEntityInterface;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerEntityInterface {
    @Unique @Nullable private List<Entity> multishotKilledEntities;


    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public List<Entity> getMultishotKilledEntities() {
        return this.multishotKilledEntities;
    }

    @Override
    public void addMultishotKilledEntity(Entity multishotKilledEntity) {
        if (this.multishotKilledEntities == null) {
            this.multishotKilledEntities = Lists.newArrayListWithCapacity(7);
        }
        this.multishotKilledEntities.add(multishotKilledEntity);
    }

    @Override
    public void clearMultishotKilledEntities() {
        this.multishotKilledEntities.clear();
    }


    
}
