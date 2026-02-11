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
 * Define a button that can change it's state from normal and hovered or vice versa.
 */
public class HoverButton extends ImageButton {
    private final ResourceLocation normalTexture;
    private final ResourceLocation hoveredTexture;
    private final OnPress onHoverPressed;
    private Rect2i normalTextureRect;
    private Rect2i hoveredTextureRect;

    public HoverButton(int x, int y, int width, int height, int textureX, int textureY, @NotNull ResourceLocation texture, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureX, textureY, texture, onPress);
        this.normalTexture = texture;
        this.hoveredTexture = null;
        this.onHoverPressed = null;
    }

    public HoverButton(int x, int y, int width, int height, int textureX, int textureY, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureX, textureY, textureYOffset, texture, onPress);
        this.normalTexture = texture;
        this.hoveredTexture = null;
        this.onHoverPressed = null;
    }

    public HoverButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i textureRect, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, textureRect.getX(), textureRect.getY(), textureYOffset, texture, textureRect.getWidth(), textureRect.getHeight(), onPress);
        this.normalTexture = texture;
        this.hoveredTexture = null;
        this.onHoverPressed = null;
    }

    public HoverButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i textureRect, @NotNull Button.OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, textureRect.getWidth(), textureRect.getY(), textureYOffset, texture, textureRect.getWidth(), textureRect.getHeight(), onPress, message);
        this.normalTexture = texture;
        this.hoveredTexture = null;
        this.onHoverPressed = null;
    }

    public HoverButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation texture, @NotNull Rect2i normalTextureRect, @NotNull Rect2i selectedTextureRect, @NotNull OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, normalTextureRect.getX(), normalTextureRect.getY(), textureYOffset, texture, normalTextureRect.getWidth(), normalTextureRect.getHeight(), (button) -> {}, message);
        this.normalTexture = texture;
        this.normalTextureRect = normalTextureRect;
        this.hoveredTexture = null;
        this.hoveredTextureRect = selectedTextureRect;
        this.onHoverPressed = onPress;
    }

    public HoverButton(int x, int y, int width, int height, int textureYOffset, @NotNull ResourceLocation normalTexture, @NotNull Rect2i normalTextureRect, @NotNull ResourceLocation selectedTexture, @NotNull Rect2i selectedTextureRect, @NotNull OnPress onPress, @NotNull Component message) {
        super(x, y, width, height, normalTextureRect.getX(), normalTextureRect.getY(), textureYOffset, normalTexture, normalTextureRect.getWidth(), normalTextureRect.getHeight(), (button) -> {}, message);
        this.normalTexture = normalTexture;
        this.normalTextureRect = normalTextureRect;
        this.hoveredTexture = selectedTexture;
        this.hoveredTextureRect = selectedTextureRect;
        this.onHoverPressed = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        if (this.onHoverPressed != null) {
            this.onHoverPressed.onPress(this);
        }
    }

    @Override
    public void renderTexture(@NotNull GuiGraphics pGraphics, @NotNull ResourceLocation texture, int x, int y, int textureX, int textureY, int textureYDiff, int width, int height, int textureWidth, int textureHeight) {
        final var resource = (this.hoveredTexture != null && this.isHovered()) ? this.hoveredTexture : this.normalTexture;
        final var rectangle = (this.hoveredTextureRect != null && this.isHovered()) ? this.hoveredTextureRect : this.normalTextureRect;
        super.renderTexture(pGraphics, resource, x, y, rectangle.getX(), rectangle.getY(), textureYDiff, width, height, rectangle.getWidth(), rectangle.getHeight());
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(HoverButton button);
    }

    /**
     * Created a hover button using factory.
     * @param integratedTexture Does the texture is used by the GUI too, use false if the hover texture is separated !
     */
    public static @NotNull HoverBuilder builder(boolean integratedTexture) {
        return new HoverBuilder(integratedTexture);
    }

    /**
     * Helper to create hover button.
     */
    public static class HoverBuilder {
        private @Nullable ResourceLocation normalTexture = null;
        private @Nullable ResourceLocation hoveredTexture = null;
        private @Nullable Component message = null;
        private @Nullable Rect2i normalTextureRect = null;
        private @Nullable Rect2i hoveredTextureRect = null;
        private @Nullable HoverButton.OnPress hoverPressed = null;
        private @Nullable Button.OnPress buttonPressed = null;
        private int x = 0, y = 0, width = 0, height = 0, textureYOffset = 0;
        private int guiWidth = 0, guiHeight = 0;
        private boolean isIntegratedByGUI = false;
        private boolean hasGuiSizeBeenSet = false;

        /**
         * Constructor of HoverBuilder.
         * @param integratedTexture Does the texture is used by the GUI too, use false if the hover texture is separated !
         */
        public HoverBuilder(boolean integratedTexture) {
            this.isIntegratedByGUI = integratedTexture;
        }

        /**
         * Set the position of the hover button.
         */
        public @NotNull HoverBuilder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        /**
         * Size of the hover button.
         */
        public @NotNull HoverBuilder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Dont call it, its set by MenuBase !
         * @param width Texture width of the GUI.
         * @param height Texture height of the GUI.
         */
        public @NotNull HoverBuilder sizeGui(int width, int height) {
            this.guiWidth = width;
            this.guiHeight = height;
            this.hasGuiSizeBeenSet = true;
            return this;
        }

        /**
         * Offset the hover button.
         */
        public @NotNull HoverBuilder offset(int y) {
            this.textureYOffset = y;
            return this;
        }

        /**
         * Hover texture, use normal texture or both if bounds() is defined.
         * Use bounds() to set up the texture position.
         * @param normal A valid ResourceLocation.
         */
        public @NotNull HoverBuilder texture(@NotNull ResourceLocation normal) {
            this.normalTexture = normal;
            this.hoveredTexture = null;
            return this;
        }

        /**
         * Hover texture, use split normal and selected texture.
         * Use bounds() to set up the texture position.
         * @param normal A valid ResourceLocation.
         * @param selected A valid ResourceLocation.
         */
        public @NotNull HoverBuilder texture(@NotNull ResourceLocation normal, @NotNull ResourceLocation selected) {
            this.normalTexture = normal;
            this.hoveredTexture = selected;
            return this;
        }

        /**
         * Hover texture position in their ResourceLocation.
         * @param normal A valid texture position.
         */
        public @NotNull HoverBuilder bounds(@NotNull Rect2i normal) {
            this.normalTextureRect = normal;
            this.hoveredTextureRect = null;
            if (this.isIntegratedByGUI && this.hasGuiSizeBeenSet) {
                this.normalTextureRect.setWidth(this.guiWidth);
                this.normalTextureRect.setHeight(this.guiHeight);
            }
            return this;
        }

        /**
         * Hover texture position in their ResourceLocation.
         * @param normal A valid texture position.
         * @param selected A valid texture position.
         */
        public @NotNull HoverBuilder bounds(@NotNull Rect2i normal, @NotNull Rect2i selected) {
            this.normalTextureRect = normal;
            this.hoveredTextureRect = selected;
            if (this.isIntegratedByGUI && this.hasGuiSizeBeenSet) {
                this.normalTextureRect.setWidth(this.guiWidth);
                this.normalTextureRect.setHeight(this.guiHeight);
                this.hoveredTextureRect.setWidth(this.guiWidth);
                this.hoveredTextureRect.setHeight(this.guiHeight);
            }
            return this;
        }

        /**
         * Set up a message on the hover button. (Can be ignored if icon is used)
         */
        public @NotNull HoverBuilder message(@NotNull Component message) {
            this.message = message;
            return this;
        }

        /**
         * When hover is pressed, callback is called.
         * @param onHoverPressed A valid callback.
         */
        public @NotNull HoverBuilder onTabPress(@NotNull HoverButton.OnPress onHoverPressed) {
            this.hoverPressed = onHoverPressed;
            return this;
        }

        /**
         * When button is pressed, callback is called.
         * @param onButtonPressed A valid callback.
         */
        public @NotNull HoverBuilder onButtonPressed(@NotNull Button.OnPress onButtonPressed){
            this.buttonPressed = onButtonPressed;
            return this;
        }

        /**
         * Now build the button.
         * @return A valid hover button.
         */
        public @NotNull HoverButton build() {
            // At last normal and normal texture rectangle need to be set !
            if (this.normalTexture == null || this.normalTextureRect == null) {
                throw new ResourceLocationException("Failed to build() HoverButton, NormalTexture or NormalTextureRect not set or null !");
            }

            // Check for hovered texture.
            if (this.hoveredTexture == null) {
                if (this.hoveredTextureRect == null) { // If both is null, make it only normal tab.
                    return new HoverButton(this.x, this.y, this.width, this.height, this.textureYOffset, this.normalTexture, this.normalTextureRect, this.buttonPressed != null ? this.buttonPressed : (e) -> {});
                } else { // Selected have UV, then it's inside normal location !
                    return new HoverButton(this.x, this.y, this.width, this.height, this.textureYOffset, this.normalTexture, this.normalTextureRect, this.hoveredTextureRect, this.hoverPressed != null ? this.hoverPressed : (e) -> {}, this.message != null ? this.message : Component.empty());
                }
            }
            else {
                // Hovered texture rectangle is required.
                if (this.hoveredTextureRect == null) {
                    throw new NullPointerException("Failed to build() HoverButton, HoveredTextureRect is null, but it's required !");
                }
                return new HoverButton(this.x,
                        this.y,
                        this.width,
                        this.height,
                        this.textureYOffset,
                        this.normalTexture,
                        this.normalTextureRect,
                        this.hoveredTexture,
                        this.hoveredTextureRect,
                        this.hoverPressed != null ? this.hoverPressed : (e) -> {},
                        this.message != null ? this.message : Component.empty());
            }
        }
    }
}
