package net.tokyosu.apocalypselib.client;

import net.minecraft.client.gui.GuiGraphics;

/** Client-only drawing primitives; layout and styling belong to callers. */
public final class GuiDrawUtils {
    private GuiDrawUtils() {}

    public static void drawBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + 1, color);
        graphics.fill(x, y + height - 1, x + width, y + height, color);
        graphics.fill(x, y, x + 1, y + height, color);
        graphics.fill(x + width - 1, y, x + width, y + height, color);
    }
}
