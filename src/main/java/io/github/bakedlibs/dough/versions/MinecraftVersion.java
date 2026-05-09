package io.github.bakedlibs.dough.versions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 * Patched MinecraftVersion that supports the new YY.D.P.build.N versioning
 * introduced in Minecraft 26.1 (2026). The original dough SemanticVersion.parse()
 * fails on strings like "26.1.2.build.2585" because they are not valid semver.
 */
public class MinecraftVersion extends SemanticVersion {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    public MinecraftVersion(int major, int minor, int patch) {
        super(major, minor, patch);
    }

    private MinecraftVersion(int major, int minor, int patch, @SuppressWarnings("unused") boolean ignored) {
        super(major, minor, patch);
    }

    public static MinecraftVersion of(Server server) throws UnknownServerVersionException {
        if (server == null) {
            throw new UnknownServerVersionException("null",
                new IllegalArgumentException("Server should not be null!"));
        }

        // getBukkitVersion() returns e.g.:
        //   "26.1.2.build.2585-stable"  (new 2026 format)
        //   "1.21.1-R0.1-SNAPSHOT"      (old format)
        String raw = server.getBukkitVersion();

        // Take only the part before the first '-'
        String versionPart = raw.split("-")[0];

        Matcher m = VERSION_PATTERN.matcher(versionPart);
        if (m.find()) {
            int major = Integer.parseInt(m.group(1));
            int minor = Integer.parseInt(m.group(2));
            int patch = m.group(3) != null ? Integer.parseInt(m.group(3)) : 0;
            return new MinecraftVersion(major, minor, patch, true);
        }

        throw new UnknownServerVersionException(raw,
            new IllegalArgumentException("Could not parse \"" + versionPart + "\" as a version."));
    }

    public static MinecraftVersion get() throws UnknownServerVersionException {
        return of(Bukkit.getServer());
    }

    public static boolean isMocked(Server server) {
        Class<?> cls = server.getClass();
        while (cls != null) {
            if (cls.getName().endsWith("mockbukkit.ServerMock")) {
                return true;
            }
            cls = cls.getSuperclass();
        }
        return false;
    }

    public static boolean isMocked() {
        return isMocked(Bukkit.getServer());
    }
}
