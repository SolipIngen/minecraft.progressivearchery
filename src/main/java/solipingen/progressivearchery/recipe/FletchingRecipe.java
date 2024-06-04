package solipingen.progressivearchery.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;


public class FletchingRecipe implements Recipe<Inventory> {
    final Ingredient head;
    final Ingredient body;
    final Ingredient tail;
    final Ingredient addition;
    final ItemStack output;

    
    public FletchingRecipe(Ingredient head, Ingredient body, Ingredient tail, Ingredient addition, ItemStack output) {
        this.head = head;
        this.body = body;
        this.tail = tail;
        this.addition = addition;
        this.output = output;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.head.test(inventory.getStack(0)) && this.body.test(inventory.getStack(1)) && this.tail.test(inventory.getStack(2)) && this.addition.test(inventory.getStack(3));
    }

    @Override
    public ItemStack craft(Inventory inventory, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = inventory.getStack(0).copyComponentsToNewStack(this.output.getItem(), this.output.getCount());
        itemStack.applyUnvalidatedChanges(this.output.getComponentChanges());
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 4 && height == 1;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(4);
        ingredients.add(this.head);
        ingredients.add(this.body);
        ingredients.add(this.tail);
        ingredients.add(this.addition);
        return ingredients;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.output;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public boolean testHead(ItemStack stack) {
        return this.head.test(stack);
    }

    public boolean testBody(ItemStack stack) {
        return this.body.test(stack);
    }

    public boolean testTail(ItemStack stack) {
        return this.tail.test(stack);
    }

    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Blocks.FLETCHING_TABLE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    

    public static class Type implements RecipeType<FletchingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "fletching";

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static class Serializer implements RecipeSerializer<FletchingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "fletching";
        private static final MapCodec<FletchingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group((Ingredient.ALLOW_EMPTY_CODEC.fieldOf("head")).forGetter(recipe -> recipe.head),
                (Ingredient.ALLOW_EMPTY_CODEC.fieldOf("body")).forGetter(recipe -> recipe.body),
                (Ingredient.ALLOW_EMPTY_CODEC.fieldOf("tail")).forGetter(recipe -> recipe.tail),
                (Ingredient.ALLOW_EMPTY_CODEC.fieldOf("addition")).forGetter(recipe -> recipe.addition),
                (ItemStack.VALIDATED_CODEC.fieldOf("output")).forGetter(recipe -> recipe.output))
                .apply(instance, (head, body, tail, addition, output) -> new FletchingRecipe((Ingredient)head, (Ingredient)body, (Ingredient)tail, (Ingredient)addition, (ItemStack)output))
            );
        public static final PacketCodec<RegistryByteBuf, FletchingRecipe> PACKET_CODEC = PacketCodec.ofStatic(FletchingRecipe.Serializer::write, FletchingRecipe.Serializer::read);


        @Override
        public MapCodec<FletchingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FletchingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public static FletchingRecipe read(RegistryByteBuf buf) {
            Ingredient head = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient body = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient tail = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient addition = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack itemStack = ItemStack.PACKET_CODEC.decode(buf);
            return new FletchingRecipe(head, body, tail, addition, itemStack);
        }

        public static void write(RegistryByteBuf buf, FletchingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.head);
            Ingredient.PACKET_CODEC.encode(buf, recipe.body);
            Ingredient.PACKET_CODEC.encode(buf, recipe.tail);
            Ingredient.PACKET_CODEC.encode(buf, recipe.addition);
            ItemStack.PACKET_CODEC.encode(buf, recipe.output);
        }
    }
}