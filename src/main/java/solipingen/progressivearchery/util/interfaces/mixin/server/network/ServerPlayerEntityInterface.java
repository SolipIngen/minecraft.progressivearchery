package solipingen.progressivearchery.util.interfaces.mixin.server.network;

import java.util.List;

import net.minecraft.entity.Entity;


public interface ServerPlayerEntityInterface {
    
    public List<Entity> getMultishotKilledEntities();
    public void addMultishotKilledEntity(Entity multishotKilledEntity);
    public void clearMultishotKilledEntities();

}
