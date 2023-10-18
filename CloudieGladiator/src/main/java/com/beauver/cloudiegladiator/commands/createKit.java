package com.beauver.cloudiegladiator.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.beauver.cloudiegladiator.CloudieGladiator;
import com.beauver.cloudiegladiator.classes.Kit;
import com.beauver.cloudiegladiator.classes.KitConfiguration;
import com.beauver.cloudiegladiator.listeners.InventoryCloseListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

@CommandAlias("cgkit")
public class createKit extends BaseCommand {
    private final KitConfiguration kitConfig;
    private InventoryCloseListener closeListener;

    public createKit(KitConfiguration kitConfig) {
        this.kitConfig = kitConfig;
    }

    @Subcommand("create")
    @CommandPermission("cloudie.events.admin.kit.create")
    public void onCreateKit(Player player, String kitName) {
        if (kitName == null) {
            player.sendMessage(Component.text("Please specify a kit name.").color(TextColor.fromHexString("#FF0000")));
            return;
        }

        Kit kit = new Kit();
        kit.setKitName(kitName);

        // Create and open the kit inventory
        Inventory kitInventory = Bukkit.createInventory(player, 45, Component.text("Creating kit - " + kitName));
        player.openInventory(kitInventory);

        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Locked"));
        meta.setCustomModelData(69);
        item.setItemMeta(meta);

        for (int i = 44; i > 36; i--) {
            kitInventory.setItem(i, item);
        }

        // Unregister the previous listener if it exists
        if (closeListener != null) {
            HandlerList.unregisterAll(closeListener);
        }

        // Register a new listener to handle interactions and save the kit when the player closes the inventory
        closeListener = new InventoryCloseListener(kit, player, kitConfig);
        Bukkit.getPluginManager().registerEvents(closeListener, CloudieGladiator.getPlugin());
    }

    @Subcommand("get")
    @CommandPermission("cloudie.events.kits.get")
    public void onGet(Player player, String kitName) {
        KitConfiguration kitConfig = new KitConfiguration(CloudieGladiator.getPlugin()); // Use your actual plugin class
        kitConfig.reloadConfig(); // Ensure the config is loaded

        FileConfiguration config = kitConfig.getConfig();

        if (config.contains("kits." + kitName)) {
            Kit kit = new Kit();
            kit.setKitName(kitName);

            ItemStack[] hotbar = retrieveItemArray(config, "kits." + kitName + ".hotbar", 9);
            ItemStack[] inventory = retrieveItemArray(config, "kits." + kitName + ".inventory", 27);

            kit.setHotbar(hotbar);
            kit.setInventory(inventory);

            // Add items to the player's inventory
            addToInventoryWithNullCheck(player.getInventory(), kit.getHotbar());
            addToInventoryWithNullCheck(player.getInventory(), kit.getInventory());

            player.updateInventory(); // Update the player's inventory to reflect the changes
            player.sendMessage(Component.text("Received kit: " + kitName).color(TextColor.fromHexString("#55FF55")));
        } else {
            player.sendMessage(Component.text("Kit not found: " + kitName).color(TextColor.fromHexString("#FF0000")));
        }
    }

    private ItemStack[] retrieveItemArray(FileConfiguration config, String path, int size) {
        List<?> itemDataList = config.getList(path);
        if (itemDataList != null) {
            ItemStack[] itemArray = new ItemStack[size];
            for (int i = 0; i < itemDataList.size(); i++) {
                if (itemDataList.get(i) instanceof ItemStack) {
                    itemArray[i] = (ItemStack) itemDataList.get(i);
                }
            }
            return itemArray;
        }
        return new ItemStack[size];
    }
    private void addToInventoryWithNullCheck(PlayerInventory inventory, ItemStack[] items) {
        for (ItemStack item : items) {
            if (item != null) {
                inventory.addItem(item);
            }
        }
    }

    @Subcommand("delete")
    @CommandPermission("cloudie.events.admin.kit.delete")
    public void onDeleteKit(Player player, String kitName) {
        if (kitName == null) {
            player.sendMessage(Component.text("Please specify a kit name to delete.").color(TextColor.fromHexString("#FF0000")));
            return;
        }

        if (deleteKit(kitName)) {
            player.sendMessage(Component.text("Kit '" + kitName + "' has been deleted.").color(TextColor.fromHexString("#55FF55")));
        } else {
            player.sendMessage(Component.text("Kit '" + kitName + "' not found or couldn't be deleted.").color(TextColor.fromHexString("#FF0000")));
        }
    }

    private boolean deleteKit(String kitName) {
        FileConfiguration config = kitConfig.getConfig();
        if (config.contains("kits." + kitName)) {
            config.set("kits." + kitName, null); // Remove the kit data
            kitConfig.saveConfig(); // Save the updated configuration
            kitConfig.reloadConfig(); // Reload the configuration after saving
            return true;
        }
        return false;
    }
}
