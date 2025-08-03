package solipingen.progressivearchery.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.recipe.book.ModRecipeBookCategories;
import solipingen.progressivearchery.recipe.display.FletchingRecipeDisplay;
import solipingen.progressivearchery.registry.tag.ModItemTags;

import java.util.List;


public class FletchingRecipe implements Recipe<CraftingRecipeInput> {
    private final Ingredient head;
    private final Ingredient body;
    private final Ingredient tail;
    private final Ingredient addition;
    private final ItemStack result;
    @Nullable private IngredientPlacement ingredientPlacement;

    
    public FletchingRecipe(Ingredient head, Ingredient body, Ingredient tail, Ingredient addition, ItemStack result) {
        this.head = head;
        this.body = body;
        this.tail = tail;
        this.addition = addition;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingRecipeInput recipeInput, World world) {
        if (recipeInput.size() >= 4) {
            return this.head.test(recipeInput.getStackInSlot(0)) && this.body.test(recipeInput.getStackInSlot(1)) && this.tail.test(recipeInput.getStackInSlot(2)) && this.addition.test(recipeInput.getStackInSlot(3));
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack craft(CraftingRecipeInput recipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = recipeInput.getStackInSlot(0).copyComponentsToNewStack(this.result.getItem(), this.result.getCount());
        itemStack.applyUnvalidatedChanges(this.result.getComponentChanges());
        return itemStack;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return ModRecipeBookCategories.FLETCHING;
    }

    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(4);
        ingredients.add(this.head);
        ingredients.add(this.body);
        ingredients.add(this.tail);
        ingredients.add(this.addition);
        return ingredients;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forShapeless(List.of(this.head, this.body, this.tail, this.addition));
        }
        return this.ingredientPlacement;
    }

    public ItemStack getResult() {
        return this.result;
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

    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput recipeInput) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(recipeInput.size(), ItemStack.EMPTY);
        for (int i = 0; i < defaultedList.size(); ++i) {
            Item item = recipeInput.getStackInSlot(i).getItem();
            if (!item.getRecipeRemainder().isEmpty() && !(i == 3 && recipeInput.getStackInSlot(i).isIn(ModItemTags.UNCONSUMED_FLETCHING_ADDITIONS))) {
                defaultedList.set(i, item.getRecipeRemainder());
            }
        }
        return defaultedList;
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(new FletchingRecipeDisplay(this.head.toDisplay(), this.body.toDisplay(), this.tail.toDisplay(), this.addition.toDisplay(),
                new SlotDisplay.StackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.FLETCHING_TABLE)));
    }

//    @Override
//    public ItemStack createIcon() {
//        return new ItemStack(Blocks.FLETCHING_TABLE);
//    }

    @Override
    public RecipeSerializer<FletchingRecipe> getSerializer() {
        return FletchingRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<FletchingRecipe> getType() {
        return FletchingRecipe.Type.INSTANCE;
    }


    public static class Type implements RecipeType<FletchingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "fletching";

    }

    public static class Serializer implements RecipeSerializer<FletchingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "fletching";
        private static final MapCodec<FletchingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(Ingredient.CODEC.fieldOf("head").forGetter(recipe -> recipe.head),
                Ingredient.CODEC.fieldOf("body").forGetter(recipe -> recipe.body),
                Ingredient.CODEC.fieldOf("tail").forGetter(recipe -> recipe.tail),
                Ingredient.CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition),
                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result))
                .apply(instance, FletchingRecipe::new)
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
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
        }
    }
}