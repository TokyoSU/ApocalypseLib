package net.tokyosu.apocalypselib.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ModUtils {
    public static @NotNull String getModName(@NotNull String namespace) {
        return ModList.get()
                .getModContainerById(namespace)
                .map(container -> container.getModInfo().getDisplayName())
                .orElse(namespace); // Fallback to namespace if mod not found
    }

    public static int getModCount() {
        return ModList.get().size();
    }

    public static @NotNull String getModName(@NotNull ResourceLocation location) {
        return getModName(location.getNamespace());
    }
}
