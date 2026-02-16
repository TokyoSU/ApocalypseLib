package net.tokyosu.apocalypselib.utils;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class SearchUtils {
    private static final Map<ItemStack, List<Component>> TOOLTIP_CACHE = new WeakHashMap<>();

    /**
     * Extensive research to include almost anything from tooltip to nbt and display name etc...
     * @param stack A valid ItemStack.
     * @return True if anything is found inside the item that contains this.searchFilter !
     */
    public static boolean matches(@NotNull ItemStack stack, @NotNull String searchFilter) {
        var search = searchFilter.toLowerCase();

        // 1. Search by display name (existing behavior)
        if (stack.getHoverName().getString().toLowerCase().contains(search)) {
            return true;
        }

        // 2. Search by item registry name (e.g., "enchanted_book")
        var itemName = stack.getItem().toString().toLowerCase();
        if (itemName.contains(search)) {
            return true;
        }

        // 3. Search by tooltip content (includes enchantments, lore, etc.)
        if (stack.hasTag() && stack.getTag() != null) {
            var tag = stack.getTag();

            // Get minecraft instance safely
            // Use cached tooltip to avoid lag each time its called.
            var tooltip = TOOLTIP_CACHE.computeIfAbsent(stack, s -> {
                var mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.player != null) {
                    return s.getTooltipLines(mc.player, TooltipFlag.Default.NORMAL);
                }
                return Collections.emptyList();
            });
            for (var line : tooltip) {
                if (line.getString().toLowerCase().contains(search)) {
                    return true;
                }
            }

            // 4. Search directly in NBT for stored enchantments (enchanted books)
            if (tag.contains("StoredEnchantments", 9)) {
                var enchantments = tag.getList("StoredEnchantments", 10);
                if (foundEnchantmentFromNBT(search, enchantments)) return true;
            }

            // 5. Search regular enchantments (on tools/armor)
            if (tag.contains("Enchantments", 9)) {
                var enchantments = tag.getList("Enchantments", 10);
                return foundEnchantmentFromNBT(search, enchantments);
            }
        }

        return false;
    }

    private static boolean foundEnchantmentFromNBT(@NotNull String search, @NotNull ListTag enchantments) {
        for (int i = 0; i < enchantments.size(); i++) {
            net.minecraft.nbt.CompoundTag enchTag = enchantments.getCompound(i);
            String enchId = enchTag.getString("id");
            // Search by enchantment ID (e.g., "minecraft:protection")
            if (enchId.toLowerCase().contains(search)) {
                return true;
            }
        }
        return false;
    }
}
