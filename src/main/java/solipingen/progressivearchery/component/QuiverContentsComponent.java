package solipingen.progressivearchery.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.client.item.TooltipData;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.component.type.ModDataComponentTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;


public final class QuiverContentsComponent implements TooltipData {
    public static final QuiverContentsComponent DEFAULT = new QuiverContentsComponent(List.of());
    public static final Codec<QuiverContentsComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, QuiverContentsComponent> PACKET_CODEC;
    private static final Fraction NESTED_QUIVER_OCCUPANCY;
    private static final int ADD_TO_NEW_SLOT = -1;
    final List<ItemStack> stacks;
    final Fraction occupancy;

    QuiverContentsComponent(List<ItemStack> stacks, Fraction occupancy) {
        this.stacks = stacks;
        this.occupancy = occupancy;
    }

    public QuiverContentsComponent(List<ItemStack> stacks) {
        this(stacks, calculateOccupancy(stacks));
    }

    private static Fraction calculateOccupancy(List<ItemStack> stacks) {
        Fraction fraction = Fraction.ZERO;
        ItemStack itemStack;
        for(Iterator var2 = stacks.iterator(); var2.hasNext(); fraction = fraction.add(getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)))) {
            itemStack = (ItemStack)var2.next();
        }
        return fraction;
    }

    static Fraction getOccupancy(ItemStack stack) {
        QuiverContentsComponent quiverContentsComponent = stack.get(ModDataComponentTypes.QUIVER_CONTENTS);
        if (quiverContentsComponent != null) {
            return NESTED_QUIVER_OCCUPANCY.add(quiverContentsComponent.getOccupancy());
        }
        else {
            List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, List.of());
            return !list.isEmpty() ? Fraction.getFraction(1, 6) : Fraction.getFraction(1, 6*stack.getMaxCount());
        }
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        else if (!(o instanceof QuiverContentsComponent)) {
            return false;
        }
        else {
            QuiverContentsComponent quiverContentsComponent = (QuiverContentsComponent)o;
            return this.occupancy.equals(quiverContentsComponent.occupancy) && ItemStack.stacksEqual(this.stacks, quiverContentsComponent.stacks);
        }
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {
        return "QuiverContents" + String.valueOf(this.stacks);
    }

    static {
        CODEC = ItemStack.CODEC.listOf().xmap(QuiverContentsComponent::new, (component) -> {
            return component.stacks;
        });
        PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(QuiverContentsComponent::new, (component) -> {
            return component.stacks;
        });
        NESTED_QUIVER_OCCUPANCY = Fraction.getFraction(1, 96);
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private Fraction occupancy;

        public Builder(QuiverContentsComponent base) {
            this.stacks = new ArrayList(base.stacks);
            this.occupancy = base.occupancy;
        }

        public QuiverContentsComponent.Builder clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

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

        private int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.occupancy);
            return Math.max(fraction.divideBy(QuiverContentsComponent.getOccupancy(stack)).intValue(), 0);
        }

        public int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.getItem().canBeNested()) {
                int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
                if (i == 0) {
                    return 0;
                }
                else {
                    this.occupancy = this.occupancy.add(QuiverContentsComponent.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1)));
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
                    return i;
                }
            }
            else {
                return 0;
            }
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        public ItemStack set(int index, ItemStack stack) {
            if (this.stacks.isEmpty() && this.stacks.add(stack))  {
                return this.stacks.getFirst();
            }
            return this.stacks.set(index, stack);
        }

        public int getItemIndex(ItemStack stack) {
            for (ItemStack itemStack : this.stacks) {
                if (ItemStack.areItemsAndComponentsEqual(stack, itemStack)) {
                    return this.stacks.indexOf(itemStack);
                }
            }
            return -1;
        }

        @Nullable
        public ItemStack remove(int index) {
            return this.stacks.remove(index);
        }

        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) {
                return null;
            }
            else {
                ItemStack itemStack = ((ItemStack)this.stacks.remove(0)).copy();
                this.occupancy = this.occupancy.subtract(QuiverContentsComponent.getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
                return itemStack;
            }
        }

        public Fraction getOccupancy() {
            return this.occupancy;
        }

        public QuiverContentsComponent build() {
            return new QuiverContentsComponent(List.copyOf(this.stacks), this.occupancy);
        }
    }
}
