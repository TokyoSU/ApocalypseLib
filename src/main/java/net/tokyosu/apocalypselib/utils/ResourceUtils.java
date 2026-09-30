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

    /**
     * Get a ResourceLocation from an Item.
     * @param item A valid Item.
     * @return A valid ResourceLocation or null if item is invalid.
     */
    public static @Nullable ResourceLocation getResourcebyItem(@NotNull Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
    
    /** Parse a required resource ID, using the supplied namespace when no colon is present. */
    public static ResourceLocation parseRequired(String value, String defaultNamespace) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Resource ID cannot be empty");
        String normalized = value.trim();
        int separator = normalized.indexOf(':');
        return separator >= 0
                ? ResourceLocation.fromNamespaceAndPath(normalized.substring(0, separator), normalized.substring(separator + 1))
                : ResourceLocation.fromNamespaceAndPath(defaultNamespace, normalized);
    }
}
