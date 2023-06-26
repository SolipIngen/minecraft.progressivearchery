package solipingen.progressivearchery.mixin.entity.player;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.util.interfaces.mixin.entity.player.PlayerEntityInterface;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityInterface {
    @Shadow @Final private PlayerInventory inventory;


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean hasQuiver() {
        for (int i = 0; i < this.inventory.size(); ++i) {
            ItemStack itemStack = this.inventory.getStack(i);
            if (itemStack.isOf(ModItems.QUIVER)) {
                return true;
            }
            else {
                continue;
            }
        }
        return false;
    }

    @Override
    public boolean hasFilledQuiver() {
        for (int i = 0; i < this.inventory.size(); ++i) {
            ItemStack itemStack = this.inventory.getStack(i);
            if (itemStack.isOf(ModItems.QUIVER)) {
                if (QuiverItem.getQuiverOccupancy(itemStack) > 0) {
                    return true;
                }
                else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return false;
    }


}
