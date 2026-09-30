package net.tokyosu.apocalypselib.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Output paths are resolved beneath an explicit base directory. */
public final class FileUtils {
    private FileUtils() {}

    public static Path resolveWithin(Path base, String relativePath) {
        Path root = base.toAbsolutePath().normalize();
        Path result = root.resolve(relativePath).normalize();
        if (!result.startsWith(root) || result.equals(root)) {
            throw new IllegalArgumentException("Output must resolve beneath " + root);
        }
        return result;
    }

    public static Path writeUtf8(Path base, String relativePath, String content) throws IOException {
        Path path = resolveWithin(base, relativePath);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardCharsets.UTF_8);
        return path;
    }
}
