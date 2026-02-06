package net.tokyosu.apocalypselib.utils;

/**
 * Contains some function to help with UI.
 */
public class HudUtils {
    /**
     * Check in a rectangle way if a mouse is over it.
     * @param x Starting position in X Axis.
     * @param y Starting position in Y Axis.
     * @param mouseX Mouse position in X Axis.
     * @param mouseY Mouse position in Y Axis.
     * @return True if it's over else false.
     */
    public static boolean isMouseHoverRect(int x, int y, double mouseX, double mouseY) {
        return isMouseHoverRect(x, y, mouseX, mouseY, 16);
    }

    /**
     * Check in a rectangle way if a mouse is over it.
     * @param x Starting position in X Axis.
     * @param y Starting position in Y Axis.
     * @param mouseX Mouse position in X Axis.
     * @param mouseY Mouse position in Y Axis.
     * @param pixelSize Size check between X/Y and MouseX/Y, example: 16 like a slot.
     * @return True if it's over else false.
     */
    public static boolean isMouseHoverRect(int x, int y, double mouseX, double mouseY, int pixelSize) {
        mouseX -= x;
        mouseY -= y;
        return  mouseX >= ((double)x - 1.0) && mouseX < ((double)x + (double)pixelSize + 1.0) &&
                mouseY >= ((double)y - 1.0) && mouseY < ((double)y + (double)pixelSize + 1.0);
    }
}
