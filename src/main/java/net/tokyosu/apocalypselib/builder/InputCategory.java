package net.tokyosu.apocalypselib.builder;

import net.minecraft.client.KeyMapping;

/**
 * Key category in-game as enum.
 */
public enum InputCategory {
    CREATIVE(KeyMapping.CATEGORY_CREATIVE),
    UI(KeyMapping.CATEGORY_INTERFACE),
    GAMEPLAY(KeyMapping.CATEGORY_GAMEPLAY),
    INVENTORY(KeyMapping.CATEGORY_INVENTORY),
    MISC(KeyMapping.CATEGORY_MISC),
    MOVEMENT(KeyMapping.CATEGORY_MOVEMENT),
    MULTIPLAYER(KeyMapping.CATEGORY_MULTIPLAYER);

    private final String name;

    public String getName() { return name; }

    InputCategory(String category) {
        this.name = category;
    }
}
