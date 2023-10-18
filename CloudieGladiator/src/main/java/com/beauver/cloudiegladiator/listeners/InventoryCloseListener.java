package com.beauver.cloudiegladiator.listeners;

import com.beauver.cloudiegladiator.CloudieGladiator;
import com.beauver.cloudiegladiator.classes.Kit;
import com.beauver.cloudiegladiator.classes.KitConfiguration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class InventoryCloseListener implements Listener {
    private Kit kit;
    private Player player;
    private KitConfiguration kitConfig; // Add this line

    public InventoryCloseListener(Kit kit, Player player, KitConfiguration kitConfig) {
        this.kit = kit;
        this.player = player;
        this.kitConfig = kitConfig; // Add this line
    }

    @EventHandler
    public void onGuiClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player closedPlayer = (Player) event.getPlayer();

        if (closedPlayer.equals(player)) {
            Inventory closedInventory = event.getInventory();

            if (closedInventory != null && closedInventory.getItem(44) != null) {
                if (closedInventory.getItem(44).getItemMeta() != null &&
                        closedInventory.getItem(44).getItemMeta().hasCustomModelData() &&
                        closedInventory.getItem(44).getItemMeta().getCustomModelData() == 69) {
                    // Save the kit when the player closes the kit creation inventory
                    saveKit(kit, kitConfig); // Pass kitConfig
                    player.sendMessage(Component.text("Kit saved!").color(TextColor.fromHexString("#55FF55")));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Inventory closedInventory = event.getInventory();

        if (closedInventory != null && closedInventory.getItem(44) != null) {
            Player player = (Player) event.getWhoClicked();
            if (closedInventory.getItem(44).getItemMeta() != null &&
                    closedInventory.getItem(44).getItemMeta().hasCustomModelData() &&
                    closedInventory.getItem(44).getItemMeta().getCustomModelData() == 69) {

                ItemStack clickedItem = event.getCurrentItem();

                if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                    int slot = event.getSlot();

                    if (slot >= 0 && slot < 9) {
                        kit.addHotbarItem(clickedItem, slot);
                    } else if (slot >= 9 && slot < 36) {
                        kit.addInventoryItem(clickedItem, slot - 9);
                    }
                }
            }
        }
    }

    private static void saveKit(Kit kit, KitConfiguration kitConfig) { // Add KitConfiguration parameter
        String kitName = kit.getKitName();

        // Get the configuration file
        FileConfiguration config = kitConfig.getConfig();

        // Serialize the kit data and store it in the configuration
        config.set("kits." + kitName + ".hotbar", kit.getHotbar());
        config.set("kits." + kitName + ".inventory", kit.getInventory());

        // Save the configuration
        kitConfig.saveConfig();
    }
}


