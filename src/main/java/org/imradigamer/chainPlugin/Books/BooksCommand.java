package org.imradigamer.chainPlugin.Books;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BooksCommand implements CommandExecutor, TabCompleter {

    private final Map<UUID, BookData> estanteBooks;
    private DisplayManager displayManager;
    public BooksCommand(Map<UUID, BookData> estanteBooks) {
        this.estanteBooks = estanteBooks;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        displayManager = new DisplayManager();

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            resetAllBooksAndDisplays();
            displayManager.setDisplayAndBarriers();
            return true;
        }
        return false;
    }

    private void resetAllBooksAndDisplays() {
        clearAllVisualEstantes();
        Bukkit.getWorlds().forEach(world -> resetVisualLibros(world));

        Bukkit.getWorlds().forEach(world -> respawnInteractionEntities(world));
    }
    private void resetVisualLibros(World world) {
        world.getEntities().stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .forEach(itemDisplay -> {
                    for (int i = 1; i <= 10; i++) {
                        String visualTag = "visual_libro_" + i;
                        if (itemDisplay.getScoreboardTags().contains(visualTag)) {
                            ItemStack book = new ItemStack(Material.LEATHER);
                            ItemMeta meta = book.getItemMeta();
                            if (meta != null) {
                                meta.setCustomModelData(getCustomModelDataForLibro(i));
                                book.setItemMeta(meta);
                            }
                            itemDisplay.setItemStack(book);
                        }
                    }
                });
    }

    private void respawnInteractionEntities(World world) {
        for (int i = 1; i <= 10; i++) {
            String interactionTag = "libro_" + i;
            String visualTag = "visual_libro_" + i;

            world.getEntities().stream()
                    .filter(entity -> entity instanceof ItemDisplay)
                    .map(entity -> (ItemDisplay) entity)
                    .filter(itemDisplay -> itemDisplay.getScoreboardTags().contains(visualTag))
                    .findFirst()
                    .ifPresent(itemDisplay -> {
                        Location itemDisplayLocation = itemDisplay.getLocation();
                        Location interactionLocation = itemDisplayLocation.clone().subtract(0, 1, 0);

                        world.spawn(interactionLocation, Interaction.class, interaction -> {
                            interaction.addScoreboardTag(interactionTag);
                            Bukkit.getLogger().warning(world.getSpawnLocation().toString());
                        });
                    });
        }
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

    private void clearAllVisualEstantes() {
        estanteBooks.clear();
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().stream()
                    .filter(entity -> entity instanceof ItemDisplay)
                    .map(entity -> (ItemDisplay) entity)
                    .filter(itemDisplay -> {
                        for (String tag : itemDisplay.getScoreboardTags()) {
                            if (tag.startsWith("visual_estante_")) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .forEach(itemDisplay -> {
                        itemDisplay.setItemStack(new ItemStack(Material.AIR));
                    });
        });
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reset");
        }
        return Collections.emptyList();
    }

}
