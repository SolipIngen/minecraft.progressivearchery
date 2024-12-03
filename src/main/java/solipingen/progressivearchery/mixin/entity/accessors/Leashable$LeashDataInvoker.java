package solipingen.progressivearchery.mixin.entity.accessors;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.Leashable;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;


@Mixin(Leashable.LeashData.class)
public interface Leashable$LeashDataInvoker {

    @Invoker("<init>")
    public static Leashable.LeashData invokeLeashDataInit(Either<UUID, BlockPos> unresolvedLeashData) {
        throw new AssertionError();
    }

}
