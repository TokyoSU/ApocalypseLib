package net.tokyosu.apocalypselib.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

@SuppressWarnings("unused")
public class ModUtils {
    public static String getModName(String namespace) {
        return ModList.get()
                .getModContainerById(namespace)
                .map(container -> container.getModInfo().getDisplayName())
                .orElse(namespace); // Fallback to namespace if mod not found
    }

    public static int getModCount() {
        return ModList.get().size();
    }

    public static String getModName(ResourceLocation location) {
        return getModName(location.getNamespace());
    }
}
