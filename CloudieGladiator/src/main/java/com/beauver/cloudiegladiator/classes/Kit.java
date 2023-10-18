package com.beauver.cloudiegladiator.classes;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Kit {
    private String kitName;
    private ItemStack[] hotbar = new ItemStack[9];
    private ItemStack[] inventory = new ItemStack[27];
    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public ItemStack[] getHotbar() {
        return hotbar;
    }

    public void setHotbar(ItemStack[] hotbar) {
        this.hotbar = hotbar;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void addHotbarItem(ItemStack item, int slot) {
        if (slot >= 0 && slot < hotbar.length) {
            hotbar[slot] = item.clone(); // Clone the item to avoid unexpected changes
        }
    }

    public void addInventoryItem(ItemStack item, int slot) {
        if (slot >= 0 && slot < inventory.length) {
            inventory[slot] = item.clone(); // Clone the item to avoid unexpected changes
        }
    }
}
