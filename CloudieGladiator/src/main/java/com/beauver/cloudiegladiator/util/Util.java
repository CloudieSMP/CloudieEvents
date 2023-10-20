package com.beauver.cloudiegladiator.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {

    public static boolean isHoldingItem(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        return heldItem != null && !heldItem.getType().isAir();
    }
}
