package net.tokyosu.apocalypselib.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

/**
 * Contains some functions and helper for rarity.
 */
public class RarityUtils {
    private static final HashMap<Rarity, Style> STYLE_MAP = new HashMap<>();
    private static final @NotNull TextColor WHITE_COLOR = Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE));

    /**
     * Get Style by ItemStack. (Cached)
     * @param stack A valid ItemStack.
     * @return A valid Style.
     */
    public static @NotNull Style getStyle(@NotNull ItemStack stack) {
        if (stack.isEmpty()) return Style.EMPTY;
        return STYLE_MAP.computeIfAbsent(stack.getRarity(), e -> e.getStyleModifier().apply(Style.EMPTY));
    }

    /**
     * Get Style by Rarity. (Cached)
     * @param rarity A valid Rarity.
     * @return A valid Style.
     */
    public static @NotNull Style getStyleByRarity(@NotNull Rarity rarity) {
        return STYLE_MAP.computeIfAbsent(rarity, e -> e.getStyleModifier().apply(Style.EMPTY));
    }

    /**
     * Check if the current ItemStack is of rarity Common,
     * Don't check the current rarity using Rarity.COMMON, instead check the color of this rarity instead.
     * @param stack A valid ItemStack.
     * @return True if it's common rarity or false if color is null or != common rarity.
     */
    public static boolean isCommon(@NotNull ItemStack stack) {
        final var style = getStyle(stack);
        final var color = style.getColor();
        return color != null && color.equals(WHITE_COLOR);
    }
}
