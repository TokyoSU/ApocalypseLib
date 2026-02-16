package net.tokyosu.apocalypselib.menu.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.apocalypselib.ApocalypseLib;
import net.tokyosu.apocalypselib.utils.SearchUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Helper to create a scrollable item grid.
 * Be sure to call each function correctly.
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class ScrollableGrid {
    public static final int COLUMNS = 9;
    public static final int ROWS_VISIBLE = 5;
    public static final int MAX_SLOTS = COLUMNS * ROWS_VISIBLE * 256;
    public static final int SLOT_SIZE = 18;
    public static final int KNOB_HEIGHT = 15;
    public static final int SCROLLBAR_WIDTH = 12;
    public static final int SCROLLBAR_HEIGHT = ROWS_VISIBLE * SLOT_SIZE - 2;
    private final List<ItemStack> filteredItems = new ArrayList<>();
    private final SimpleContainer container;
    private Map<String, SortedSet<ItemStack>> sortedMap;
    private String searchFilter = "";
    private ResourceLocation scrollTexture;
    private String tabIdentifier;
    private String tabName;
    private int scrollbarX;
    private int scrollbarY;
    private int scrollRow = 0;
    private boolean dragging = false;
    private boolean dirty = false;

    /**
     * Scrollable grid need a container to set and update items.
     * @param container A valid container.
     */
    public ScrollableGrid(@NotNull SimpleContainer container) {
        this.container = container;
    }

    /**
     * Set an items list mapped with a mod id.
     */
    public void setItemList(@NotNull Map<String, SortedSet<ItemStack>> items) {
        this.sortedMap = items;
    }

    /**
     * Set a search filter.
     * @param filter A valid filter.
     */
    public void setSearchFilter(@NotNull String filter) {
        this.searchFilter = filter.toLowerCase();
        this.scrollRow = 0;
        this.rebuildAdd();
    }

    public void setTabIdentifier(@NotNull String modid, @NotNull String modname) {
        this.tabIdentifier = modid;
        this.tabName = modname;
        this.rebuildAdd();
    }

    public void setDirty() {
        this.dirty = true;
    }

    public void resetSearch() {
        this.filteredItems.clear();
        this.searchFilter = "";
        this.scrollRow = 0;
        this.dirty = true;
    }

    public void setScrollTexture(@NotNull ResourceLocation scrollTexture) {
        this.scrollTexture = scrollTexture;
    }

    public void setScrollPos(int x, int y) {
        this.scrollbarX = x;
        this.scrollbarY = y;
    }

    private int getRealSlotsCounts() {
        return this.filteredItems.size();
    }

    private int getTotalRows() {
        return (int)Math.ceil(getRealSlotsCounts() / (double)COLUMNS);
    }

    private int getMaxScroll() {
        return Math.max(0, getTotalRows() - ROWS_VISIBLE);
    }

    public int getColumnCount() {
        return COLUMNS;
    }

    public int getRowsCount() {
        return ROWS_VISIBLE;
    }

    public int getSlotSize() {
        return SLOT_SIZE;
    }

    public @NotNull String getTabName() {
        return this.tabName;
    }

    public @NotNull String getTabIdentifier() {
        return this.tabIdentifier;
    }

    /**
     * Rebuild the list if anything need it, this need to be called !
     */
    public void tick() {
        if (this.dirty) {
            this.rebuildAdd();
            this.dirty = false;
        }
    }

    private void rebuildAdd() {
        if (this.sortedMap != null && !this.sortedMap.isEmpty()) {
            for(int i = 0; i < this.container.getContainerSize(); ++i) {
                this.container.setItem(i, ItemStack.EMPTY);
            }

            // Sort it to avoid items at different place in the list.
            var sortedStacks = this.sortedMap.get(this.tabIdentifier);
            if (!sortedStacks.isEmpty()) {
                this.filteredItems.clear();

                for (var stack : sortedStacks) {
                    if (this.searchFilter.isEmpty() || SearchUtils.matches(stack, this.searchFilter)) {
                        this.filteredItems.add(stack);
                    }
                }

                this.rebuild();
            } else {
                ApocalypseLib.LOGGER.error("Failed to rebuild() a ScrollableGrid, modlist is null or empty\nDid you call setItemList() or build the list correctly ?");
            }
        } else {
            ApocalypseLib.LOGGER.error("Failed to rebuild() a ScrollableGrid, modlist is null !\nBe sure to call setItemList() before doing anything !");
        }
    }

    /**
     * Rebuild the container items list and notify it that it need to rebuild for the renderer.
     */
    private void rebuild() {
        // Clear ALL slots
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            this.container.setItem(i, ItemStack.EMPTY);
        }

        int startIndex = this.scrollRow * COLUMNS;
        int endIndex = Math.min(startIndex + ROWS_VISIBLE * COLUMNS, this.filteredItems.size());

        int slotIndex = 0;
        for (int i = startIndex; i < endIndex; i++) {
            if (slotIndex < ROWS_VISIBLE * COLUMNS) {
                this.container.setItem(slotIndex++, this.filteredItems.get(i).copy());
            }
        }

        this.container.setChanged();
    }

    /* ---------------- RENDER ---------------- */

    public void renderScrollbar(@NotNull GuiGraphics g) {
        if (getMaxScroll() <= 0) return;

        int movableHeight = SCROLLBAR_HEIGHT - KNOB_HEIGHT;
        float progress = (float)scrollRow / getMaxScroll();

        g.blit(scrollTexture,
                scrollbarX,
                scrollbarY + (int)(movableHeight * progress),
                0, 0,
                12, KNOB_HEIGHT,
                12, KNOB_HEIGHT);
    }

    /* ---------------- SCROLL WHEEL ---------------- */

    public boolean mouseScrolled(double delta) {
        if (getMaxScroll() <= 0) return false;
        if (delta != 0.0) {
            scrollRow -= (int) delta;
            scrollRow = Mth.clamp(scrollRow, 0, getMaxScroll());
            this.dirty = true;
        }
        return true;
    }

    /* ---------------- DRAG SUPPORT ---------------- */

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        if (mouseX >= this.scrollbarX && mouseX <= this.scrollbarX + SCROLLBAR_WIDTH && mouseY >= this.scrollbarY && mouseY <= this.scrollbarY + SCROLLBAR_HEIGHT) {
            this.dragging = true;
            this.updateScrollFromMouse(mouseY);
            return true;
        }

        return false;
    }

    public boolean mouseDragged(double mouseY) {
        if (!this.dragging) return false;
        this.updateScrollFromMouse(mouseY);
        return true;
    }

    public void mouseReleased() {
        this.dragging = false;
    }

    private void updateScrollFromMouse(double mouseY) {
        int movableHeight = SCROLLBAR_HEIGHT - KNOB_HEIGHT;
        double relative = mouseY - scrollbarY - (KNOB_HEIGHT / 2.0);
        double percent = Mth.clamp(relative / movableHeight, 0.0, 1.0);
        this.scrollRow = (int)Math.round(percent * getMaxScroll());
        this.dirty = true;
    }
}
