package net.tokyosu.apocalypselib.menu.base;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;

/** Retains named providers while delegating title/factory behavior to Minecraft. */
public abstract class MenuProviderBase implements MenuProvider {
    private final SimpleMenuProvider delegate;

    protected MenuProviderBase(MenuConstructor constructor, Component title) {
        delegate = new SimpleMenuProvider(constructor, title);
    }

    @Override
    public Component getDisplayName() { return delegate.getDisplayName(); }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return delegate.createMenu(id, inventory, player);
    }
}
