package net.tokyosu.apocalypselib.builder;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.jetbrains.annotations.NotNull;

/**
 * Simplify keybind creation using a factory.
 * You can do new KeybindBuilder().translated().conflict().key().category().build();
 * or instead of translated() use the constructor instead.
 */
public class KeybindBuilder {
    private KeyConflictContext context = KeyConflictContext.UNIVERSAL;
    private String resource_name;
    private InputCategory category;
    private int key;

    /**
     * Base KeybindBuilder constructor.
     */
    public KeybindBuilder() {}

    /**
     * Initialize the KeybindBuilder with a resource name through constructor.
     * @param resource_name A valid resource name.
     * @param translated If true: avoid adding "key." and ".name" since it's already added by it.
     */
    public KeybindBuilder(@NotNull String resource_name, boolean translated) {
        if (translated)
            this.resource_name = "key." + resource_name + ".name";
        else
            this.resource_name = resource_name;
    }

    /**
     * Set the keybind name.
     * @param name A valid resource name.
     */
    public KeybindBuilder resource(@NotNull String name) {
        this.resource_name = name;
        return this;
    }

    /**
     * Set the keybind translatable through language file.
     * @param name The name of the resource, key. and .name is add automatically !
     */
    public KeybindBuilder translated(@NotNull String name) {
        this.resource_name = "key." + name + ".name";
        return this;
    }

    /**
     * Set a key conflict context, default: KeyConflictContext.UNIVERSAL.
     * @param context A valid context.
     */
    public KeybindBuilder conflict(@NotNull KeyConflictContext context) {
        this.context = context;
        return this;
    }

    /**
     * Define the key that will be bind.
     * @param key Use InputConstants.KEY_ to define it.
     */
    public KeybindBuilder key(int key) {
        this.key = key;
        return this;
    }

    /**
     * Select a category for the key.
     * @param category A valid category.
     */
    public KeybindBuilder category(@NotNull InputCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Now build the keymapping.
     * @return A valid KeyMapping.
     */
    public KeyMapping build() {
        return new KeyMapping(this.resource_name, this.context, InputConstants.getKey(this.key, -1), this.category.getName());
    }
}
