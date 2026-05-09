package io.github.thebusybiscuit.slimefun4.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerTextures;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

/**
 * Replaces dough PlayerSkin/PlayerHead API which relied on extending
 * com.mojang.authlib.GameProfile — now final in Paper 26.1.
 * Uses Paper PlayerProfile API instead.
 */
public final class SkinUtils {

    private SkinUtils() {}

    /**
     * Creates a PLAYER_HEAD ItemStack with the given base64 texture string.
     */
    @Nonnull
    public static ItemStack createHead(@Nonnull String base64Texture) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        applyBase64ToMeta(skull, base64Texture, UUID.nameUUIDFromBytes(base64Texture.getBytes(StandardCharsets.UTF_8)));
        return skull;
    }

    /**
     * Creates a PLAYER_HEAD ItemStack from a hex hash texture code.
     */
    @Nonnull
    public static ItemStack createHeadFromHash(@Nonnull UUID uuid, @Nonnull String textureHash) {
        return createHead(hashToBase64(textureHash));
    }

    /**
     * Applies a base64 skin texture to a block Skull.
     */
    public static void applySkinToBlock(@Nonnull Block block, @Nonnull String base64Texture, @Nonnull UUID uuid) {
        if (!(block.getState() instanceof Skull skull)) {
            return;
        }
        skull.setPlayerProfile(buildProfile(uuid, base64Texture));
        skull.update(true, false);
    }

    /**
     * Fetches the base64 skin texture for a player UUID asynchronously.
     */
    @Nonnull
    public static CompletableFuture<String> fetchBase64ForUUID(@Nonnull Plugin plugin, @Nonnull UUID playerUUID) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                org.bukkit.profile.PlayerProfile bukkit = Bukkit.createPlayerProfile(playerUUID);
                bukkit = bukkit.update().get();
                PlayerTextures textures = bukkit.getTextures();
                URL skinUrl = textures.getSkin();
                if (skinUrl != null) {
                    String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + skinUrl.toString() + "\"}}}";
                    String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
                    future.complete(base64);
                } else {
                    future.complete(null);
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Nonnull
    public static String hashToBase64(@Nonnull String hash) {
        String json = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + hash + "\"}}}";
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    private static void applyBase64ToMeta(@Nonnull ItemStack skull, @Nonnull String base64, @Nonnull UUID uuid) {
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta == null) return;
        meta.setPlayerProfile(buildProfile(uuid, base64));
        skull.setItemMeta(meta);
    }

    @Nonnull
    private static PlayerProfile buildProfile(@Nonnull UUID uuid, @Nonnull String base64) {
        PlayerProfile profile = Bukkit.createProfile(uuid, "SF_" + uuid.toString().substring(0, 8));
        profile.setProperty(new ProfileProperty("textures", base64));
        return profile;
    }
}
