package net.tokyosu.apocalypselib.menu.button;

import net.minecraft.ResourceLocationException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Define a tab button that can change it's state from normal to selected or vice versa.
 */
@SuppressWarnings("unused")
public class TabButton extends ImageButton {
    private final ResourceLocation normalTexture;
    private final ResourceLocation selectedTexture;
    private final OnPress tabPressed;
    private Rect2i normalTextureRect;
    private Rect2i selectedTextureRect;
    private boolean isSelected = false;

    public TabButton(int x, int y, int width, int height, int textureX, int textureY, @NotNull ResourceLocation texture, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureX, textureY, texture, onPress);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureX, int textureY, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureX, textureY, textureYOffset, texture, onPress);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i textureRect, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureRect.getX(), textureRect.getY(), textureYOffset, texture, textureRect.getWidth(), textureRect.getHeight(), onPress);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i textureRect, @NotNull Button.OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, textureRect.getWidth(), textureRect.getY(), textureYOffset, texture, textureRect.getWidth(), textureRect.getHeight(), onPress, message);
        this.normalTexture = texture;
        this.selectedTexture = null;
        this.tabPressed = null;
    }

    public TabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i normalTextureRect, @NotNull Rect2i selectedTextureRect, @NotNull TabButton.OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, normalTextureRect.getX(), normalTextureRect.getY(), textureYOffset, texture, normalTextureRect.getWidth(), normalTextureRect.getHeight(), (button) -> {}, message);
        this.normalTexture = texture;
        this.normalTextureRect = normalTextureRect;
        this.selectedTexture = null;
        this.selectedTextureRect = selectedTextureRect;
        this.tabPressed = onPress;
    }

    public TabButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation normalTexture, @NotNull Rect2i normalTextureRect, @NotNull ResourceLocation selectedTexture, @NotNull Rect2i selectedTextureRect, @NotNull TabButton.OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, normalTextureRect.getX(), normalTextureRect.getY(), textureYOffset, normalTexture, normalTextureRect.getWidth(), normalTextureRect.getHeight(), (button) -> {}, message);
        this.normalTexture = normalTexture;
        this.normalTextureRect = normalTextureRect;
        this.selectedTexture = selectedTexture;
        this.selectedTextureRect = selectedTextureRect;
        this.tabPressed = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        if (this.tabPressed != null && !this.isSelected) {
            this.tabPressed.onPress(this);
            this.isSelected = true;
        }
    }

    /**
     * Reset tab to unselected
     */
    public void unselect() {
        this.isSelected = false;
    }

    @Override
    public void renderTexture(@NotNull GuiGraphics pGraphics, @NotNull ResourceLocation texture, int x, int y, int textureX, int textureY, int textureYDiff, int width, int height, int textureWidth, int textureHeight) {
        final var resource = (this.selectedTexture != null && this.isSelected) ? this.selectedTexture : this.normalTexture;
        final var rectangle = (this.selectedTextureRect != null && this.isSelected) ? this.selectedTextureRect : this.normalTextureRect;
        super.renderTexture(pGraphics, resource, x, y, rectangle.getX(), rectangle.getY(), textureYDiff, width, height, rectangle.getWidth(), rectangle.getHeight());
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(TabButton button);
    }

    /**
     * Created a tab button using factory.
     * @param integratedTexture Does the texture is used by the GUI too, use false if the tab texture is separated !
     */
    public static @NotNull TabBuilder builder(boolean integratedTexture) {
        return new TabBuilder(integratedTexture);
    }

    /**
     * Helper to create tab button.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static class TabBuilder {
        private @Nullable ResourceLocation normalTexture = null;
        private @Nullable ResourceLocation selectedTexture = null;
        private @Nullable Component message = null;
        private @Nullable Rect2i normalTextureRect = null;
        private @Nullable Rect2i selectedTextureRect = null;
        private @Nullable TabButton.OnPress tabPressed = null;
        private @Nullable Button.OnPress buttonPressed = null;
        private int x = 0, y = 0, width = 0, height = 0, textureYOffset = 0;
        private int guiWidth = 0, guiHeight = 0;
        private boolean isIntegratedByGUI = false;
        private boolean hasGuiSizeBeenSet = false;

        /**
         * Constructor of TabBuilder.
         * @param integratedTexture Does the texture is used by the GUI too, use false if the tab texture is separated !
         */
        public TabBuilder(boolean integratedTexture) {
            this.isIntegratedByGUI = integratedTexture;
        }

        /**
         * Set the position of the tab button.
         */
        public @NotNull TabBuilder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        /**
         * Size of the tab button.
         */
        public @NotNull TabBuilder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Dont call it, its set by MenuBase !
         * @param width Texture width of the GUI.
         * @param height Texture height of the GUI.
         */
        public @NotNull TabBuilder sizeGui(int width, int height) {
            this.guiWidth = width;
            this.guiHeight = height;
            this.hasGuiSizeBeenSet = true;
            return this;
        }

        /**
         * Offset the tab button.
         */
        public @NotNull TabBuilder offset(int y) {
            this.textureYOffset = y;
            return this;
        }

        /**
         * Tab texture, use normal texture or both if bounds() is defined.
         * Use bounds() to set up the texture position.
         * @param normal A valid ResourceLocation.
         */
        public @NotNull TabBuilder texture(@NotNull ResourceLocation normal) {
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
        public @NotNull TabBuilder texture(@NotNull ResourceLocation normal, @NotNull ResourceLocation selected) {
            this.normalTexture = normal;
            this.selectedTexture = selected;
            return this;
        }

        /**
         * Tab texture position in their ResourceLocation.
         * @param normal A valid texture position.
         */
        public @NotNull TabBuilder bounds(@NotNull Rect2i normal) {
            this.normalTextureRect = new Rect2i(normal.getX(), normal.getY(), normal.getWidth(), normal.getHeight()); // Avoid overriding the original value !
            this.selectedTextureRect = null;
            if (this.isIntegratedByGUI && this.hasGuiSizeBeenSet) {
                this.normalTextureRect.setWidth(this.guiWidth);
                this.normalTextureRect.setHeight(this.guiHeight);
            }
            return this;
        }

        /**
         * Tab texture position in their ResourceLocation.
         * @param normal A valid texture position.
         * @param selected A valid texture position.
         */
        public @NotNull TabBuilder bounds(@NotNull Rect2i normal, @NotNull Rect2i selected) {
            this.normalTextureRect = new Rect2i(normal.getX(), normal.getY(), normal.getWidth(), normal.getHeight()); // Avoid overriding the original value !
            this.selectedTextureRect = new Rect2i(selected.getX(), selected.getY(), selected.getWidth(), selected.getHeight()); // Avoid overriding the original value !
            if (this.isIntegratedByGUI && this.hasGuiSizeBeenSet) {
                this.normalTextureRect.setWidth(this.guiWidth);
                this.normalTextureRect.setHeight(this.guiHeight);
                this.selectedTextureRect.setWidth(this.guiWidth);
                this.selectedTextureRect.setHeight(this.guiHeight);
            }
            return this;
        }

        /**
         * Set up a message on the tab button. (Can be ignored if icon is used)
         */
        public @NotNull TabBuilder message(@NotNull Component message) {
            this.message = message;
            return this;
        }

        /**
         * When tab is pressed, callback is called.
         * @param onPress A valid callback.
         */
        public @NotNull TabBuilder onTabPress(@NotNull TabButton.OnPress onPress) {
            this.tabPressed = onPress;
            return this;
        }

        /**
         * When button is pressed, callback is called.
         * @param onButtonPressed A valid callback.
         */
        public @NotNull TabBuilder onButtonPressed(@NotNull Button.OnPress onButtonPressed){
            this.buttonPressed = onButtonPressed;
            return this;
        }

        /**
         * Now build the button.
         * @return A valid tab button.
         */
        public @NotNull TabButton build() {
            // At last normal and normal texture rectangle need to be set !
            if (this.normalTexture == null || this.normalTextureRect == null) {
                throw new ResourceLocationException("Failed to build() TabButton, NormalTexture or NormalTextureRect not set or null !");
            }

            // Check for selected texture.
            if (this.selectedTexture == null) {
                if (this.selectedTextureRect == null) {
                    // If both is null, make it only normal tab.
                    return new TabButton(this.x, this.y, this.width, this.height, this.textureYOffset, this.normalTexture, this.normalTextureRect, this.buttonPressed != null ? this.buttonPressed : (e) -> {});
                } else { // Selected have UV, then it's inside normal location !
                    return new TabButton(this.x, this.y, this.width, this.height, this.textureYOffset, this.normalTexture, this.normalTextureRect, this.selectedTextureRect, this.tabPressed != null ? this.tabPressed : (e) -> {}, this.message != null ? this.message : Component.empty());
                }
            }
            else {
                // Selected texture rectangle is required.
                if (this.selectedTextureRect == null) {
                    throw new NullPointerException("Failed to build() TabButton, SelectedTextureRect is null, but it's required !");
                }
                return new TabButton(this.x,
                        this.y,
                        this.width,
                        this.height,
                        this.textureYOffset,
                        this.normalTexture,
                        this.normalTextureRect,
                        this.selectedTexture,
                        this.selectedTextureRect,
                        this.tabPressed != null ? this.tabPressed : (e) -> {},
                        this.message != null ? this.message : Component.empty());
            }
        }
    }
}
