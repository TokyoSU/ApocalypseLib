package net.tokyosu.apocalypselib.tab;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.apocalypselib.menu.component.ScrollableGrid;
import net.tokyosu.apocalypselib.utils.ResourceUtils;

import java.util.*;

@SuppressWarnings("unused")
public class TabCollector {
    public static final Map<String, LinkedHashSet<ItemStack>> TAB_ITEMS = new HashMap<>();

    @SuppressWarnings("DataFlowIssue")
    public static void collectAllTabs() {
        TAB_ITEMS.clear();
        Set<String> seenItems = new HashSet<>();

        for (CreativeModeTab tab : BuiltInRegistries.CREATIVE_MODE_TAB) {
            try {
                for (ItemStack stack : tab.getDisplayItems()) {
                    if (stack.isEmpty()) continue;

                    // Use your ResourceUtils if available
                    ResourceLocation itemLocation = ResourceUtils.getResourcebyItem(stack.getItem());
                    if (itemLocation == null) continue;

                    String namespace = itemLocation.getNamespace();

                    // Create unique key (item + NBT)
                    String uniqueKey = itemLocation + (stack.hasTag() ? stack.getTag().toString() : "");

                    // Only add if not seen before
                    if (seenItems.add(uniqueKey)) {
                        TAB_ITEMS.computeIfAbsent(namespace, k -> new LinkedHashSet<>(ScrollableGrid.MAX_SLOTS / 2)).add(stack.copy()); // Allocate enough to avoid doing that with add().
                    }
                }
            } catch (Exception e) {
                // Skip problematic tabs
            }
        }
    }
}
