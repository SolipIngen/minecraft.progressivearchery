package solipingen.progressivearchery.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import solipingen.progressivearchery.registry.tag.ModItemTags;


public class FletchingRecipe implements Recipe<CraftingRecipeInput> {
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
    public boolean matches(CraftingRecipeInput recipeInput, World world) {
        if (recipeInput.getSize() >= 4) {
            return this.head.test(recipeInput.getStackInSlot(0)) && this.body.test(recipeInput.getStackInSlot(1)) && this.tail.test(recipeInput.getStackInSlot(2)) && this.addition.test(recipeInput.getStackInSlot(3));
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack craft(CraftingRecipeInput recipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = recipeInput.getStackInSlot(0).copyComponentsToNewStack(this.output.getItem(), this.output.getCount());
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
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput recipeInput) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(recipeInput.getSize(), ItemStack.EMPTY);
        for (int i = 0; i < defaultedList.size(); ++i) {
            Item item = recipeInput.getStackInSlot(i).getItem();
            if (item.hasRecipeRemainder() && !(i == 3 && recipeInput.getStackInSlot(i).isIn(ModItemTags.UNCONSUMED_FLETCHING_ADDITIONS))) {
                defaultedList.set(i, new ItemStack(item.getRecipeRemainder()));
            }
        }
        return defaultedList;
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