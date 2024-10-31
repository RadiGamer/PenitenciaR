package org.imradigamer.chainPlugin.Circus;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CircusCommand implements CommandExecutor, TabCompleter {

    private final BarrierManager barrierManager;
    private final ItemDisplayManager itemDisplayManager;

    public CircusCommand(BarrierManager barrierManager, ItemDisplayManager itemDisplayManager) {
        this.barrierManager = barrierManager;
        this.itemDisplayManager = itemDisplayManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for correct arguments
        if (args.length < 2) {
            sender.sendMessage("Usage: /circo <1/2> <abrir/cerrar>");
            return true;
        }

        // Get the world "world"
        World world = Bukkit.getWorld("world");

        String areaId = args[0];
        String action = args[1];

        // Perform action based on the command arguments
        switch (areaId) {
            case "1":
                if (action.equalsIgnoreCase("abrir")) {
                    barrierManager.modifyBarrierBlocks(1, true);
                    itemDisplayManager.updateItemDisplay("puerta_circo_1", 119);
                    sendFeedback(sender, "Abierta area 1.");
                } else if (action.equalsIgnoreCase("cerrar")) {
                    barrierManager.modifyBarrierBlocks(1, false);
                    itemDisplayManager.updateItemDisplay("puerta_circo_1", 120);
                    sendFeedback(sender, "Cerrada area 1.");
                } else {
                    sender.sendMessage("Usa 'abrir' o 'cerrar'.");
                }
                break;

            case "2":
                if (action.equalsIgnoreCase("abrir")) {
                    barrierManager.modifyBarrierBlocks(2, true);
                    itemDisplayManager.updateItemDisplay("puerta_circo_2", 119);
                    sendFeedback(sender, "Abierta area 2.");
                } else if (action.equalsIgnoreCase("cerrar")) {
                    barrierManager.modifyBarrierBlocks(2, false);
                    itemDisplayManager.updateItemDisplay("puerta_circo_2", 120);
                    sendFeedback(sender, "Cerrada area 2.");
                } else {
                    sender.sendMessage("Usa 'abrir' o 'cerrar'.");
                }
                break;
            case "3":
                if (action.equalsIgnoreCase("abrir")) {
                    barrierManager.modifyBarrierBlocks(3, true);
                    itemDisplayManager.updateItemDisplay("puerta_circo_3", 119);
                    sendFeedback(sender, "Abierta area 3.");
                } else if (action.equalsIgnoreCase("cerrar")) {
                    barrierManager.modifyBarrierBlocks(3, false);
                    itemDisplayManager.updateItemDisplay("puerta_circo_3", 120);
                    sendFeedback(sender, "Cerrada area 3.");
                } else {
                    sender.sendMessage("Usa 'abrir' o 'cerrar'.");
                }
                break;

            default:
                sender.sendMessage("Area invalida ID. Usa 1, 2 o 3.");
                break;
        }

        return true;
    }

    // Utility method to send feedback to both players and console
    private void sendFeedback(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else if (sender instanceof ConsoleCommandSender) {
            Bukkit.getLogger().info("[CircusCommand] " + message);
        } else {
            sender.sendMessage(message);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // First argument is the area ID, suggesting "1", "2", or "3"
            return Arrays.asList("1", "2", "3");
        } else if (args.length == 2) {
            // Second argument is the action, suggesting "abrir" or "cerrar"
            return Arrays.asList("abrir", "cerrar");
        }
        // No further arguments
        return Collections.emptyList();
    }
}
