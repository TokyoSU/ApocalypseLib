package net.tokyosu.apocalypselib.menu.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TabButton extends ImageButton {
    private final ResourceLocation normalTexture;
    private final ResourceLocation selectedTexture;
    private final OnTabPress tabPressed;
    private Rect2i normalTextureRect;
    private Rect2i selectedTextureRect;
    private boolean isSelected = false;

    public TabButton(int x, int y, int width, int height, int textureX, int textureY, @NotNull ResourceLocation texture, @NotNull OnPress onPress) {
        super(x, y, width, height, textureX, textureY, texture, onPress);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureX, int textureY, int textureYOffset, @NotNull ResourceLocation texture, @NotNull OnPress onPress) {
        super(x, y, width, height, textureX, textureY, textureYOffset, texture, onPress);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureX, int textureY, int textureYOffset, @NotNull ResourceLocation texture, int textureWidth, int textureHeight, @NotNull OnPress onPress) {
        super(x, y, width, height, textureX, textureY, textureYOffset, texture, textureWidth, textureHeight, onPress);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureX, int textureY, int textureYOffset, @NotNull ResourceLocation texture, int textureWidth, int textureHeight, @NotNull OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, textureX, textureY, textureYOffset, texture, textureWidth, textureHeight, onPress, message);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i normalTextureRect, @NotNull Rect2i selectedTextureRect, @NotNull OnTabPress onTabPress, @NotNull Component message) {
        super(x, y, width, height, normalTextureRect.getX(), normalTextureRect.getY(), textureYOffset, texture, normalTextureRect.getWidth(), normalTextureRect.getHeight(), (button) -> {}, message);
        this.normalTexture = texture;
        this.normalTextureRect = normalTextureRect;
        this.selectedTexture = null;
        this.selectedTextureRect = selectedTextureRect;
        this.tabPressed = onTabPress;
    }

    public TabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation normalTexture, @NotNull Rect2i normalTextureRect, @NotNull ResourceLocation selectedTexture, @NotNull Rect2i selectedTextureRect, @NotNull OnTabPress onTabPress, @NotNull Component message) {
        super(x, y, width, height, normalTextureRect.getX(), normalTextureRect.getY(), textureYOffset, normalTexture, normalTextureRect.getWidth(), normalTextureRect.getHeight(), (button) -> {}, message);
        this.normalTexture = normalTexture;
        this.normalTextureRect = normalTextureRect;
        this.selectedTexture = selectedTexture;
        this.selectedTextureRect = selectedTextureRect;
        this.tabPressed = onTabPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        if (this.tabPressed != null && !this.isSelected) {
            this.tabPressed.onPress(this);
            this.isSelected = true;
        }
    }

    /// Reset selection of the tab, now able to call onPress() again !
    public void reset() {
        this.isSelected = false;
    }

    @Override
    public void renderTexture(@NotNull GuiGraphics pGraphics, @NotNull ResourceLocation texture, int x, int y, int textureX, int textureY, int textureYDiff, int width, int height, int textureWidth, int textureHeight) {
        if (this.active && this.isSelected)
            super.renderTexture(pGraphics, selectedTexture != null ? selectedTexture : normalTexture, x, y, selectedTextureRect.getX(), selectedTextureRect.getY(), textureYDiff, width, height, selectedTextureRect.getWidth(), selectedTextureRect.getHeight());
        else
            super.renderTexture(pGraphics, normalTexture, x, y, normalTextureRect.getX(), normalTextureRect.getY(), textureYDiff, width, height, normalTextureRect.getWidth(), normalTextureRect.getHeight());
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnTabPress {
        void onPress(TabButton button);
    }

    /**
     * Created a tab button using factory.
     */
    public static TabBuilder builder() {
        return new TabBuilder();
    }

    /**
     * Helper to create tab button.
     */
    public static class TabBuilder {
        private @Nullable ResourceLocation normalTexture = null;
        private @Nullable ResourceLocation selectedTexture = null;
        private @Nullable Component message = null;
        private OnTabPress tabPressed;
        private Rect2i normalTextureRect;
        private Rect2i selectedTextureRect;
        private int x, y, width, height, textureYOffset;

        public TabBuilder() {}

        /**
         * Set the position of the tab button.
         */
        public TabBuilder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        /**
         * Size of the tab button.
         */
        public TabBuilder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Offset the tab button.
         */
        public TabBuilder offset(int y) {
            this.textureYOffset = y;
            return this;
        }

        /**
         * Tab texture, use normal texture or both if bounds() is defined.
         * Use bounds() to set up the texture position.
         * @param normal A valid ResourceLocation.
         */
        public TabBuilder texture(@NotNull ResourceLocation normal) {
            this.normalTexture = normal;
            this.selectedTexture = null;
            return this;
        }

        /**
         * Tab texture, use split normal and selected texture.
         * Use bounds() to set up the texture position.
         * @param normal A valid ResourceLocation.
         * @param selected A valid ResourceLocation.
         */
        public TabBuilder texture(@NotNull ResourceLocation normal, @NotNull ResourceLocation selected) {
            this.normalTexture = normal;
            this.selectedTexture = selected;
            return this;
        }

        /**
         * Tab texture position in their ResourceLocation.
         * @param normal A valid texture position.
         */
        public TabBuilder bounds(@NotNull Rect2i normal) {
            this.normalTextureRect = normal;
            return this;
        }

        /**
         * Set up a message on the tab button. (Can be ignored if icon is used)
         */
        public TabBuilder message(@NotNull Component message) {
            this.message = message;
            return this;
        }

        /**
         * Tab texture position in their ResourceLocation.
         * @param normal A valid texture position.
         * @param selected A valid texture position.
         */
        public TabBuilder bounds(@NotNull Rect2i normal, @NotNull Rect2i selected) {
            this.normalTextureRect = normal;
            this.selectedTextureRect = selected;
            return this;
        }

        /**
         * When tab is pressed, callback is called.
         * @param onPress A valid callback.
         */
        public TabBuilder onPressed(@NotNull OnTabPress onPress) {
            this.tabPressed = onPress;
            return this;
        }

        /**
         * Now build the button.
         * @return A valid tab button.
         */
        public @NotNull TabButton build() {
            return new TabButton(this.x,
                    this.y,
                    this.width,
                    this.height,
                    this.textureYOffset,
                    this.normalTexture,
                    this.normalTextureRect,
                    this.selectedTexture,
                    this.selectedTextureRect,
                    this.tabPressed,
                    this.message);
        }
    }
}
