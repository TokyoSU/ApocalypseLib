package net.tokyosu.apocalypselib.utils;

import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ColorUtils {
    /**
     * Get an ARGB packed color from Rarity.
     * @param rarity A valid Rarity.
     * @return A valid ARGB packed color, or white color (shouldn't happen).
     */
    public static int getARGBFromRarity(@NotNull Rarity rarity) {
        final var style = RarityUtils.getStyleByRarity(rarity);
        final var styleColor = style.getColor();
        return styleColor != null ? styleColor.getValue() : 0xFFFFFFFF;
    }

    /**
     * Get a TextColor from an ARGB value.
     * @param argb A valid ARGB value.
     * @return A valid TextColor.
     */
    public static @NotNull TextColor getTextColorByARGB(int argb) {
        return TextColor.fromRgb(argb);
    }

    /**
     * (ARGB) Get the red color from a ARGB color. (Int)
     * @param argb An ARGB packed color.
     * @return A valid red component value from ARGB packed.
     */
    public static int getRed(int argb) {
        return FastColor.ARGB32.red(argb);
    }

    /**
     * (ARGB) Get the green color from a ARGB color. (Int)
     * @param argb An ARGB packed color.
     * @return A valid green component value from ARGB packed.
     */
    public static int getGreen(int argb) {
        return FastColor.ARGB32.green(argb);
    }

    /**
     * (ARGB) Get the blue color from a ARGB color. (Int)
     * @param argb An ARGB packed color.
     * @return A valid blue component value from ARGB packed.
     */
    public static int getBlue(int argb) {
        return FastColor.ARGB32.blue(argb);
    }

    /**
     * (ARGB) Get the alpha color from a ARGB color. (Int)
     * @param argb An ARGB packed color.
     * @return A valid alpha component value from ARGB packed.
     */
    public static int getAlpha(int argb) {
        return FastColor.ARGB32.alpha(argb);
    }

    /**
     * (ARGB) Get the red color from a ARGB color. (Byte)
     * @param argb An ARGB packed color.
     * @return A valid red component value from ARGB packed. (0 -> 255)
     */
    public static byte getRedByte(int argb) {
        return (byte)(FastColor.ARGB32.red(argb) & 0xFF);
    }

    /**
     * (ARGB) Get the green color from a ARGB color. (Byte)
     * @param argb An ARGB packed color.
     * @return A valid green component value from ARGB packed. (0 -> 255)
     */
    public static byte getGreenByte(int argb) {
        return (byte)(FastColor.ARGB32.green(argb) & 0xFF);
    }

    /**
     * (ARGB) Get the blue color from a ARGB color. (Byte)
     * @param argb An ARGB packed color.
     * @return A valid blue component value from ARGB packed. (0 -> 255)
     */
    public static byte getBlueByte(int argb) {
        return (byte)(FastColor.ARGB32.blue(argb) & 0xFF);
    }

    /**
     * (ARGB) Get the alpha color from a ARGB color. (Byte)
     * @param argb An ARGB packed color.
     * @return A valid alpha component value from ARGB packed. (0 -> 255)
     */
    public static byte getAlphaByte(int argb) {
        return (byte)(FastColor.ARGB32.alpha(argb) & 0xFF);
    }

    /**
     * (ARGB) Get the red color from a ARGB color. (Float)
     * @param argb An ARGB packed color.
     * @return A valid red component value from ARGB packed. (0 -> 1)
     */
    public static float getRedFloat(int argb) {
        return (float)FastColor.ARGB32.red(argb) / 255.0f;
    }

    /**
     * (ARGB) Get the green color from a ARGB color. (Float)
     * @param argb An ARGB packed color.
     * @return A valid green component value from ARGB packed. (0 -> 1)
     */
    public static float getGreenFloat(int argb) {
        return (float)FastColor.ARGB32.green(argb) / 255.0f;
    }

    /**
     * (ARGB) Get the blue color from a ARGB color. (Float)
     * @param argb An ARGB packed color.
     * @return A valid blue component value from ARGB packed. (0 -> 1)
     */
    public static float getBlueFloat(int argb) {
        return (float)FastColor.ARGB32.blue(argb) / 255.0f;
    }

    /**
     * (ARGB) Get the alpha color from a ARGB color. (Float)
     * @param argb An ARGB packed argb.
     * @return A valid alpha component value from ARGB packed. (0 -> 1)
     */
    public static float getAlphaFloat(int argb) {
        return (float)FastColor.ARGB32.alpha(argb) / 255.0f;
    }
}
