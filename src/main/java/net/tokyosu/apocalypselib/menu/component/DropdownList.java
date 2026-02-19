package net.tokyosu.apocalypselib.menu.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.tokyosu.apocalypselib.ApocalypseLib;
import net.tokyosu.apocalypselib.menu.button.HoverButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Make a dropdown list using texture as base.
 * @param <T> A structure or class that will be used as value to draw, use the displayFunction argument to draw it correctly.
 */
@SuppressWarnings({"SpellCheckingInspection", "FieldCanBeLocal", "unused"})
public class DropdownList<T> {
    private static final ResourceLocation DROP_DOWN_TEXTURE = ResourceLocation.fromNamespaceAndPath(ApocalypseLib.MOD_ID, "textures/gui/dropdown.png");
    private final ResourceLocation texture;
    private final Rect2i posAndSize;
    private final Rect2i clippedArea; // The area where the values can scroll (outside will not show).
    private final Rect2i mainArea; // The main area (drop bar + button)
    private final Rect2i scrollArea; // Where the scroll button should remain (min and max value)
    private final Rect2i textArea; // Where the selected value (string?) will remain after and at init.
    private final Rect2i scrollTex; // The scroll bar button that move think up/down.
    private final Rect2i dropbar; // The drop bar (A whole button).
    private final Consumer<T> onSelect; // Callback when a value is selected.
    private final Consumer<Tuple<Rect2i, T>> onSelectHovered;
    private final HoverButton dropbarDownBtn;
    private final HoverButton dropbarUpBtn;
    private final Function<T, Component> displayFunction;
    private final int textureWidth; // The whole texture size.
    private final int textureHeight;
    private final int itemHeight; // Height of each value.
    private boolean isDraggingScroll = false;
    private double dragStartOffset = 0.0;
    private boolean expanded = false; // Is drop bar selected ? if true, open the dropdown.
    private int scrollOffset = 0;
    private T hoveredItem = null;
    private List<T> values; // All values to show if dropbar is selected.
    private T selected; // The value selected if any.
    private boolean isClosing;

    public DropdownList(@NotNull Rect2i posAndSize,
                        @NotNull Rect2i dropbar,
                        @NotNull Rect2i dropbarHover,
                        @NotNull Rect2i dropbarup,
                        @NotNull Rect2i dropbarupHover,
                        @NotNull Rect2i mainArea,
                        @NotNull Rect2i scrollArea,
                        @NotNull Rect2i scrollTex,
                        @NotNull Rect2i textArea,
                        @NotNull Rect2i clippedArea,
                        @Nullable ResourceLocation texture,
                        int textureWidth, int textureHeight,
                        int itemHeight,
                        @NotNull Function<T, Component> displayFunction,
                        @NotNull Consumer<T> onSelect) {
        this(posAndSize, dropbar, dropbarHover, dropbarup, dropbarupHover, mainArea, scrollArea, scrollTex, textArea, clippedArea, texture, textureWidth, textureHeight, itemHeight, displayFunction, onSelect, null);
    }

    public DropdownList(@NotNull Rect2i posAndSize,
                        @NotNull Rect2i dropbar,
                        @NotNull Rect2i dropbarHover,
                        @NotNull Rect2i dropbarup,
                        @NotNull Rect2i dropbarupHover,
                        @NotNull Rect2i mainArea,
                        @NotNull Rect2i scrollArea,
                        @NotNull Rect2i scrollTex,
                        @NotNull Rect2i textArea,
                        @NotNull Rect2i clippedArea,
                        @Nullable ResourceLocation texture,
                        int textureWidth, int textureHeight,
                        int itemHeight,
                        @NotNull Function<T, Component> displayFunction,
                        @NotNull Consumer<T> onSelect,
                        @Nullable Consumer<Tuple<Rect2i, T>> onSelectHovered) {
        this.posAndSize = posAndSize;
        this.texture = texture != null ? texture : DROP_DOWN_TEXTURE;
        this.onSelect = onSelect;
        this.clippedArea = clippedArea;
        this.mainArea = mainArea;
        this.scrollArea = scrollArea;
        this.textArea = textArea;
        this.scrollTex = scrollTex;
        this.dropbar = dropbar;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.itemHeight = itemHeight;
        this.displayFunction = displayFunction;
        this.onSelectHovered = onSelectHovered;

        int x = posAndSize.getX();
        int y = posAndSize.getY();
        this.dropbarDownBtn = HoverButton.builder(true).sizeGui(this.textureWidth, this.textureHeight).pos(x, y).size(dropbar.getWidth(), dropbar.getHeight()).bounds(dropbar, dropbarHover).texture(this.texture).onHoverPress(this::onDropBarDownSelected).build();
        this.dropbarUpBtn = HoverButton.builder(true).sizeGui(this.textureWidth, this.textureHeight).pos(x, y).size(dropbarup.getWidth(), dropbarup.getHeight()).bounds(dropbarup, dropbarupHover).texture(this.texture).onHoverPress(this::onDropBarUpSelected).build();
        this.dropbarDownBtn.active = true;
        this.dropbarUpBtn.active = false;
        this.isClosing = false;
    }

    private void onDropBarUpSelected(@NotNull HoverButton hoverButton) {
        this.dropbarDownBtn.active = true;
        this.dropbarUpBtn.active = false;
        this.expanded = false;
    }

    private void onDropBarDownSelected(@NotNull HoverButton button) {
        this.dropbarUpBtn.active = true;
        this.dropbarDownBtn.active = false;
        this.expanded = true;
    }

    public void setClosing(boolean value) {
        this.isClosing = value;
    }

    public boolean isClosing() {
        return this.isClosing;
    }

    public void setValues(@NotNull List<T> values) {
        this.values = values;
    }

    public void setSelected(@NotNull T selected) {
        this.selected = selected;
    }

    public @NotNull T getSelected() {
        return this.selected;
    }

    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int posX = this.posAndSize.getX();
        int posY = this.posAndSize.getY();

        // Show dropbar button.
        if (this.dropbarDownBtn.isActive()) {
            this.dropbarDownBtn.render(graphics, mouseX, mouseY, partialTick);
        }
        if (this.dropbarUpBtn.isActive()) {
            this.dropbarUpBtn.render(graphics, mouseX, mouseY, partialTick);
        }

        // Show the selected value on the text panel
        if (this.selected != null) {
            var textComp = this.displayFunction.apply(this.selected);
            if (textComp != null) {
                graphics.drawString(Minecraft.getInstance().font, textComp, posX + this.textArea.getX() + 4, posY + this.textArea.getY() + 4, 0xFFFFFF);
            }
        }

        // Only show main texture if expended is true !
        // Draw dropdown if expanded
        if (this.expanded) {
            this.renderDropdown(graphics, posX, posY, mouseX, mouseY);
        }
    }

    private void renderDropdown(@NotNull GuiGraphics graphics, int posX, int posY, int mouseX, int mouseY) {
        // Draw dropdown texture.
        int dropDownY = posY + this.dropbar.getHeight();
        this.drawTexture(graphics, posX, dropDownY, this.mainArea);

        // Now show the values stored if any
        this.enableScissor(graphics, posX, dropDownY, this.clippedArea);
        if (this.values != null && !this.values.isEmpty()) {
            int itemStartY = dropDownY + this.clippedArea.getY() - scrollOffset;
            int clipLeft = posX + this.clippedArea.getX();
            int clipTop = dropDownY + this.clippedArea.getY();
            int clipBottom = clipTop + this.clippedArea.getHeight();

            this.hoveredItem = null;

            for (int i = 0; i < this.values.size(); i++) {
                T value = this.values.get(i);
                int itemY = itemStartY + (i * this.itemHeight);

                // Only render if visible in clipped area
                if (itemY + this.itemHeight >= clipTop && itemY < clipBottom) {

                    // Check if this item is hovered
                    var isHovered = mouseX >= clipLeft && mouseX <= clipLeft + this.clippedArea.getWidth() &&
                            mouseY >= itemY && mouseY < itemY + this.itemHeight &&
                            mouseY >= clipTop && mouseY < clipBottom;
                    var hoveredRect = new Rect2i(clipLeft, itemY, clipLeft + this.clippedArea.getWidth(), itemY + this.itemHeight);

                    if (isHovered) {
                        this.hoveredItem = value;
                        if (this.onSelectHovered != null)
                            this.onSelectHovered.accept(new Tuple<>(hoveredRect, this.hoveredItem));
                    }

                    // Draw background for hovered item
                    if (isHovered) {
                        graphics.fill(hoveredRect.getX(), hoveredRect.getY(), hoveredRect.getWidth(), hoveredRect.getHeight(), 0x80FFFFFF);
                    }

                    // Draw background for selected item
                    if (value.equals(this.selected)) {
                        graphics.fill(hoveredRect.getX(), hoveredRect.getY(), hoveredRect.getWidth(), hoveredRect.getHeight(), 0x60FFFF00);
                    }

                    // Draw item text
                    Component text = this.displayFunction.apply(value);
                    graphics.drawString(Minecraft.getInstance().font, text, clipLeft + 4, itemY + (this.itemHeight / 2) - 4, 0xFFFFFF);
                }
            }
        }
        this.disableScissor(graphics);

        // Draw scrollbar if needed
        if (this.values != null && this.values.size() * this.itemHeight > this.clippedArea.getHeight()) {
            this.renderScrollbar(graphics, posX, dropDownY);
        }
    }

    private void renderScrollbar(@NotNull GuiGraphics graphics, int posX, int posY) {
        int scrollbarX = posX + this.scrollArea.getX();
        int scrollbarTopY = posY + this.scrollArea.getY();  // Top of scroll track
        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            int trackHeight = this.scrollArea.getHeight();
            int thumbHeight = this.scrollTex.getHeight();
            int scrollableArea = trackHeight - thumbHeight;

            // Calculate thumb position (ensure it goes from 0 to scrollableArea)
            float progress = Mth.clamp((float) scrollOffset / maxScroll, 0.0f, 1.0f);
            int thumbY = scrollbarTopY + Math.round(scrollableArea * progress);

            // Draw scroll thumb
            this.drawTexture(graphics, scrollbarX, thumbY, this.scrollTex);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int type) {
        if (type != 0) return false;

        // Handle dropdown buttons
        if (this.dropbarDownBtn.isActive() && this.dropbarDownBtn.mouseClicked(mouseX, mouseY, type)) {
            return true;
        }
        if (this.dropbarUpBtn.isActive() && this.dropbarUpBtn.mouseClicked(mouseX, mouseY, type)) {
            return true;
        }

        // Handle item selection in expanded state
        if (this.expanded) {
            int dropDownY = this.posAndSize.getY() + this.dropbar.getHeight();
            int clipLeft = this.posAndSize.getX() + this.clippedArea.getX();
            int clipTop = dropDownY + this.clippedArea.getY();
            int clipRight = clipLeft + this.clippedArea.getWidth();
            int clipBottom = clipTop + this.clippedArea.getHeight();

            // Click on item
            if (mouseX >= clipLeft && mouseX <= clipRight && mouseY >= clipTop && mouseY < clipBottom) {
                if (this.hoveredItem != null) {
                    this.selected = this.hoveredItem;
                    this.onSelect.accept(this.selected);
                    this.expanded = false;
                    this.isClosing = true;
                    this.dropbarUpBtn.active = false;
                    this.dropbarDownBtn.active = true;
                    return true;
                }
            }

            // Click on scrollbar
            if (this.isScrollThumbHovering(mouseX, mouseY)) {
                this.isDraggingScroll = true;
                this.dragStartOffset = getDragStartOffset(mouseY);
                return true;
            }

            // Click outside to close
            if (!this.isHovering(mouseX, mouseY)) {
                this.expanded = false;
                this.isClosing = true;
                this.dropbarUpBtn.active = false;
                this.dropbarDownBtn.active = true;
                return true;
            }
        }

        return false;
    }

    private double getDragStartOffset(double mouseY) {
        // Calculate current thumb position
        int trackHeight = this.scrollArea.getHeight();
        int thumbHeight = this.scrollTex.getHeight();
        int scrollableArea = trackHeight - thumbHeight;

        int maxScroll = this.getMaxScroll();
        float progress = maxScroll > 0 ? (float) scrollOffset / maxScroll : 0.0f;
        int currentThumbY = getScrollBarTop() + Math.round(scrollableArea * progress);

        // Store offset from thumb top to mouse Y
        return mouseY - currentThumbY;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.expanded && this.values != null) {
            int maxScroll = this.getMaxScroll();
            if (maxScroll > 0) {
                // Scroll by one item height per wheel tick
                int scrollAmount = (int) (delta * this.itemHeight);
                this.scrollOffset -= scrollAmount;

                // Clamp to valid range [0, maxScroll]
                this.scrollOffset = Mth.clamp(this.scrollOffset, 0, maxScroll);

                return true;
            }
        }
        return false;
    }

    public boolean mouseDragged(double mouseY) {
        if (this.isDraggingScroll && this.expanded) {
            this.updateScrollFromMouse(mouseY);
            return true;
        }
        return false;
    }

    public void mouseReleased() {
        this.isDraggingScroll = false;
        this.dragStartOffset = 0.0;
    }

    private void updateScrollFromMouse(double mouseY) {
        int trackHeight = this.scrollArea.getHeight();
        int thumbHeight = this.scrollTex.getHeight();
        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            double percent = this.getScrollPercent(mouseY, trackHeight, thumbHeight);

            // Update scroll offset
            this.scrollOffset = (int) Math.round(percent * maxScroll);
            this.scrollOffset = Mth.clamp(this.scrollOffset, 0, maxScroll);
        }
    }

    private int getMaxScroll() {
        int totalContentHeight = this.values.size() * this.itemHeight;
        return Math.max(0, totalContentHeight - this.clippedArea.getHeight());
    }

    private double getScrollPercent(double mouseY, int trackHeight, int thumbHeight) {
        int scrollableArea = trackHeight - thumbHeight;

        // Apply the drag offset to get the thumb's top position
        double thumbTopY = mouseY - this.dragStartOffset;

        // Calculate how far the thumb has moved from the top
        double relative = thumbTopY - this.getScrollBarTop();

        // Clamp to valid range [0, scrollableArea]
        relative = Mth.clamp(relative, 0.0, scrollableArea);

        // Calculate percentage
        double percent = scrollableArea > 0 ? relative / scrollableArea : 0.0;
        percent = Mth.clamp(percent, 0.0, 1.0);
        return percent;
    }

    public int getX() {
        return this.posAndSize.getX();
    }

    public int getY() {
        return this.posAndSize.getY();
    }

    public int getWidth() {
        return this.posAndSize.getWidth();
    }

    public int getHeight() {
        return this.posAndSize.getHeight();
    }

    public boolean isHovering(double mouseX, double mouseY) {
        int x = getX();
        int y = getY();
        if (this.expanded) {
            return mouseX >= x && mouseX <= x + this.posAndSize.getWidth() && mouseY >= y && mouseY <= y + this.posAndSize.getHeight();
        }
        return mouseX >= x && mouseX <= x + this.dropbar.getWidth() && mouseY >= y && mouseY <= y + this.dropbar.getHeight();
    }

    public boolean isScrollHovering(double mouseX, double mouseY) {
        int x = getX() + this.scrollArea.getX();
        int y = getY() + this.dropbar.getHeight() + this.scrollArea.getY();
        return mouseX >= x && mouseX <= x + this.scrollArea.getWidth() && mouseY >= y && mouseY <= y + this.scrollArea.getHeight();
    }

    private int getScrollBarTop() {
        int dropDownY = this.posAndSize.getY() + this.dropbar.getHeight();
        return dropDownY + this.scrollArea.getY();
    }

    public boolean isScrollThumbHovering(double mouseX, double mouseY) {
        if (!this.expanded || this.values == null) return false;

        int scrollbarX = this.posAndSize.getX() + this.scrollArea.getX();
        int scrollbarTopY = getScrollBarTop();
        int trackHeight = this.scrollArea.getHeight();
        int thumbHeight = this.scrollTex.getHeight();

        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            int scrollableArea = trackHeight - thumbHeight;
            float progress = (float) scrollOffset / maxScroll;
            int thumbY = scrollbarTopY + Math.round(scrollableArea * progress);

            // Check if mouse is over the thumb button
            return mouseX >= scrollbarX &&
                    mouseX <= scrollbarX + this.scrollTex.getWidth() &&
                    mouseY >= thumbY &&
                    mouseY <= thumbY + thumbHeight;
        }

        return false;
    }

    private void drawTexture(@NotNull GuiGraphics graphics, int x, int y, int texx, int texy, int texwidth, int texheight) {
        graphics.blit(this.texture, x, y, texx, texy, texwidth, texheight, this.textureWidth, this.textureHeight);
    }

    private void drawTexture(@NotNull GuiGraphics graphics, int x, int y, @NotNull Rect2i texarea) {
        graphics.blit(this.texture, x, y, texarea.getX(), texarea.getY(), texarea.getWidth(), texarea.getHeight(), this.textureWidth, this.textureHeight);
    }

    private void enableScissor(@NotNull GuiGraphics graphics, int baseX, int baseY, @NotNull Rect2i relativeArea) {
        graphics.enableScissor(
                baseX + relativeArea.getX(),
                baseY + relativeArea.getY(),
                baseX + relativeArea.getX() + relativeArea.getWidth(),
                baseY + relativeArea.getY() + relativeArea.getHeight()
        );
    }

    private void disableScissor(@NotNull GuiGraphics graphics) {
        graphics.disableScissor();
    }
}
