package net.tokyosu.apocalypselib.menu.base;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

/**
 * A base menu for custom GUI, which include a player hotbar and inventory slots creation function.
 */
public abstract class MenuBase extends AbstractContainerMenu {
    protected MenuBase(@NotNull MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    protected MenuBase(@NotNull MenuType<?> menuType, int containerId, @NotNull Inventory playerInventory) {
        super(menuType, containerId);
        init(playerInventory);
    }

    /// Only called by using the second constructor (playerInventory) !
    public abstract void init(@NotNull Inventory playerInventory);

    /**
     * Create a player hotbar slots at a specific position with an offset between each slot.
     * @param inventory A valid player inventory.
     * @param posX Starting X position.
     * @param posY Starting Y position.
     */
    protected void makePlayerHotbarSlotsAt(Inventory inventory, int posX, int posY) {
        this.makePlayerHotbarSlotsAt(inventory, posX, posY, 18);
    }

    /**
     * Create a player hotbar slots at a specific position with an offset between each slot.
     * @param inventory A valid player inventory.
     * @param posX Starting X position.
     * @param posY Starting Y position.
     * @param columnOffsetX Distance of separation between each slot (first slot not included). (Default: 18)
     */
    protected void makePlayerHotbarSlotsAt(Inventory inventory, int posX, int posY, int columnOffsetX) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column,posX + (column * columnOffsetX), posY));
        }
    }

    /**
     * Create a player inventory slots at a specific position with an offset on both axis between slot.
     * @param inventory A valid player inventory.
     * @param posX Starting X position.
     * @param posY Starting Y position.
     */
    protected void makePlayerInventorySlotsAt(Inventory inventory, int posX, int posY) {
        this.makePlayerInventorySlotsAt(inventory, posX, posY, 18, 18);
    }

    /**
     * Create a player inventory slots at a specific position with an offset on both axis between slot.
     * @param inventory A valid player inventory.
     * @param posX Starting X position.
     * @param posY Starting Y position.
     * @param columnOffsetX Distance of separation between each slot (first slot not included). (Default: 18)
     * @param rowOffsetY Distance of separation between each slot (first slot not included). (Default: 18)
     */
    protected void makePlayerInventorySlotsAt(Inventory inventory, int posX, int posY, int columnOffsetX, int rowOffsetY) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory,
                                9 + column + (row * 9),
                                posX + (column * columnOffsetX),
                                posY + (row * rowOffsetY)
                        )
                );
            }
        }
    }
}
