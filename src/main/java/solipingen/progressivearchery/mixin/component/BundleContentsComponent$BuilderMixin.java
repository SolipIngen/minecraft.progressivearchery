package solipingen.progressivearchery.mixin.component;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.component.QuiverContentsComponent;

import java.util.List;


@Mixin(BundleContentsComponent.Builder.class)
public abstract class BundleContentsComponent$BuilderMixin {
    @Shadow @Final private List<ItemStack> stacks;
    @Shadow private Fraction occupancy;

    @Invoker("getMaxAllowed")
    public abstract int invokeGetMaxAllowed(ItemStack stack);


    @Inject(method = "add(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void injectedAdd(ItemStack stack, CallbackInfoReturnable<Integer> cbireturn) {
        if (!stack.isEmpty() && stack.getItem().canBeNested()) {
            int i = Math.min(stack.getCount(), this.invokeGetMaxAllowed(stack));
            if (i == 0) {
                cbireturn.setReturnValue(0);
            }
            else {
                this.occupancy = this.occupancy.add(this.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1)));
                int j = this.addInternal(stack);
                if (j != -1) {
                    ItemStack itemStack = this.stacks.remove(j);
                    int k = Math.min(itemStack.getCount() + i, itemStack.getMaxCount());
                    ItemStack itemStack2 = itemStack.copyWithCount(k);
                    stack.decrement(k - itemStack.getCount());
                    this.stacks.add(j, itemStack2);
                    if (this.occupancy.floatValue() < 1.0f && !stack.isEmpty()) {
                        ItemStack itemStack3 = stack.copy();
                        stack.decrement(stack.getCount());
                        this.stacks.add(j, itemStack3);
                    }
                }
                else {
                    this.stacks.addFirst(stack.split(i));
                }
                cbireturn.setReturnValue(i);
            }
        }
        else {
            cbireturn.setReturnValue(0);
        }
    }

    @Unique
    private int addInternal(ItemStack stack) {
        if (stack.isStackable()) {
            for (int i = 0; i < this.stacks.size(); ++i) {
                if (ItemStack.areItemsAndComponentsEqual(this.stacks.get(i), stack)) {
                    if (this.stacks.get(i).getCount() >= stack.getMaxCount()) continue;
                    return i;
                }
            }
        }
        return -1;
    }

    @Unique
    private Fraction getOccupancy(ItemStack stack) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent != null) {
            return Fraction.getFraction(1, 192).add(bundleContentsComponent.getOccupancy());
        }
        else {
            List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, List.of());
            return !list.isEmpty() ? Fraction.getFraction(1, 12) : Fraction.getFraction(1, 12*stack.getMaxCount());
        }
    }


}
