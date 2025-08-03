package solipingen.progressivearchery.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class FletchingRecipeCache {
    private final FletchingRecipeCache.CachedRecipe[] cache;
    private WeakReference<RecipeManager> recipeManagerRef = new WeakReference(null);


    public FletchingRecipeCache(int size) {
        this.cache = new FletchingRecipeCache.CachedRecipe[size];
    }

    public Optional<RecipeEntry<FletchingRecipe>> getRecipe(World world, CraftingRecipeInput recipeInput) {
        if (recipeInput.isEmpty()) {
            return Optional.empty();
        }
        else {
            this.validateRecipeManager(world);
            for(int i = 0; i < this.cache.length; ++i) {
                FletchingRecipeCache.CachedRecipe cachedRecipe = this.cache[i];
                if (cachedRecipe != null && cachedRecipe.matches(recipeInput.getStacks())) {
                    this.sendToFront(i);
                    return Optional.ofNullable(cachedRecipe.value());
                }
            }
            return this.getAndCacheRecipe(recipeInput, world);
        }
    }

    private void validateRecipeManager(World world) {
        RecipeManager recipeManager = world.getRecipeManager();
        if (recipeManager != this.recipeManagerRef.get()) {
            this.recipeManagerRef = new WeakReference(recipeManager);
            Arrays.fill(this.cache, null);
        }
    }

    private Optional<RecipeEntry<FletchingRecipe>> getAndCacheRecipe(CraftingRecipeInput recipeInput, World world) {

        Optional<RecipeEntry<FletchingRecipe>> optional = world.getRecipeManager() instanceof ServerRecipeManager serverRecipeManager ?
                serverRecipeManager.getFirstMatch(FletchingRecipe.Type.INSTANCE, recipeInput, world)
                : Optional.empty();
        this.cache(recipeInput.getStacks(), optional.orElse(null));
        return optional;
    }

    private void sendToFront(int index) {
        if (index > 0) {
            FletchingRecipeCache.CachedRecipe cachedRecipe = this.cache[index];
            System.arraycopy(this.cache, 0, this.cache, 1, index);
            this.cache[0] = cachedRecipe;
        }
    }

    private void cache(List<ItemStack> inputStacks, @Nullable RecipeEntry<FletchingRecipe> recipe) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inputStacks.size(), ItemStack.EMPTY);
        for(int i = 0; i < inputStacks.size(); ++i) {
            defaultedList.set(i, inputStacks.get(i).copyWithCount(1));
        }
        System.arraycopy(this.cache, 0, this.cache, 1, this.cache.length - 1);
        this.cache[0] = new FletchingRecipeCache.CachedRecipe(defaultedList, recipe);
    }


    static record CachedRecipe(DefaultedList<ItemStack> key, @Nullable RecipeEntry<FletchingRecipe> value) {
        CachedRecipe(DefaultedList<ItemStack> key, @Nullable RecipeEntry<FletchingRecipe> value) {
            this.key = key;
            this.value = value;
        }

        public boolean matches(List<ItemStack> inputs) {
            if (this.key.size() != inputs.size()) {
                return false;
            }
            else {
                for(int i = 0; i < this.key.size(); ++i) {
                    if (!ItemStack.areItemsAndComponentsEqual(this.key.get(i), inputs.get(i))) {
                        return false;
                    }
                }

                return true;
            }
        }

        public DefaultedList<ItemStack> key() {
            return this.key;
        }

        @Nullable
        public RecipeEntry<FletchingRecipe> value() {
            return this.value;
        }
    }

}
