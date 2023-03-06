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
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModItems;


@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends PathAwareEntity implements Monster {
    
    
    protected HostileEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getArrowType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"), cancellable = true)
    private void injectedGetArrowType(ItemStack stack, CallbackInfoReturnable<ItemStack> cbireturn) {
        Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
        Random random = world.getRandom();
        float arrowrandomf = random.nextFloat();
        if (stack.isOf(ModItems.WOODEN_TUBULAR_BOW) || stack.isOf(ModItems.COPPER_FUSED_TUBULAR_BOW) || stack.isOf(ModItems.GOLD_FUSED_TUBULAR_BOW) || stack.isOf(ModItems.IRON_FUSED_TUBULAR_BOW) || stack.isOf(ModItems.DIAMOND_FUSED_TUBULAR_BOW)) {
            if (itemStack.isEmpty()) {
                if (arrowrandomf < 0.33f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.WOODEN_KID_ARROW));
                }
                else if (0.33f <= arrowrandomf && arrowrandomf < 0.67f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.FLINT_KID_ARROW));
                }
                else if (0.67f <= arrowrandomf && arrowrandomf < 0.9f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.COPPER_KID_ARROW));
                }
                else if (0.9f <= arrowrandomf && arrowrandomf < 0.97f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.GOLDEN_KID_ARROW));
                }
                else if (0.97f <= arrowrandomf && arrowrandomf < 1.0f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.IRON_KID_ARROW));
                }
            }
            else {
                cbireturn.setReturnValue(itemStack);
            }
        }
        else {
            if (itemStack.isEmpty()) {
                if (arrowrandomf < 0.33f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.WOODEN_ARROW));
                }
                else if (0.33f <= arrowrandomf && arrowrandomf < 0.67f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.FLINT_ARROW));
                }
                else if (0.67f <= arrowrandomf && arrowrandomf < 0.9f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.COPPER_ARROW));
                }
                else if (0.9f <= arrowrandomf && arrowrandomf < 0.97f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.GOLDEN_ARROW));
                }
                else if (0.97f <= arrowrandomf && arrowrandomf < 1.0f) {
                    cbireturn.setReturnValue(new ItemStack(ModItems.IRON_ARROW));
                }
            }
            else {
                cbireturn.setReturnValue(itemStack);
            }
        }
    }
}
