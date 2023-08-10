package solipingen.progressivearchery.recipe;

import com.google.gson.JsonObject;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;


public class FletchingRecipe implements Recipe<Inventory> {
    final Ingredient head;
    final Ingredient body;
    final Ingredient tail;
    final Ingredient addition;
    final ItemStack output;
    private final Identifier id;

    
    public FletchingRecipe(Identifier id, Ingredient head, Ingredient body, Ingredient tail, Ingredient addition, ItemStack output) {
        this.id = id;
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
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = this.output.copy();
        NbtCompound nbtCompound = inventory.getStack(0).getNbt();
        if (nbtCompound != null) {
            itemStack.setNbt(nbtCompound.copy());
        }
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
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
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
    public Identifier getId() {
        return this.id;
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

        @Override
        public FletchingRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient head = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "head"));
            Ingredient body = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "body"));
            Ingredient tail = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "tail"));
            Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "addition"));
            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "output"));
            return new FletchingRecipe(identifier, head, body, tail, addition, itemStack);
        }

        @Override
        public FletchingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient head = Ingredient.fromPacket(packetByteBuf);
            Ingredient body = Ingredient.fromPacket(packetByteBuf);
            Ingredient tail = Ingredient.fromPacket(packetByteBuf);
            Ingredient addition = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new FletchingRecipe(identifier, head, body, tail, addition, itemStack);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, FletchingRecipe fletchingRecipe) {
            fletchingRecipe.head.write(packetByteBuf);
            fletchingRecipe.body.write(packetByteBuf);
            fletchingRecipe.tail.write(packetByteBuf);
            fletchingRecipe.addition.write(packetByteBuf);
            packetByteBuf.writeItemStack(fletchingRecipe.output);
        }
    }
}