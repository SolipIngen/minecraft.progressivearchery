package solipingen.progressivearchery.component.type;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import solipingen.progressivearchery.component.ModDataComponentTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;


public final class QuiverContentsComponent implements TooltipData {
    public static final QuiverContentsComponent DEFAULT = new QuiverContentsComponent(List.of());
    public static final Codec<QuiverContentsComponent> CODEC = ItemStack.CODEC.listOf()
            .flatXmap(QuiverContentsComponent::validateOccupancy, component -> DataResult.success(component.stacks));
    public static final PacketCodec<RegistryByteBuf, QuiverContentsComponent> PACKET_CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList()).xmap(QuiverContentsComponent::new, component -> component.stacks);
    private static final Fraction NESTED_QUIVER_OCCUPANCY = Fraction.getFraction(1, 96);
    private final List<ItemStack> stacks;
    private final Fraction occupancy;


    private static Fraction calculateOccupancy(List<ItemStack> stacks) {
        Fraction fraction = Fraction.ZERO;
        if (!stacks.isEmpty()) {
            for (ItemStack itemStack : stacks) {
                fraction = fraction.add(QuiverContentsComponent.getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
            }
        }
        return fraction;
    }

    private static Fraction getOccupancy(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (quiverContentsComponent != null) {
            return NESTED_QUIVER_OCCUPANCY.add(quiverContentsComponent.getOccupancy());
        }
        else {
            return Fraction.getFraction(1, 6*stack.getMaxCount());
        }
    }

    private static DataResult<QuiverContentsComponent> validateOccupancy(List<ItemStack> stacks) {
        try {
            Fraction fraction = QuiverContentsComponent.calculateOccupancy(stacks);
            return DataResult.success(new QuiverContentsComponent(stacks, fraction));
        }
        catch (ArithmeticException var2) {
            return DataResult.error(() -> "Excessive total bundle weight");
        }
    }

    QuiverContentsComponent(List<ItemStack> stacks, Fraction occupancy) {
        this.stacks = stacks;
        this.occupancy = occupancy;
    }

    public QuiverContentsComponent(List<ItemStack> stacks) {
        this(stacks, QuiverContentsComponent.calculateOccupancy(stacks));
    }

    public int getNumberOfStacksShown() {
        int i = this.size();
        int j = i > 12 ? 11 : 12;
        int k = i % 4;
        int l = k == 0 ? 0 : 4 - k;
        return Math.min(i, j - l);
    }

    public ItemStack get(int index) {
        return this.stacks.get(index);
    }

    public Stream<ItemStack> stream() {
        return this.stacks.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> iterate() {
        return this.stacks;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.transform(this.stacks, ItemStack::copy);
    }

    public int size() {
        return this.stacks.size();
    }

    public Fraction getOccupancy() {
        return this.occupancy;
    }

    public boolean isEmpty() {
        return this.stacks.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        else if (!(o instanceof QuiverContentsComponent quiverContentsComponent)) {
            return false;
        }
        else {
            return this.occupancy.equals(quiverContentsComponent.occupancy) && ItemStack.stacksEqual(this.stacks, quiverContentsComponent.stacks);
        }
    }

    public boolean contains(ItemStack stack) {
        for (ItemStack containedStack : this.stacks) {
            if (ItemStack.areEqual(stack, containedStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    @Override
    public String toString() {
        return "QuiverContents" + this.stacks;
    }


    public static class Builder {
        private final List<ItemStack> stacks;


        public Builder(QuiverContentsComponent base) {
            this.stacks = new ArrayList<>(base.stacks);
        }

        public QuiverContentsComponent.Builder clear() {
            this.stacks.clear();
            return this;
        }

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

        private int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.getOccupancy());
            return Math.max(fraction.divideBy(QuiverContentsComponent.getOccupancy(stack)).intValue(), 0);
        }

        public int add(ItemStack stack) {
            int count = 0;
            while (!stack.isEmpty() && stack.getItem().canBeNested() && this.getMaxAllowed(stack) > 0) {
                int j = this.getInsertionIndex(stack);
                if (j == -1) {
                    int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
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
            return count;
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        public ItemStack set(int index, ItemStack stack) {
            if (this.stacks.isEmpty()) {
                this.stacks.add(stack);
                return this.stacks.getFirst();
            }
            else {
                ItemStack itemStack = this.stacks.set(index, stack);
                return itemStack;
            }
        }

        public int getItemIndex(ItemStack stack) {
            for (ItemStack itemStack : this.stacks) {
                if (ItemStack.areItemsAndComponentsEqual(stack, itemStack)) {
                    return this.stacks.indexOf(itemStack);
                }
            }
            return -1;
        }

        public boolean isWithinBounds(int index) {
            return index >= 0 && index < this.stacks.size();
        }

        public ItemStack remove(int index) {
            ItemStack itemStack = ItemStack.EMPTY;
            if (!this.stacks.isEmpty()) {
                int i = this.isWithinBounds(index) ? index : 0;
                itemStack = this.stacks.remove(i).copy();
            }
            return itemStack;
        }

        public Fraction getOccupancy() {
            return QuiverContentsComponent.calculateOccupancy(this.stacks);
        }

        public QuiverContentsComponent build() {
            return new QuiverContentsComponent(List.copyOf(this.stacks), this.getOccupancy());
        }
    }

}
