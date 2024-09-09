package org.imradigamer.chainPlugin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class ChainManager {

    private static Location chainOrigin;
    private static final Map<Player, List<ItemDisplay>> chainedPlayers = new HashMap<>();
    private static final Map<Player, Location> initialChainingLocations = new HashMap<>();

    public static void giveKeysToPlayers() {
        for (Player player : chainedPlayers.keySet()) {
            ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
            ItemMeta meta = key.getItemMeta();
            meta.setDisplayName("Llave");
            key.setItemMeta(meta);

            player.getInventory().addItem(key);
        }
    }

    public static boolean hasKeyPermission(Player player) {
        return player.hasPermission("chain.key");
    }

    public static void useKey(Player player) {
        if (!hasKeyPermission(player)) {
            player.sendMessage("Tu llave parece no funcionar!"); //TODO AQUI NO ESTA JALANDO EL MENSAJE
            return;
        }

         //TODO Testear esta vaina
        freeSpecificPlayers(6);
    }

    private static void freeSpecificPlayers(int numberOfPlayersToFree) {
        List<Player> playersToFree = new ArrayList<>(chainedPlayers.keySet());

        playersToFree.removeIf(player -> player.hasPermission("chain.keep"));


        if (numberOfPlayersToFree > playersToFree.size()) {
            numberOfPlayersToFree = playersToFree.size();
        }

        for (int i = 0; i < numberOfPlayersToFree; i++) {
            Player playerToFree = playersToFree.get(i);

            freePlayer(playerToFree);
        }
    }

    private static void freePlayer(Player player) {
        List<ItemDisplay> chainLinks = chainedPlayers.get(player);
        if (chainLinks != null) {
            chainLinks.forEach(ItemDisplay::remove);
        }
        chainedPlayers.remove(player);

        player.sendMessage("Has sido liberado!");
    }
    public static void onPlayerUseKey(Player player, ItemStack item) {
        if (item != null && item.getType() == Material.TRIPWIRE_HOOK && "Key".equals(item.getItemMeta().getDisplayName())) {
            useKey(player);
        }
    }

    public static void setChainOrigin(Player player) {
        chainOrigin = player.getLocation();
    }

    public static void startChainingPlayers(World world) {
        if (chainOrigin == null) {
            return;
        }

        for (Player player : world.getPlayers()) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                chainPlayer(player);
            }
        }
        giveKeysToPlayers();
    }

    private static void chainPlayer(Player player) {
        List<ItemDisplay> chainLinks = new ArrayList<>();
        Location playerLocation = player.getLocation().clone();
        playerLocation.setY(playerLocation.getY() + 1.5);

        double distance = chainOrigin.distance(playerLocation);
        Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();

        for (double i = 0; i < distance; i += 0.5) {
            Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(i));
            ItemDisplay chainLink = player.getWorld().spawn(chainLinkLocation, ItemDisplay.class);
            chainLink.setItemStack(new ItemStack(Material.CHAIN));
            chainLink.setPersistent(true);
            chainLinks.add(chainLink);
        }

        chainedPlayers.put(player, chainLinks);
        initialChainingLocations.put(player, player.getLocation().clone());
    }


    public static void clearAllChains() {
        for (Map.Entry<Player, List<ItemDisplay>> entry : chainedPlayers.entrySet()) {
            Player player = entry.getKey();
            List<ItemDisplay> chainLinks = entry.getValue();
            chainLinks.forEach(ItemDisplay::remove); // Remove chain links
        }
        chainedPlayers.clear();
        initialChainingLocations.clear();
    }

          public static void updateChains() {
              if (chainOrigin == null) {
                  return;
              }

              for (Map.Entry<Player, List<ItemDisplay>> entry : chainedPlayers.entrySet()) {
                  Player player = entry.getKey();
                  List<ItemDisplay> chainLinks = entry.getValue();


                  Location playerLocation = player.getLocation().clone();
                  playerLocation.setY(playerLocation.getY() + 1.2);


                  double distance = chainOrigin.distance(playerLocation);
                  Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();


                  if (direction.length() == 0) {
                      direction = new Vector(0, 1, 0);
                  }


                  double sagFactor = calculateSagFactor(distance);

                  Location previousLinkLocation = chainOrigin.clone();
                  double totalLinks = chainLinks.size();

                  for (int i = 0; i < chainLinks.size(); i++) {
                      ItemDisplay chainLink = chainLinks.get(i);


                      double t = i / totalLinks;
                      Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(t * distance));

                      double sag = Math.sin(Math.PI * t) * sagFactor; // CURVA SIN
                      chainLinkLocation.setY(chainLinkLocation.getY() - sag);

                      Vector toNextLink = chainLinkLocation.toVector().subtract(previousLinkLocation.toVector()).normalize();
                      if (toNextLink.length() == 0) {
                          toNextLink = new Vector(1, 0, 0);
                      }
                      float yaw = (float) Math.toDegrees(Math.atan2(toNextLink.getZ(), toNextLink.getX())) - 90;

                      //yaw valido como numero finito
                      if (Double.isNaN(yaw) || !Double.isFinite(yaw)) {
                          yaw = 0;
                      }

                      chainLinkLocation.setYaw(yaw);

                      chainLink.teleport(chainLinkLocation);

                      previousLinkLocation = chainLinkLocation;
                  }
              }
          }

    private static double calculateSagFactor(double distance) {
        double maxSag = 2.0;
        double minSag = 0.2;
        double sagFactor = maxSag / (distance + 1);
        return Math.max(minSag, sagFactor);
    }

    public static boolean isPlayerChained(Player player) {
        return chainedPlayers.containsKey(player);
    }
    public static Location getInitialChainingLocation(Player player) {
        return initialChainingLocations.get(player);
    }
}