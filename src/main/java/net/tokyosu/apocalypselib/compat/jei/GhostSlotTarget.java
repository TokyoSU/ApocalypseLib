package net.tokyosu.apocalypselib.compat.jei;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;

/** Optional JEI target for an ItemStack-only ghost slot. Other ingredient types are ignored. */
public record GhostSlotTarget<I>(Rect2i area, Consumer<ItemStack> setter)
        implements IGhostIngredientHandler.Target<I> {
    @Override
    public Rect2i getArea() { return area; }

    @Override
    public void accept(I ingredient) {
        if (ingredient instanceof ItemStack stack) setter.accept(stack);
    }
}
