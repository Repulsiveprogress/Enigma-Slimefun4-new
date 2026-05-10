package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

final class GrapplingHookEntity {

    private final Arrow arrow;
    private final Entity leashTarget;

    @ParametersAreNonnullByDefault
    GrapplingHookEntity(Player p, Arrow arrow, Entity leashTarget, boolean dropItem, boolean wasConsumed) {
        this.arrow = arrow;
        this.leashTarget = leashTarget;
    }

    @Nonnull
    public Arrow getArrow() {
        return arrow;
    }

    public void drop(@Nonnull Location l) {
        // Nothing dropped — prevents lead/item duplication exploits
    }

    public void remove() {
        if (arrow.isValid()) {
            arrow.remove();
        }

        if (leashTarget.isValid()) {
            // Remove leash before killing the entity to prevent lead drop
            if (leashTarget instanceof org.bukkit.entity.LivingEntity living && living.isLeashed()) {
                living.setLeashHolder(null);
            }
            leashTarget.remove();
        }
    }

}