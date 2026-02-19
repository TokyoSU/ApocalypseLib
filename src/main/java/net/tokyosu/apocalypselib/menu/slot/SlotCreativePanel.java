package net.tokyosu.apocalypselib.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotCreativePanel extends Slot {
    public SlotCreativePanel(@NotNull Container container, int p_40224_, int p_40225_, int p_40226_) {
        super(container, p_40224_, p_40225_, p_40226_);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return false;
    }
}
