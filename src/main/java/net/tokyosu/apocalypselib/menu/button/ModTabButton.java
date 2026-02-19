package net.tokyosu.apocalypselib.menu.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.tokyosu.apocalypselib.tab.ModTabCollector;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ModTabButton extends TabButton {
    private ModTabCollector.ModTabInfo modInfo;

    public ModTabButton(int x, int y, int width, int height, int textureX, int textureY, @NotNull ResourceLocation texture, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureX, textureY, texture, onPress);
    }

    public ModTabButton(int x, int y, int width, int height, int textureX, int textureY, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureX, textureY, textureYOffset, texture, onPress);
    }

    public ModTabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i textureRect, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureYOffset, texture, textureRect, onPress);
    }

    public ModTabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i textureRect, @NotNull Button.OnPress onPress, @NotNull ModTabCollector.ModTabInfo info) {
        super(x, y, width, height, textureYOffset, texture, textureRect, onPress);
        this.setModInfo(info);
    }

    public ModTabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i textureRect, @NotNull Button.OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, textureYOffset, texture, textureRect, onPress, message);
    }

    public ModTabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i normalTextureRect, @NotNull Rect2i selectedTextureRect, @NotNull OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, textureYOffset, texture, normalTextureRect, selectedTextureRect, onPress, message);
    }

    public ModTabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i normalTextureRect, @NotNull Rect2i selectedTextureRect, @NotNull OnPress onPress, @NotNull Component message, @NotNull ModTabCollector.ModTabInfo info) {
        super(x, y, width, height, textureYOffset, texture, normalTextureRect, selectedTextureRect, onPress, message);
        this.setModInfo(info);
    }

    public ModTabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation normalTexture, @NotNull Rect2i normalTextureRect, @NotNull ResourceLocation selectedTexture, @NotNull Rect2i selectedTextureRect, @NotNull OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, textureYOffset, normalTexture, normalTextureRect, selectedTexture, selectedTextureRect, onPress, message);
    }

    public ModTabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation normalTexture, @NotNull Rect2i normalTextureRect, @NotNull ResourceLocation selectedTexture, @NotNull Rect2i selectedTextureRect, @NotNull OnPress onPress, @NotNull Component message, @NotNull ModTabCollector.ModTabInfo info) {
        super(x, y, width, height, textureYOffset, normalTexture, normalTextureRect, selectedTexture, selectedTextureRect, onPress, message);
        this.setModInfo(info);
    }

    public void setModInfo(@NotNull ModTabCollector.ModTabInfo tabInfo) {
        this.modInfo = tabInfo;
    }

    public @NotNull ModTabCollector.ModTabInfo getModInfo() {
        return this.modInfo;
    }

    @Override
    public void renderTexture(@NotNull GuiGraphics pGraphics, @NotNull ResourceLocation texture, int x, int y, int textureX, int textureY, int textureYDiff, int width, int height, int textureWidth, int textureHeight) {
        if (modInfo != null) {
            this.setTabIconOffset(5, 7);
            this.setTabIcon(modInfo.iconItem());
        }
        super.renderTexture(pGraphics, texture, x, y, textureX, textureY, textureYDiff, width, height, textureWidth, textureHeight);
    }
}
