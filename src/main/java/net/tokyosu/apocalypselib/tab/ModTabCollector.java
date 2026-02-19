package net.tokyosu.apocalypselib.tab;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.tokyosu.apocalypselib.utils.ResourceUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ModTabCollector {
    private static final Map<String, ModTabInfo> MOD_TABS = new LinkedHashMap<>();

    public static void collectAllModTabs() {
        // First, collect all mods with their display names
        Map<String, String> modDisplayNames = new HashMap<>();
        for (var mod : ModList.get().getMods()) {
            String modId = mod.getModId();
            String displayName = mod.getDisplayName();
            modDisplayNames.put(modId, displayName != null ? displayName : modId);
        }

        // Then, match creative tabs to mods
        for (var creativeTab : BuiltInRegistries.CREATIVE_MODE_TAB) {
            var icon = creativeTab.getIconItem();
            if (icon.isEmpty()) continue;

            var resourceLocation = ResourceUtils.getResourcebyItem(icon.getItem());
            if (resourceLocation == null) continue;

            // Only add if we haven't added this mod yet and it exists
            var namespace = resourceLocation.getNamespace();
            if (modDisplayNames.containsKey(namespace) && !MOD_TABS.containsKey(namespace)) {
                MOD_TABS.putIfAbsent(namespace, new ModTabInfo(
                        namespace,
                        modDisplayNames.get(namespace),
                        icon.copy()
                ));
            }
        }
    }

    public static int getModCount() { return MOD_TABS.size(); }

    public static Map<String, ModTabInfo> getModTabs() {
        return MOD_TABS;
    }

    public static ModTabInfo getTabInfo(String namespace) {
        return MOD_TABS.get(namespace);
    }

    public record ModTabInfo(String namespace, String displayName, ItemStack iconItem) { }
}
