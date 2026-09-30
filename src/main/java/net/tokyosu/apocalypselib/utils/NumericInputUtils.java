package net.tokyosu.apocalypselib.utils;

/** Edit-box filters accept incomplete typing states; parsers supply explicit defaults. */
public final class NumericInputUtils {
    private NumericInputUtils() {}

    public static boolean isIntegerInput(String text) {
        if (text.isEmpty() || text.equals("-")) return true;
        try { Integer.parseInt(text); return true; }
        catch (NumberFormatException exception) { return false; }
    }

    public static boolean isDecimalInput(String text, double min, double max) {
        if (text.isEmpty() || text.equals("-") || text.equals(".") || text.equals("-.")) return true;
        try {
            double value = Double.parseDouble(text);
            return value >= min && value <= max;
        } catch (NumberFormatException exception) { return false; }
    }

    public static int integerOrDefault(String text, int fallback) {
        try { return Integer.parseInt(text); }
        catch (NumberFormatException exception) { return fallback; }
    }

    public static double decimalOrDefault(String text, double fallback) {
        try { return Double.parseDouble(text); }
        catch (NumberFormatException exception) { return fallback; }
    }
}
