package org.imradigamer.chainPlugin.Circus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

public class InteractionListener implements Listener, CommandExecutor {

    private final JavaPlugin plugin;
    private final HashSet<UUID> interactedPlayers = new HashSet<>();

    public InteractionListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        // Check if the player is holding the correct item (stick with CustomModelData 667000)
        ItemStack item = player.getInventory().getItemInMainHand();
        if (isCorrectItem(item) && entity.getScoreboardTags().contains("puerta_final") && !interactedPlayers.contains(player.getUniqueId())) {
            // Execute command
            Bukkit.dispatchCommand(player, "enddoor open"); // Replace with your command
            // Mark player as interacted
            interactedPlayers.add(player.getUniqueId());
        }
    }

    // Helper method to check if the item is a stick with the required CustomModelData
    private boolean isCorrectItem(ItemStack item) {
        if (item == null || item.getType() != Material.STICK) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 667000;
    }

    // Command to reset interactions for all players
    public void resetInteractions() {
        interactedPlayers.clear();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
       resetInteractions();
        return false;
    }
}
