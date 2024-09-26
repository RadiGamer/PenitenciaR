package org.imradigamer.chainPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.imradigamer.chainPlugin.ChainPlugin;

public class ItemTaggerCommand implements CommandExecutor {
    private final ChainPlugin plugin;

    public ItemTaggerCommand(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("itemtagger.use")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        // Process all items in the player's world
        player.getWorld().getEntitiesByClass(ItemDisplay.class).forEach(item -> {
            ItemStack itemStack = item.getItemStack();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String displayName = meta.getDisplayName();
                String tag = null;

                // Match display names and assign tags
                switch (displayName) {
                    case "lat_abajo_izq":
                        tag = "lat_abajo_izq";
                        break;
                    case "lat_abajo_derecha":
                        tag = "lat_abajo_derecha";
                        break;
                    case "centro_abajo_izq":
                        tag = "centro_abajo_izq";
                        break;
                    case "centro_abajo_derecha":
                        tag = "centro_abajo_derecha";
                        break;
                }

                if (tag != null) {
                    // Set custom tag in the PersistentDataContainer
                    PersistentDataContainer data = meta.getPersistentDataContainer();
                    data.set(plugin.customTagKey, PersistentDataType.STRING, tag);

                    // Remove the display name entirely
                    meta.setDisplayName("");
                    itemStack.setItemMeta(meta);
                }
            }
        });

        player.sendMessage("Items have been processed. Display names removed and tags applied.");
        return true;
    }
}
