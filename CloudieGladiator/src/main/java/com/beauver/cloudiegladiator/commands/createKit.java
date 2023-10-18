package com.beauver.cloudiegladiator.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("cgkit")
public class createKit extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("cloudie.events.admin.kit.create")
    public void onCreateKit(Player player, String kitName){
        if(kitName == null){
            player.sendMessage(Component.text("Please specify a kit name.").color(TextColor.fromHexString("FF0000")));
            return;
        }

        Inventory kitInventory = Bukkit.createInventory(player, 45, Component.text("Creating kit - " + kitName));
        player.openInventory(kitInventory);

        
    }
}
