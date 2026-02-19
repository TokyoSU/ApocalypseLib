package net.tokyosu.apocalypselib.menu.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.tokyosu.apocalypselib.ApocalypseLib;
import net.tokyosu.apocalypselib.builder.InventoryBuilder;
import net.tokyosu.apocalypselib.menu.button.ModTabButton;
import net.tokyosu.apocalypselib.menu.button.TabButton;
import net.tokyosu.apocalypselib.menu.slot.SlotCreativePanel;
import net.tokyosu.apocalypselib.tab.ModTabCollector;
import net.tokyosu.apocalypselib.tab.TabCollector;
import net.tokyosu.apocalypselib.utils.HudUtils;
import net.tokyosu.apocalypselib.utils.ModUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Allow to draw a creative menu above another menu.
 * Be sure to call required function !
 */
@SuppressWarnings("unused")
public class CreativePanel {
    private static final ResourceLocation EDITOR_TEXTURE = ResourceLocation.fromNamespaceAndPath(ApocalypseLib.MOD_ID, "textures/gui/editor.png"); // 256x256
    private static final ResourceLocation EDITOR_SCROLL_TEXTURE = ResourceLocation.fromNamespaceAndPath(ApocalypseLib.MOD_ID, "textures/gui/editor_scrollbar.png"); // 12x15
    private static final Rect2i EDITOR_RECT = new Rect2i(195, 113, 256, 256); // Width, Height, TextureWidth, TextureHeight (fixed size cause of texture).
    private static final int MAX_TAB_IN_PAGE = 7;
    private final List<ModTabButton> pTabButtonList = new ArrayList<>();
    private final InventoryBuilder baseGUI;
    private final ScrollableGrid scrollableGrid;
    private Font font;
    private EditBox searchBox;
    private Button nextPageButton;
    private Button previousPageButton;
    private int modCount = 0;
    private int currentTabPage = 0;

    public CreativePanel(@NotNull SimpleContainer container) {
        this.baseGUI = new InventoryBuilder(EDITOR_TEXTURE, EDITOR_RECT.getX(), EDITOR_RECT.getY(), EDITOR_RECT.getWidth(), EDITOR_RECT.getHeight());
        this.scrollableGrid = new ScrollableGrid(container);
    }

    /**
     * Initialize creative menu, use Minecraft.getInstance().font.
     * @param screenWidth Use this.width in your screen class.
     * @param screenHeight Use this.height in your screen class.
     * @param maxSearchLength Max character for the search bar (default 50)
     */
    public void init(int screenWidth, int screenHeight, int maxSearchLength) {
        this.init(Minecraft.getInstance().font, screenWidth, screenHeight, maxSearchLength);
    }

    /**
     * Called when the search bar is being used. (each time a character is added !)
     * @param searchFilter A valid search filter, if empty, reset the scrollable grid search too.
     */
    private void onSearchChangedCallback(@NotNull String searchFilter) {
        if (searchFilter.isEmpty()) {
            this.scrollableGrid.resetSearch();
            return;
        }
        this.scrollableGrid.setSearchFilter(searchFilter);
    }

    /**
     * Initialize creative menu.
     * @param screenWidth Use this.width in your screen class.
     * @param screenHeight Use this.height in your screen class.
     */
    public void init(@NotNull Font font, int screenWidth, int screenHeight, int maxSearchLength) {
        TabCollector.collectAllTabs();
        ModTabCollector.collectAllModTabs();
        this.modCount = ModUtils.getModCount();

        this.font = font;
        this.baseGUI.setFont(font);
        this.baseGUI.init(screenWidth, screenHeight);

        // Initialize scrollable grid.
        this.scrollableGrid.setItemList(TabCollector.TAB_ITEMS);
        this.scrollableGrid.setScrollTexture(EDITOR_SCROLL_TEXTURE);
        this.scrollableGrid.setScrollPos(this.baseGUI.getPosX() + 175, this.baseGUI.getPosY() + 18);
        this.scrollableGrid.setTabIdentifier("minecraft", ModUtils.getModName("minecraft"));

        // Initialize search.
        this.searchBox = new EditBox(this.font, this.baseGUI.getPosX() + 99, this.baseGUI.getPosY() + 6, 88, 10, Component.literal("Search"));
        this.searchBox.setMaxLength(maxSearchLength);
        this.searchBox.setBordered(false);
        this.searchBox.setTextColor(0xFFFFFF);
        this.searchBox.setResponder(this::onSearchChangedCallback);

        this.makeTabs();
    }

    /**
     * Create inventory slots for the menu, use with menu.addSlots().
     * @return A valid slots list for CreativeMenu.
     */
    public @NotNull List<Slot> createSlots(@NotNull SimpleContainer container) {
        List<Slot> slots = new ArrayList<>();
        int columnCount = scrollableGrid.getColumnCount();
        int slotSize = scrollableGrid.getSlotSize();
        for (int rowId = 0; rowId < scrollableGrid.getRowsCount(); rowId++) {
            for (int columnId = 0; columnId < columnCount; columnId++) {
                int slotId = (rowId * columnCount) + columnId;
                int x = 9 + columnId * slotSize;
                int y = 18 + rowId * slotSize;
                slots.add(new SlotCreativePanel(container, slotId, x, y));
            }
        }
        return slots;
    }

    public void tick() {
        if (this.searchBox != null) {
            this.searchBox.tick();  // Required for cursor blinking and updates.
        }
        if (this.scrollableGrid != null) {
            this.scrollableGrid.tick(); // Required for refreshing the list each time it's receiving this.dirty = true.
        }
    }

    /**
     * Render the GUI, be sure to call this.renderTooltip() after this one !
     */
    public void render(@NotNull GuiGraphics pGui, int pMouseX, int pMouseY, float pPartialTick) {
        // Render the scroll bar.
        this.scrollableGrid.renderScrollbar(pGui);

        // Render tabs.
        if (!this.pTabButtonList.isEmpty()) {
            for (int tabId = 0; tabId < this.pTabButtonList.size(); tabId++) {
                int modIndex = (this.currentTabPage * MAX_TAB_IN_PAGE) + tabId;
                if (modIndex >= this.modCount) break;
                var tab = this.pTabButtonList.get(tabId);
                if (tab.isActive()) {
                    tab.render(pGui, pMouseX, pMouseY, pPartialTick);
                    var info = tab.getModInfo();
                    if (info != null) {
                        var stack = info.iconItem();
                        if (stack != null && !stack.isEmpty() && HudUtils.isMouseHoverRect(tab.getX() + 5, tab.getY() + 7, pMouseX, pMouseY, 16)) {
                            pGui.renderTooltip(this.font, Component.literal(info.displayName()), pMouseX, pMouseY);
                        }
                    }
                }
            }
        }

        if (this.previousPageButton != null && this.previousPageButton.isActive())
            this.previousPageButton.render(pGui, pMouseX, pMouseY, pPartialTick);
        if (this.nextPageButton != null && this.nextPageButton.isActive())
            this.nextPageButton.render(pGui, pMouseX, pMouseY, pPartialTick);
    }

    /**
     * Render the GUI background, be sure to call this.renderBackground before this one !
     */
    public void renderBg(@NotNull GuiGraphics pGui, int pMouseX, int pMouseY, float pPartialTick) {
        this.baseGUI.setGraphics(pGui);
        this.baseGUI.drawBackground(0, 0, 0, 0);
    }

    /**
     * Callback for mouseClicked event.
     * @param event Use 'this' for the event.
     */
    public boolean mouseClicked(@NotNull AbstractContainerEventHandler event, double x, double y, int type) {
        // Check search box FIRST before other interactions
        if (this.searchBox != null && this.searchBox.mouseClicked(x, y, type)) {
            event.setFocused(this.searchBox);
            return true;
        }

        if (this.previousPageButton.isActive() && this.previousPageButton.mouseClicked(x, y, type))
            return true;
        if (this.nextPageButton.isActive() && this.nextPageButton.mouseClicked(x, y, type))
            return true;

        // Check for tab click
        if (!this.pTabButtonList.isEmpty()) {
            for (var tab : this.pTabButtonList) {
                if (tab.isActive() && tab.mouseClicked(x, y, type))
                    return true;
            }
        }

        // Clicking outside the search box should unfocus it
        if (this.searchBox != null)
            this.searchBox.setFocused(false);

        return this.scrollableGrid.mouseClicked(x, y, type);
    }

    public boolean isMouseOver(double x, double y) {
        if (!this.pTabButtonList.isEmpty()) {
            for (var tab : this.pTabButtonList) {
                if (tab.isActive() && tab.isMouseOver(x, y))
                    return true;
            }
        }

        if (this.nextPageButton.isMouseOver(x, y)) {
            return true;
        }
        if (this.previousPageButton.isMouseOver(x, y)) {
            return true;
        }
        return this.searchBox.isMouseOver(x, y);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox.isFocused()) {
            return this.searchBox.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (this.searchBox.isFocused()) {
            return this.searchBox.charTyped(codePoint, modifiers);
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.scrollableGrid.mouseScrolled(delta);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.scrollableGrid.mouseDragged(mouseY);
    }

    public void mouseReleased() {
        this.scrollableGrid.mouseReleased();
    }

    private void resetWithout(@NotNull TabButton button) {
        if (this.pTabButtonList.isEmpty()) return;
        this.pTabButtonList.forEach((tab) -> {
            if (tab != button)
                tab.unselect();
        });
    }

    private void onTabButtonPressed(@NotNull TabButton button, int modIndex) {
        List<ModTabCollector.ModTabInfo> allMods = new ArrayList<>(ModTabCollector.getModTabs().values());
        if (modIndex >= allMods.size()) return;
        var modInfo = allMods.get(modIndex);
        this.resetWithout(button);
        this.searchBox.setValue("");
        this.scrollableGrid.resetSearch();
        this.scrollableGrid.setTabIdentifier(modInfo.namespace(), modInfo.displayName());
    }

    private void makeTabs() {
        this.pTabButtonList.clear();

        // Get all mod tabs
        List<ModTabCollector.ModTabInfo> allMods = new ArrayList<>(ModTabCollector.getModTabs().values());
        int totalMods = allMods.size();
        int totalPages = (int)Math.ceil((double) totalMods / MAX_TAB_IN_PAGE);

        // Calculate range for current page
        int startIndex = this.currentTabPage * MAX_TAB_IN_PAGE;
        int baseX = this.baseGUI.getPosX() + 6;
        int baseY = this.baseGUI.getPosY() - 28;

        // Create tabs for current page
        for (int tabId = 0; tabId < MAX_TAB_IN_PAGE; tabId++) {
            int modIndex = startIndex + tabId;
            var tab = new ModTabButton(
                    baseX + (tabId * 26),
                    baseY,
                    26, 29, // tab size.
                    0,
                    EDITOR_TEXTURE,
                    new Rect2i(196, 0, 256, 256),
                    new Rect2i(196, 29, 256, 256),
                    (e) -> onTabButtonPressed(e, modIndex),
                    Component.empty());

            // Disable if no mod for this slot
            if (modIndex >= totalMods) {
                tab.active = false;
                tab.visible = false;
            } else {
                tab.setModInfo(allMods.get(modIndex));
            }

            // Auto-select first tab on first page
            if (tabId == 0 && this.currentTabPage == 0) {
                tab.onPress();
            }

            this.pTabButtonList.add(tab);
        }

        // Create/update pagination buttons
        this.updatePaginationButtons(totalPages);
    }

    private void updatePaginationButtons(int totalPages) {
        int paginationY = this.baseGUI.getPosY() - 24;
        int leftX = this.baseGUI.getPosX() - 20;
        int rightX = this.baseGUI.getPosX() + 195 + 5;

        // Previous page button
        this.previousPageButton = Button.builder(Component.literal("<"), btn -> {
                            if (this.currentTabPage > 0) {
                                this.currentTabPage--;
                                this.makeTabs();
                                this.scrollableGrid.resetSearch();
                                this.scrollableGrid.setDirty();
                            }
                        })
                        .bounds(leftX, paginationY, 15, 20)
                        .build();
        this.previousPageButton.active = this.currentTabPage > 0;

        // Next page button
        this.nextPageButton = Button.builder(Component.literal(">"), btn -> {
                            if (this.currentTabPage < totalPages - 1) {
                                this.currentTabPage++;
                                this.makeTabs();
                                this.scrollableGrid.resetSearch();
                                this.scrollableGrid.setDirty();
                            }
                        })
                        .bounds(rightX, paginationY, 15, 20)
                        .build();
        this.nextPageButton.active = this.currentTabPage < totalPages - 1;
    }

    /**
     * Does the GUI is still valid ?
     * @return Always true, we don't need to check distance from block or anything else.
     */
    public boolean stillValid() {
        return true;
    }

    public int getX() {
        return this.baseGUI.getPosX();
    }

    public int getY() {
        return this.baseGUI.getPosY();
    }

    public int getWidth() {
        return EDITOR_RECT.getX();
    }

    public int getHeight() {
        return EDITOR_RECT.getY();
    }
}
