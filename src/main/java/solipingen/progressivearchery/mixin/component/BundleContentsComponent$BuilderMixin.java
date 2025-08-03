package solipingen.progressivearchery.mixin.component;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BeesComponent;
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
import solipingen.progressivearchery.component.ModDataComponentTypes;
import solipingen.progressivearchery.component.type.QuiverContentsComponent;

import java.util.List;


@Mixin(BundleContentsComponent.Builder.class)
public abstract class BundleContentsComponent$BuilderMixin {
    @Shadow @Final private List<ItemStack> stacks;
    @Shadow private Fraction occupancy;

    @Invoker("getMaxAllowed")
    public abstract int invokeGetMaxAllowed(ItemStack stack);


    @Inject(method = "add(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void injectedAdd(ItemStack stack, CallbackInfoReturnable<Integer> cbireturn) {
        Fraction itemOccupancy = BundleContentsComponent$BuilderMixin.getOccupancy(stack);
        int count = 0;
        while (!stack.isEmpty() && stack.getItem().canBeNested() && this.invokeGetMaxAllowed(stack) > 0) {
            int j = this.getInsertionIndex(stack);
            if (j == -1) {
                int i = Math.min(stack.getCount(), this.invokeGetMaxAllowed(stack));
                ItemStack itemStack = stack.split(i);
                this.stacks.addFirst(itemStack);
                count += this.stacks.getFirst().getCount();
            }
            else {
                int k = Math.min(stack.getCount(), this.stacks.get(j).getMaxCount() - this.stacks.get(j).getCount());
                ItemStack itemStack = stack.split(k);
                this.stacks.get(j).increment(itemStack.getCount());
                count += itemStack.getCount();
            }
        }
        this.occupancy = this.occupancy.add(itemOccupancy.multiplyBy(Fraction.getFraction(count, 1)));
        cbireturn.setReturnValue(count);
    }

    @Unique
    private int getInsertionIndex(ItemStack stack) {
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
    private static Fraction getOccupancy(ItemStack stack) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (bundleContentsComponent != null) {
            return Fraction.getFraction(1, 192).add(bundleContentsComponent.getOccupancy());
        }
        else if (quiverContentsComponent != null) {
            return Fraction.getFraction(1, 384).add(quiverContentsComponent.getOccupancy());
        }
        else {
            List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, BeesComponent.DEFAULT).bees();
            return !list.isEmpty() ? Fraction.getFraction(1, 12) : Fraction.getFraction(1, 12*stack.getMaxCount());
        }
    }


}
