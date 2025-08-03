package solipingen.progressivearchery.mixin.entity.accessors;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(MobEntity.class)
public interface MobEntityInvoker {

    @Invoker("getExperienceToDrop")
    public int invokeGetExperienceToDrop(ServerWorld world);

}
