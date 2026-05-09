package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * Applies name/lore translations from items.yml to every registered {@link SlimefunItem}.
 * Called once after items and languages are loaded.
 */
public class ItemLocalizationService {

    private final Slimefun plugin;

    public ItemLocalizationService(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
    }

    /**
     * Patches all registered SlimefunItem stacks with localized name/lore
     * from the server's default language items.yml, falling back to English.
     */
    public void applyLocalizations() {
        Language language = Slimefun.getLocalization().getDefaultLanguage();

        if (language == null) {
            return;
        }

        int patched = 0;

        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            SlimefunItemStack stack = item.getSlimefunItemStack();

            if (stack == null) {
                continue;
            }

            String id = stack.getItemId();

            String name = Slimefun.getLocalization().getItemName(language, id);
            List<String> lore = Slimefun.getLocalization().getItemLore(language, id);

            if (name == null && lore == null) {
                continue;
            }

            ItemMeta meta = stack.getItemMeta();

            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            if (lore != null) {
                lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
                meta.setLore(lore);
            }

            stack.setItemMeta(meta);
            patched++;
        }

        plugin.getLogger().log(Level.INFO, "Applied item localizations to {0} items.", patched);
    }
}
