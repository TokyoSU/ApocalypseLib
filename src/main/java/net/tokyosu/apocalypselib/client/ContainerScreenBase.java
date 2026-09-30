package net.tokyosu.apocalypselib.client;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/** Shared label visibility for custom container screens. Event/modal policy remains in each screen. */
public abstract class ContainerScreenBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected ContainerScreenBase(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    protected final void hideLabels() {
        hideInventoryLabel();
        titleLabelX = titleLabelY = 8000;
    }

    protected final void hideInventoryLabel() {
        inventoryLabelX = inventoryLabelY = 8000;
    }
}
