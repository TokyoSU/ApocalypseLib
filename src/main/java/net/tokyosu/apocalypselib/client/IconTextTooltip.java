package net.tokyosu.apocalypselib.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Icon followed by vertically centered, scaled text. Height is explicit for compact tooltips. */
public interface IconTextTooltip extends ClientTooltipComponent {
    Component text();
    ResourceLocation icon();
    int color();

    default int iconSize() { return 16; }
    default int iconGap() { return 4; }
    default float textScale() { return 0.75F; }

    @Override
    default int getWidth(Font font) {
        return iconSize() + iconGap() + Mth.ceil(font.width(text()) * textScale());
    }

    @Override
    default void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        int size = iconSize();
        graphics.blit(icon(), x, y, 0, 0, size, size, size, size);
        graphics.pose().pushPose();
        try {
            graphics.pose().translate(x + size + iconGap(), y + (size - font.lineHeight * textScale()) * 0.5F, 0);
            graphics.pose().scale(textScale(), textScale(), 1);
            graphics.drawString(font, text(), 0, 0, color(), true);
        } finally { graphics.pose().popPose(); }
    }
}
