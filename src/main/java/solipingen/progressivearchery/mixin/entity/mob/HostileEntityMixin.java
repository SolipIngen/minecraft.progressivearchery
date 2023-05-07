package solipingen.progressivearchery.mixin.entity.mob;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModItems;


@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends PathAwareEntity implements Monster {
    
    
    protected HostileEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getProjectileType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"), cancellable = true)
    private void injectedGetArrowType(ItemStack stack, CallbackInfoReturnable<ItemStack> cbireturn) {
        Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
        Random random = this.getRandom();
        float arrowrandomf = (1.0f - 0.05f*this.world.getDifficulty().getId())*random.nextFloat() + 0.05f*this.world.getDifficulty().getId()*this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
        if (stack.getItem() instanceof ModBowItem && ((ModBowItem)stack.getItem()).getBowType() == 3) {
            if (itemStack.isEmpty()) {
                if (0.15f <= arrowrandomf && arrowrandomf < 0.5f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.FLINT_KID_ARROW));
                }
                else if (0.5f <= arrowrandomf && arrowrandomf < 0.85f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.COPPER_KID_ARROW));
                }
                else if (0.85f <= arrowrandomf && arrowrandomf < 0.95f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.GOLDEN_KID_ARROW));
                }
                else if (0.95f <= arrowrandomf && arrowrandomf <= 1.0f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.IRON_KID_ARROW));
                }
                else {
                    cbireturn.setReturnValue(new ItemStack(ModItems.WOODEN_KID_ARROW));
                }
            }
            else {
                cbireturn.setReturnValue(itemStack);
            }
        }
        else {
            if (itemStack.isEmpty()) {
                if (((HostileEntity)(Object)this) instanceof PiglinEntity) {
                    if (arrowrandomf < 0.15f) {
                        cbireturn.setReturnValue(new ItemStack(ModItems.WOODEN_ARROW));
                    }
                    else if (0.15f <= arrowrandomf && arrowrandomf < 0.5f) {
                        cbireturn.setReturnValue(new ItemStack(ModItems.FLINT_ARROW));
                    }
                    else {
                        cbireturn.setReturnValue(new ItemStack(ModItems.GOLDEN_ARROW));
                    }
                }
                else {
                    if (0.15f <= arrowrandomf && arrowrandomf < 0.5f) {
                        cbireturn.setReturnValue(new ItemStack(ModItems.FLINT_ARROW));
                    }
                    else if (0.5f <= arrowrandomf && arrowrandomf < 0.85f) {
                        cbireturn.setReturnValue(new ItemStack(ModItems.COPPER_ARROW));
                    }
                    else if (0.85f <= arrowrandomf && arrowrandomf < 0.95f) {
                        cbireturn.setReturnValue(new ItemStack(ModItems.GOLDEN_ARROW));
                    }
                    else if (0.95f <= arrowrandomf && arrowrandomf <= 1.0f) {
                        cbireturn.setReturnValue(new ItemStack(ModItems.IRON_ARROW));
                    }
                    else {
                        cbireturn.setReturnValue(new ItemStack(ModItems.WOODEN_ARROW));
                    }
                }
            }
            else {
                cbireturn.setReturnValue(itemStack);
            }
        }
    }
}
