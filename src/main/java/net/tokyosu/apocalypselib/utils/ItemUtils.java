package net.tokyosu.apocalypselib.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Contains some utils for inventory like creation of items.
 */
public final class ItemUtils {
    /**
     * Create an ItemStack using a resource name.
     * @param resourceName Item name, example: minecraft:apple
     * @return A valid ItemStack or ItemStack.EMPTY.
     */
    public static @NotNull ItemStack createStack(@NotNull String resourceName) {
        return createStack(resourceName, 1);
    }

    /**
     * Create an ItemStack using a resource name.
     * @param resourceName Item name, example: minecraft:apple
     * @param count Stack count.
     * @return A valid ItemStack or ItemStack.EMPTY.
     */
    public static @NotNull ItemStack createStack(@NotNull String resourceName, int count) {
        // Check if the resource name is not empty.
        if (resourceName.isEmpty()) return ItemStack.EMPTY;

        // Get and check if resource location is valid.
        var resourceLocation = ResourceLocation.tryParse(resourceName);
        if (resourceLocation == null) return ItemStack.EMPTY;

        var item = ResourceUtils.getItemByLocation(resourceLocation);
        // Check if the item is null.
        if (item == null) return ItemStack.EMPTY;

        // Now return the ItemStack (no nbt).
        return createStackFromItem(item, count);
    }

    /**
     * Create an ItemStack using a resource name and assign a nbt tag.
     * @param resourceName Item name, example: minecraft:enchanted_book
     * @param nbt A valid nbt tag to assign.
     * @return A valid nbt ItemStack or ItemStack.EMPTY.
     */
    public static @NotNull ItemStack createStackNBT(@NotNull String resourceName, @NotNull String nbt) {
        return createStackNBT(resourceName, nbt, 1);
    }

    /**
     * Create an ItemStack using a resource name and assign a nbt tag.
     * @param resourceName Item name, example: minecraft:enchanted_book
     * @param nbt A valid nbt tag to assign.
     * @param count Stack count.
     * @return A valid nbt ItemStack or ItemStack.EMPTY.
     */
    public static @NotNull ItemStack createStackNBT(@NotNull String resourceName, @NotNull String nbt, int count) {
        // Check if the resource name is not empty, same for nbt.
        if (resourceName.isEmpty() || nbt.isEmpty()) return ItemStack.EMPTY;

        // Get and check if resource location is valid.
        var resourceLocation = ResourceLocation.tryParse(resourceName);
        if (resourceLocation == null) return ItemStack.EMPTY;

        var item = ResourceUtils.getItemByLocation(resourceLocation);
        if (item == null) return ItemStack.EMPTY; // Check if the item is null.

        // Now we can create the ItemStack.
        return createStackFromItemNBT(item, nbt, count);
    }

    /**
     * Create a ItemStack from an Item.
     * @param item A valid item.
     * @return A valid ItemStack.
     */
    public static @NotNull ItemStack createStackFromItem(@NotNull Item item) {
        return createStackFromItem(item, 1);
    }

    /**
     * Create a ItemStack from an Item.
     * @param item A valid item.
     * @param count Stack count.
     * @return A valid ItemStack.
     */
    public static @NotNull ItemStack createStackFromItem(@NotNull Item item, int count) {
        var stack = new ItemStack(item);
        stack.setCount(count);
        return stack;
    }

    /**
     * Create a ItemStack from an Item and assign a nbt from string.
     * @param item A valid Item.
     * @param nbt A valid nbt tag.
     * @return A valid ItemStack or ItemStack.EMPTY.
     */
    public static @NotNull ItemStack createStackFromItemNBT(@NotNull Item item, @NotNull String nbt) {
        return createStackFromItemNBT(item, nbt, 1);
    }

    /**
     * Create a ItemStack from an Item and assign a nbt from string.
     * @param item A valid Item.
     * @param nbt A valid nbt tag.
     * @param count Stack count.
     * @return A valid ItemStack or ItemStack.EMPTY.
     */
    public static @NotNull ItemStack createStackFromItemNBT(@NotNull Item item, @NotNull String nbt, int count) {
        // For now, parse nbt into CompoundTag else return.
        CompoundTag nbtTag = TagUtils.stringToNBT(nbt);
        if (nbtTag == null) return ItemStack.EMPTY;

        // Now create the stack and assign the nbt tag.
        var stack = new ItemStack(item);
        stack.setTag(nbtTag);
        stack.setCount(count);
        return stack;
    }

    /**
     * Does the ItemStack A is the same as Item B
     * @param a A valid ItemStack.
     * @param b A valid Item.
     * @return True if both are the same, false otherwise.
     */
    public static boolean sameAs(@NotNull ItemStack a, @NotNull Item b) {
        return a.getItem().equals(b);
    }

    /**
     * Does the ItemEntity A have same stored value as Item B
     * @param a A valid ItemEntity.
     * @param b A valid Item.
     * @return True if both are the same, false otherwise.
     */
    public static boolean sameAs(@NotNull ItemEntity a, @NotNull Item b) {
        return a.getItem().getItem().equals(b);
    }
}
