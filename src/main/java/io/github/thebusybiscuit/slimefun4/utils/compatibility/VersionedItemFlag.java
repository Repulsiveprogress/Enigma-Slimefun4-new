package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import org.bukkit.inventory.ItemFlag;

public class VersionedItemFlag {

    // HIDE_ADDITIONAL_TOOLTIP exists since 1.20.5; we target 26.1+ so it's always present.
    public static final ItemFlag HIDE_ADDITIONAL_TOOLTIP = ItemFlag.HIDE_ADDITIONAL_TOOLTIP;

    private VersionedItemFlag() {}
}
