package net.tokyosu.apocalypselib.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.apocalypselib.ApocalypseLib;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains some function for simplifying tag/nbt creating, check etc...
 */
@SuppressWarnings("unused")
public class TagUtils {
    /**
     * Process a nbt from a string.
     * @param nbt A valid nbt tag.
     * @return A valid tag compound or null.
     */
    public static @Nullable CompoundTag stringToNBT(@NotNull String nbt) {
        // For now, parse nbt into CompoundTag else return.
        CompoundTag nbtTag;
        try {
            nbtTag = TagParser.parseTag(nbt);
        } catch (CommandSyntaxException e) {
            ApocalypseLib.LOGGER.error("Failed to create an CompoundTag, Error: {}, NBT used: {}", e.getMessage(), nbt);
            return null;
        }
        return nbtTag;
    }

    /**
     * Check if a ItemStack contains nbtRequired tag.
     * WARNING: The more tag nbtRequired have, the more strict it becomes !
     * @param stack A valid ItemStack.
     * @param nbtRequired A valid CompoundTag.
     * @return True if the ItemStack nbt is the same as the nbtRequired tag.
     */
    public static boolean containsNBT(@NotNull ItemStack stack, @NotNull CompoundTag nbtRequired) {
        // Check if the stack have nbt tag, else false.
        if (!stack.hasTag()) return false;
        return NbtUtils.compareNbt(nbtRequired, stack.getTag(), true);
    }
}
