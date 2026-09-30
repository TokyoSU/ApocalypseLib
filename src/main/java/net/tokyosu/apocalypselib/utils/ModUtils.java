package net.tokyosu.apocalypselib.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

public class ModUtils {
    /**
     * Get a mod name.
     * @param namespace A valid mod namespace.
     * @return Display name or else fallback to namespace argument value.
     */
    public static @NotNull String getModName(@NotNull String namespace) {
        return ModList.get()
                .getModContainerById(namespace)
                .map(container -> container.getModInfo().getDisplayName())
                .orElse(namespace); // Fallback to namespace if mod not found
    }

    /**
     * Get loaded mod count.
     */
    public static int getModCount() {
        return ModList.get().size();
    }

    /**
     * Get a mod name using ResourceLocation.
     * @param location A valid resource to find the mod name.
     * @return A valid display name.
     */
    public static @NotNull String getModName(@NotNull ResourceLocation location) {
        return getModName(location.getNamespace());
    }

    /**
     * Does a mod is loaded ?
     * @param namespace A valid mod id.
     * @return True if found, false otherwise.
     */
    public static boolean isLoaded(@NotNull String namespace) {
        return ModList.get().isLoaded(namespace);
    }
    
    /**
     * Get the minecraft font.
     * @apiNote This function have the suppress warning of 'resource', this avoid having this warning each time we use Minecraft.getInstance().font.
     * @return Minecraft valid font.
     */
    @SuppressWarnings("resource")
    public static @NotNull Font getFont() {
        return Minecraft.getInstance().font;
    }
}
