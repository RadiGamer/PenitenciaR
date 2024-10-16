package org.imradigamer.chainPlugin.Books;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractionListener implements Listener {

    private final Map<UUID, BookData> estanteBooks = new HashMap<>();
    private static final long COOLDOWN_TIME = 5000;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof Interaction) {
            Interaction interaction = (Interaction) event.getRightClicked();

            for (int i = 1; i <= 10; i++) {
                String libroTag = "libro_" + i;
                String visualLibroTag = "visual_libro_" + i;

                if (interaction.getScoreboardTags().contains(libroTag)) {
                    giveBookToPlayer(player, i);

                    updateVisualBook(interaction, visualLibroTag, i);

                    interaction.remove();

                    return;
                }
            }

            for (int i = 1; i <= 10; i++) {
                String estanteTag = "estante_" + i;
                if (interaction.getScoreboardTags().contains(estanteTag)) {
                    handleEstanteInteraction(player, interaction, estanteTag);
                    return;
                }
            }
        }
    }
    private void giveBookToPlayer(Player player, int libroNumber) {
        ItemStack book = new ItemStack(Material.LEATHER);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(getCustomModelDataForLibro(libroNumber));
            meta.setDisplayName(" ");
            book.setItemMeta(meta);

        }

        player.getInventory().addItem(book);
        player.sendMessage("Has recibido el libro " + libroNumber);
    }

    private int getCustomModelDataForLibro(int libroNumber) {
        switch (libroNumber) {
            case 1:
                return 26;
            case 2:
                return 43;
            case 3:
                return 27;
            case 4:
                return 28;
            case 5:
                return 29;
            case 6:
                return 30;
            case 7:
                return 36;
            case 8:
                return 31;
            case 9:
                return 32;
            case 10:
                return 33;
            default:
                return 0;
        }
    }
    private void updateVisualBook(Interaction interaction, String visualLibroTag, int libroNumber) {
        interaction.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .filter(itemDisplay -> itemDisplay.getScoreboardTags().contains(visualLibroTag))
                .forEach(itemDisplay -> {
                    itemDisplay.setItemStack(new ItemStack(Material.AIR));

                    Bukkit.getLogger().info("Removed visual book for " + visualLibroTag);
                });
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (itemInHand != null && itemInHand.getType() == Material.LEATHER) {
                ItemMeta meta = itemInHand.getItemMeta();
                if (meta != null && meta.hasCustomModelData()) {
                    int customModelData = meta.getCustomModelData();

                    switch (customModelData) {
                        case 26:
                            broadcastLibroMessage(1);
                            break;
                        case 43:
                            broadcastLibroMessage(2);
                            break;
                        case 27:
                            broadcastLibroMessage(3);
                            break;
                        case 28:
                            broadcastLibroMessage(4);
                            break;
                        case 29:
                            broadcastLibroMessage(5);
                            break;
                        case 30:
                            broadcastLibroMessage(6);
                            break;
                        case 36:
                            broadcastLibroMessage(7);
                            break;
                        case 31:
                            broadcastLibroMessage(8);
                            break;
                        case 32:
                            broadcastLibroMessage(9);
                            break;
                        case 33:
                            broadcastLibroMessage(10);
                            break;
                        default:
                            break;
                    }
                    setCooldown(player);
                }
            }
        }
    }

    private void broadcastLibroMessage(int libroNumber) {
        Bukkit.broadcastMessage("Aquí se interactuó con el libro " + libroNumber);
    }

    private void handleEstanteInteraction(Player player, Interaction interaction, String estanteTag) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        UUID interactionUUID = interaction.getUniqueId();

        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            if (estanteBooks.containsKey(interactionUUID)) {
                BookData savedBook = estanteBooks.get(interactionUUID);

                ItemStack returnedBook = new ItemStack(Material.LEATHER);
                ItemMeta meta = returnedBook.getItemMeta();
                meta.setDisplayName(" ");
                meta.setCustomModelData(savedBook.getCustomModelData());
                returnedBook.setItemMeta(meta);

                player.getInventory().addItem(returnedBook);
                Bukkit.getLogger().info("Se sacó el libro de " + estanteTag);

                estanteBooks.remove(interactionUUID);

                updateVisualEstante(estanteTag, null);

            } else {
                player.sendMessage(ChatColor.of("#850129") + "El estante parece estar vacío...");
            }
        } else if (itemInHand.getType() == Material.LEATHER) {
            ItemMeta meta = itemInHand.getItemMeta();
            if (meta != null && meta.hasCustomModelData()) {
                int customModelData = meta.getCustomModelData();

                if (estanteBooks.containsKey(interactionUUID)) {
                    player.sendMessage(ChatColor.of("#850129") + "Este estante ya tiene un libro guardado.");
                    return;
                }

                for (int i = 1; i <= 10; i++) {
                    if (matchesLibro(customModelData, i)) {
                        estanteBooks.put(interactionUUID, new BookData(customModelData));

                        player.getInventory().getItemInMainHand().setAmount(0);

                        Bukkit.getLogger().info("Se guardó un libro en " + estanteTag);

                        ItemStack displayBook = new ItemStack(Material.LEATHER);
                        ItemMeta displayMeta = displayBook.getItemMeta();
                        displayMeta.setCustomModelData(customModelData);
                        displayBook.setItemMeta(displayMeta);

                        updateVisualEstante(estanteTag, displayBook);

                        checkForCompletion();

                        return;
                    }
                }
            }
        }
    }

    private void updateVisualEstante(String estanteTag, ItemStack item) {
        String visualTag = "visual_" + estanteTag;

        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().stream()
                    .filter(entity -> entity instanceof ItemDisplay)
                    .map(entity -> (ItemDisplay) entity)
                    .filter(itemDisplay -> itemDisplay.getScoreboardTags().contains(visualTag))
                    .forEach(itemDisplay -> {
                        if (item == null) {
                            itemDisplay.setItemStack(new ItemStack(Material.AIR));
                        } else {
                            itemDisplay.setItemStack(item);
                        }
                    });
        });
    }

    private void checkForCompletion() {
        for (int i = 1; i <= 10; i++) {
            int expectedCustomModelData;

            switch (i) {
                case 1:
                    expectedCustomModelData = 26;
                    break;
                case 2:
                    expectedCustomModelData = 43;
                    break;
                case 3:
                    expectedCustomModelData = 27;
                    break;
                case 4:
                    expectedCustomModelData = 28;
                    break;
                case 5:
                    expectedCustomModelData = 29;
                    break;
                case 6:
                    expectedCustomModelData = 30;
                    break;
                case 7:
                    expectedCustomModelData = 36;
                    break;
                case 8:
                    expectedCustomModelData = 31;
                    break;
                case 9:
                    expectedCustomModelData = 32;
                    break;
                case 10:
                    expectedCustomModelData = 33;
                    break;
                default:
                    return;
            }

            boolean hasBook = estanteBooks.values().stream()
                    .anyMatch(book -> book.getCustomModelData() == expectedCustomModelData);

            if (!hasBook) {
                return;
            }
        }
        Bukkit.broadcastMessage(ChatColor.of("#32a852") + "Completado!");
    }

    private boolean matchesLibro(int customModelData, int libroNumber) {
        switch (libroNumber) {
            case 1:
                return customModelData == 26;
            case 2:
                return customModelData == 43;
            case 3:
                return customModelData == 27;
            case 4:
                return customModelData == 28;
            case 5:
                return customModelData == 29;
            case 6:
                return customModelData == 30;
            case 7:
                return customModelData == 36;
            case 8:
                return customModelData == 31;
            case 9:
                return customModelData == 32;
            case 10:
                return customModelData == 33;
            default:
                return false;
        }
    }

    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public Map<UUID, BookData> getEstanteBooks() {
        return estanteBooks;
    }
}
