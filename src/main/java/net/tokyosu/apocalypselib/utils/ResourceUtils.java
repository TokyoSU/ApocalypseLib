package net.tokyosu.apocalypselib.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains some functions to check or validate resource location.
 */
public class ResourceUtils {
    /**
     * Does a ResourceLocation is associated with an item ?
     * @param location A valid ResourceLocation.
     * @return True if valid or false if item is not found.
     */
    public static boolean isItemValid(@NotNull ResourceLocation location) {
        return ForgeRegistries.ITEMS.containsKey(location);
    }

    /**
     * Get an item by resource location.
     * @param location A valid ResourceLocation
     * @return A valid item or null.
     */
    public static @Nullable Item getItemByLocation(@NotNull ResourceLocation location) {
        return isItemValid(location) ? ForgeRegistries.ITEMS.getValue(location) : null;
    }
}
