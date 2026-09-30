package net.tokyosu.apocalypselib.compat.jei;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import java.util.List;
import java.util.function.Function;

/** Optional JEI adapter. Instantiate only from a JEI plugin. */
public final class GuiContainerHandler<T extends AbstractContainerScreen<?>> implements IGuiContainerHandler<T> {
    private final Function<T, List<Rect2i>> areas;

    public GuiContainerHandler(Function<T, List<Rect2i>> areas) { this.areas = areas; }

    @Override
    public List<Rect2i> getGuiExtraAreas(T screen) { return areas.apply(screen); }
}
