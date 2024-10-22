package org.imradigamer.chainPlugin.Books;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
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
    private static final long COOLDOWN_TIME_BOOKS = 14500;
    private static final long COOLDOWN_TIME_NOTES = 7500;
    private final HashMap<UUID, Long> cooldowns_books = new HashMap<>();
    private final HashMap<UUID, Long> cooldowns_notes = new HashMap<>();

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
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        Map<String, String> titleMap = new HashMap<>();
        titleMap.put("nota_1", "ꐽ");
        titleMap.put("nota_2", "ꐾ");
        titleMap.put("nota_3", "ꐿ");
        titleMap.put("nota_4", "ꑀ");
        titleMap.put("nota_5", "ꑁ");
        titleMap.put("nota_6", "ꑂ");
        titleMap.put("nota_7", "ꑃ");
        titleMap.put("nota_8", "ꑄ");
        titleMap.put("nota_9", "ꑅ");
        titleMap.put("nota_10", "ꑆ");

        if (entity instanceof Interaction) {
            Interaction interaction = (Interaction) entity;

            UUID playerUUID = player.getUniqueId();

            if (isPlayerOnCooldown_notes(playerUUID)) {
                player.sendTitle(" ","",1,1,1);
                removeCooldown_notes(player);
                return;
            }


            for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                String tag = entry.getKey();
                String title = entry.getValue();

                if (interaction.getScoreboardTags().contains(tag)) {
                    setCooldown(player,false);
                    player.sendTitle(title, "", 5, 100, 5);  // Title: specific title, no subtitle, timings (fade in: 10 ticks, stay: 70 ticks, fade out: 20 ticks)
                    break;
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
                return 51;
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

            UUID playerUUID = player.getUniqueId();

            if (isPlayerOnCooldown_books(playerUUID)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopimage "+player.getName());
                removeCooldown_books(player);
                return;
            }

            if (itemInHand != null && itemInHand.getType() == Material.LEATHER) {
                ItemMeta meta = itemInHand.getItemMeta();
                if (meta != null && meta.hasCustomModelData()) {
                    int customModelData = meta.getCustomModelData();

                    switch (customModelData) {
                        case 26:
                            broadcastLibroMessage(player,"1_El_Principito.png");
                            break;
                        case 43:
                            broadcastLibroMessage(player,"2_Don_quijote_de_la_mancha.png");
                            break;
                        case 27:
                            broadcastLibroMessage(player,"3_La_biblia.png");
                            break;
                        case 28:
                            broadcastLibroMessage(player,"4_cien_años_de_soledad.png");
                            break;
                        case 29:
                            broadcastLibroMessage(player,"5_Divina_comedia.png");
                            break;
                        case 30:
                            broadcastLibroMessage(player, "6_El_enigma_sagrado.png");
                            break;
                        case 51:
                            broadcastLibroMessage(player,"7_El_cuervo.png");
                            break;
                        case 31:
                            broadcastLibroMessage(player,"8_El_hombre_de_tiza.png");
                            break;
                        case 32:
                            broadcastLibroMessage(player,"9_El_monje_que_vendió_su_ferrari.png");
                            break;
                        case 33:
                            broadcastLibroMessage(player,"10_Cronicas_de_una_muerte_anunciada.png");
                            break;
                        default:
                            break;
                    }
                    setCooldown(player, true);
                }
            }
        }
    }

    private void broadcastLibroMessage(Player player, String libro) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playimage "+ player.getName()+ " 100 50 50 0 15 0 https://media.mbpcreators.com/"+libro);
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
        Map<String, Integer> estanteRequirements = new HashMap<>();
        estanteRequirements.put("estante_1", 26);
        estanteRequirements.put("estante_2", 43);
        estanteRequirements.put("estante_3", 27);
        estanteRequirements.put("estante_4", 28);
        estanteRequirements.put("estante_5", 29);
        estanteRequirements.put("estante_6", 30);
        estanteRequirements.put("estante_7", 51);
        estanteRequirements.put("estante_8", 31);
        estanteRequirements.put("estante_9", 32);
        estanteRequirements.put("estante_10", 33);

        for (Map.Entry<String, Integer> entry : estanteRequirements.entrySet()) {
            String estanteTag = entry.getKey();
            int expectedCustomModelData = entry.getValue();

            boolean estanteFound = false;
            for (Interaction interaction : Bukkit.getWorlds().get(0).getEntitiesByClass(Interaction.class)) {
                if (interaction.getScoreboardTags().contains(estanteTag)) {
                    estanteFound = true;
                    BookData savedBook = estanteBooks.get(interaction.getUniqueId());

                    if (savedBook == null || savedBook.getCustomModelData() != expectedCustomModelData) {
                        return;
                    }
                }
            }

            if (!estanteFound) {
                return;
            }
        }

        Bukkit.broadcastMessage(ChatColor.of("#32a852") + "Completado!");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"door two d true");
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
                return customModelData == 51;
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

    private void setCooldown(Player player, Boolean books) {
        if(books){
        cooldowns_books.put(player.getUniqueId(), System.currentTimeMillis());
        }else{
            cooldowns_notes.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }
    private void removeCooldown_books(Player player) {
        cooldowns_books.remove(player.getUniqueId());
    }
    private void removeCooldown_notes(Player player) {
        cooldowns_notes.remove(player.getUniqueId());
    }
    private boolean isPlayerOnCooldown_books(UUID playerUUID) {
        if (!cooldowns_books.containsKey(playerUUID)) {
            return false;
        }

        long lastInteractionTime = cooldowns_books.get(playerUUID);
        return (System.currentTimeMillis() - lastInteractionTime) < COOLDOWN_TIME_BOOKS;
    }
    private boolean isPlayerOnCooldown_notes(UUID playerUUID) {
        if (!cooldowns_notes.containsKey(playerUUID)) {
            return false;
        }

        long lastInteractionTime = cooldowns_notes.get(playerUUID);
        return (System.currentTimeMillis() - lastInteractionTime) < COOLDOWN_TIME_NOTES;
    }


    public Map<UUID, BookData> getEstanteBooks() {
        return estanteBooks;
    }
}
