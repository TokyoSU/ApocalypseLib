package net.tokyosu.apocalypselib.utils;

/**
 * Contains some function to help with UI.
 */
@SuppressWarnings("unused")
public class HudUtils {
    /**
     * Check in a rectangle way if a mouse is over it.
     * @param x Starting position in X Axis.
     * @param y Starting position in Y Axis.
     * @param mouseX Mouse position in X Axis.
     * @param mouseY Mouse position in Y Axis.
     * @return True if it's over else false.
     */
    public static boolean isMouseHoverRect(int x, int y, int mouseX, int mouseY) {
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
    public static boolean isMouseHoverRect(int x, int y, int mouseX, int mouseY, int pixelSize) {
        return mouseX >= x && mouseX <= x + pixelSize && mouseY >= y && mouseY <= y + pixelSize;
    }
}
