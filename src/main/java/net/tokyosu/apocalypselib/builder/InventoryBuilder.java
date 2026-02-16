package net.tokyosu.apocalypselib.builder;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.apocalypselib.menu.button.HoverButton;
import net.tokyosu.apocalypselib.menu.button.TabButton;
import net.tokyosu.apocalypselib.utils.HudUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Helper to create inventory, contains some functions to help.
 * Also avoid doing relative position each time for .
 */
@SuppressWarnings("unused")
public class InventoryBuilder {
    private final ResourceLocation background;
    private final int textureWidth;
    private final int textureHeight;
    private final int width;
    private final int height;
    private GuiGraphics pGui;
    private Font pFont;
    private int posX;
    private int posY;

    /**
     * Base value required for the InventoryBuilder.
     * @param pBackground A background texture.
     * @param width Width of the UI.
     * @param height Height of the UI.
     * @param pTextureWidth Texture width of the background.
     * @param pTextureHeight Texture height of the background.
     */
    public InventoryBuilder(@NotNull ResourceLocation pBackground, int width, int height, int pTextureWidth, int pTextureHeight) {
        this.background = pBackground;
        this.width = width;
        this.height = height;
        this.textureWidth = pTextureWidth;
        this.textureHeight = pTextureHeight;
    }

    /**
     * Create an inventory builder.
     * @param pBackground A background texture.
     * @param width Width of the UI.
     * @param height Height of the UI.
     * @param pTextureWidth Texture width of the background.
     * @param pTextureHeight Texture height of the background.
     * @return A valid InventoryBuilder.
     */
    public static @NotNull InventoryBuilder create(@NotNull ResourceLocation pBackground, int width, int height, int pTextureWidth, int pTextureHeight) {
        return new InventoryBuilder(pBackground, width, height, pTextureWidth, pTextureHeight);
    }

    /**
     * Initialize the GUI position.
     * @param width Minecraft width.
     * @param height Minecraft height.
     */
    public void init(int width, int height) {
        this.posX = (width - this.width) / 2;
        this.posY = (height - this.height) / 2;
    }

    /**
     * Get the UI position X (top-left).
     */
    public int getPosX() {
        return this.posX;
    }

    /**
     * Get the UI position Y (top-left).
     */
    public int getPosY() {
        return this.posY;
    }

    /**
     * Set the GuiGraphics to draw thinks.
     * @param pGuiGraphics A valid GuiGraphics.
     */
    public void setGraphics(@NotNull GuiGraphics pGuiGraphics) {
        this.pGui = pGuiGraphics;
    }

    /**
     * Set the font used for this GUI.
     * @param pFont A valid Font.
     */
    public void setFont(@NotNull Font pFont) {
        this.pFont = pFont;
    }

    /**
     * Draw a background relative to top-left corner of the GUI.
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param texX Size X of the background.
     * @param texY Size Y of the background.
     */
    public void drawBackground(int x, int y, int texX, int texY) {
        pGui.blit(this.background, this.posX + x, this.posY + y, texX, texY, this.width, this.height, this.textureWidth, this.textureHeight);
    }

    /**
     * Draw a text relative to top-left corner of the GUI.
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param text A valid text Component.
     * @param colorRGB Color of the text, usually WHITE.
     */
    public void drawString(int x, int y, @NotNull Component text, int colorRGB) {
        if (this.pFont != null) {
            pGui.drawString(this.pFont, text, this.posX + x, this.posY + y, colorRGB);
        }
    }

    /**
     * Draw a pulsating text relative to top-left corner of the GUI.
     * @param text A valid text Component.
     * @param x Starting X position.
     * @param y Starting Y position.
     */
    public void drawPulsatingString(@NotNull Component text, int x, int y) {
        if (this.pFont == null) return;

        float time = (float)(System.currentTimeMillis() % 1000L) / 1000.0F;
        float pulse = 1.0F + 0.05F * Mth.sin(time * (float)Math.PI * 2.0F);
        int alpha = (int)(255 * (0.7F + 0.3F * Mth.sin(time * (float)Math.PI * 2.0F)));

        // Measure string dimensions
        int textWidth = this.pFont.width(text.getString());
        int textHeight = this.pFont.lineHeight;

        this.pGui.pose().pushPose();
        this.pGui.pose().translate(this.posX + x, this.posY + y, 305.0F); // Make it above items !
        this.pGui.pose().scale(pulse, pulse, 1.0F);
        this.pGui.pose().rotateAround(
                Axis.ZP.rotationDegrees(-45.0F),
                0.0F,
                0.0F,
                0.0F
        );
        this.pGui.pose().translate(-textWidth / 2f, -textHeight / 2f, 0); // Align text correctly

        // Draw text with alpha
        this.pGui.drawCenteredString(
                this.pFont,
                text,
                -(textWidth / 2),
                -(textHeight / 2),
                0xFFFFFF | (alpha << 24)
        );

        this.pGui.pose().popPose();
    }

    /**
     * Draw a ItemStack icon relative to top-left corner of the GUI.
     * @param stack A valid ItemStack.
     * @param x Starting X position.
     * @param y Starting Y position.
     */
    public void drawIcon(@NotNull ItemStack stack, int x, int y) {
        if (stack.isEmpty()) return;
        pGui.renderFakeItem(stack, this.posX + x, this.posY + y);
    }

    /**
     * Draw an ItemStack tooltip if mouse overlap the slot.
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param text A valid text for the tooltip.
     * @param pMouseX Mouse position X.
     * @param pMouseY Mouse position Y.
     */
    public void drawTooltip(int x, int y, @NotNull Component text, int pMouseX, int pMouseY) {
        if (this.pFont != null && HudUtils.isMouseHoverRect(x, y, pMouseX, pMouseY)) {
            pGui.renderTooltip(this.pFont, text, pMouseX, pMouseY);
        }
    }

    /**
     * Draw an ItemStack tooltip if mouse overlap, use size to define the border.
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param text A valid text for the tooltip.
     * @param pMouseX Mouse position X.
     * @param pMouseY Mouse position Y.
     * @param size Size of the checked rectangle, usually 16.
     */
    public void drawTooltip(int x, int y, @NotNull Component text, int pMouseX, int pMouseY, int size) {
        if (this.pFont != null && HudUtils.isMouseHoverRect(x, y, pMouseX, pMouseY, size)) {
            pGui.renderTooltip(this.pFont, text, pMouseX, pMouseY);
        }
    }

    /**
     * Draw an ItemStack icon, and if mouse overlap the slot then draw the tooltip.
     * @param stack A valid ItemStack.
     * @param x Starting X position for the icon.
     * @param y Starting Y position for the icon.
     * @param pMouseX Mouse position X.
     * @param pMouseY Mouse position Y.
     */
    public void drawIconWithTooltip(@NotNull ItemStack stack, int x, int y, int pMouseX, int pMouseY) {
        this.drawIcon(stack, x, y);
        if (this.pFont != null && HudUtils.isMouseHoverRect(x, y, pMouseX, pMouseY)) {
            pGui.renderTooltip(this.pFont, Screen.getTooltipFromItem(Minecraft.getInstance(), stack), stack.getTooltipImage(), pMouseX, pMouseY);
        }
    }

    /**
     * Draw an ItemStack icon, and if mouse overlap the slot then draw the tooltip.
     * @param stack A valid ItemStack.
     * @param x Starting X position for the icon.
     * @param y Starting Y position for the icon.
     * @param pMouseX Mouse position X.
     * @param pMouseY Mouse position Y.
     * @param size Size of the checked rectangle, usually 16.
     */
    public void drawIconWithTooltip(@NotNull ItemStack stack, int x, int y, int pMouseX, int pMouseY, int size) {
        this.drawIcon(stack, x, y);
        if (this.pFont != null && HudUtils.isMouseHoverRect(x, y, pMouseX, pMouseY, size)) {
            pGui.renderTooltip(this.pFont, Screen.getTooltipFromItem(Minecraft.getInstance(), stack), stack.getTooltipImage(), pMouseX, pMouseY);
        }
    }

    /**
     * Create a relative hover button (still need to be build()).
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param integratedTexture Does the texture is used by the GUI too, use false if the hover texture is separated !
     * @return A valid hover builder.
     */
    public @NotNull HoverButton.HoverBuilder createHoveredButton(int x, int y, boolean integratedTexture) {
        var builder = HoverButton.builder(integratedTexture);
        builder.pos(this.posX + x, this.posY + y);
        if (integratedTexture)
            builder.sizeGui(this.textureWidth, this.textureHeight);
        return builder;
    }

    /**
     * Create a relative tab button (still need to be build()).
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param integratedTexture Does the texture is used by the GUI too, use false if the tab texture is separated !
     * @return A valid tab builder.
     */
    public @NotNull TabButton.TabBuilder createTabButton(int x, int y, boolean integratedTexture) {
        var builder = TabButton.builder(integratedTexture);
        builder.pos(this.posX + x, this.posY + y);
        if (integratedTexture)
            builder.sizeGui(this.textureWidth, this.textureHeight);
        return builder;
    }

    /**
     * Create a basic minecraft button at relative position from the gui.
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param message Name or message in the button.
     * @param onPress A callback when the button is pressed.
     * @return A valid button builder.
     */
    public @NotNull Button.Builder createButton(int x, int y, @NotNull Component message, @NotNull Button.OnPress onPress) {
        return Button.builder(message, onPress).pos(this.posX + x, this.posY + y);
    }
}
