package com.beauver.cloudiegladiator.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.beauver.cloudiegladiator.CloudieGladiator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import java.util.Arrays;

@CommandAlias("gladiator")
public class SendPeople extends BaseCommand {

    @Subcommand("sendplayers")
    @CommandCompletion("@players")
    @CommandPermission("cloudie.events.admin.sendpeople")
    public void onSend(Player player, String[] args){
        Plugin plugin = CloudieGladiator.getPlugin();
        ConfigurationSection spawnLocations = plugin.getConfig().getConfigurationSection("spawnLocations");

        if(args.length < 2){
            player.sendMessage(Component.text("Please provide TWO people who should fight in the gladiator event.").color(TextColor.fromHexString("#FF0000")));
            return;
        }

        Player player1 = Bukkit.getPlayer(args[0]);
        Player player2 = Bukkit.getPlayer(args[1]);

        if(player1 == null){
            player.sendMessage(Component.text("The first mentioned player is currently not online.").color(TextColor.fromHexString("#FF0000")));
            return;
        }else if(player2 == null){
            player.sendMessage(Component.text("The second mentioned player is currently not online.").color(TextColor.fromHexString("#FF0000")));
            return;
        }else if(player1 == player2){
            player.sendMessage(Component.text("You can not send the same player twice.").color(TextColor.fromHexString("#FF0000")));
            return;
        }
        CloudieGladiator.playersFighting.add(player1);
        CloudieGladiator.playersFighting.add(player2);

        if (spawnLocations != null) {
            double player1X = spawnLocations.getDouble("player1.x");
            double player1Y = spawnLocations.getDouble("player1.y");
            double player1Z = spawnLocations.getDouble("player1.z");

            double player2X = spawnLocations.getDouble("player2.x");
            double player2Y = spawnLocations.getDouble("player2.y");
            double player2Z = spawnLocations.getDouble("player2.z");

            Location spawnLocationPlayer1 = new Location(player.getWorld(), player1X, player1Y, player1Z);
            Location spawnLocationPlayer2 = new Location(player.getWorld(), player2X, player2Y, player2Z);

            player1.teleport(spawnLocationPlayer1);
            player2.teleport(spawnLocationPlayer2);

            player1.setGameMode(GameMode.SURVIVAL);
            player2.setGameMode(GameMode.SURVIVAL);
        }

        CloudieGladiator.playersFightingInventory.add(player1.getInventory().getContents());
        CloudieGladiator.playersFightingInventory.add(player2.getInventory().getContents());
        player1.getInventory().clear();
        player2.getInventory().clear();

        player.sendMessage(Component.text("Sent " + player1.getName() + " and " + player2.getName() + " to the arena!").color(TextColor.fromHexString("#00AA00")));
    }

    @Subcommand("retrieveplayers")
    @CommandPermission("cloudie.events.admin.retrievepeople")
    public void onRetrieve(Player player){

        Plugin plugin = CloudieGladiator.getPlugin();

        if(CloudieGladiator.playersFighting.isEmpty()){
            player.sendMessage(Component.text("There is currently no one fighting.").color(TextColor.fromHexString("#FF0000")));
            return;
        }

        Player player1 = CloudieGladiator.playersFighting.get(0);
        Player player2 = CloudieGladiator.playersFighting.get(1);

        ItemStack[] inventoryPlayer1 = CloudieGladiator.playersFightingInventory.get(0);
        ItemStack[] inventoryPlayer2 = CloudieGladiator.playersFightingInventory.get(1);

        CloudieGladiator.playersFighting.clear();
        CloudieGladiator.playersFightingInventory.clear();

        ConfigurationSection spawnLocations = plugin.getConfig().getConfigurationSection("spawnLocations");
        if (spawnLocations != null) {
            double audienceX = spawnLocations.getDouble("audience.x");
            double audienceY = spawnLocations.getDouble("audience.y");
            double audienceZ = spawnLocations.getDouble("audience.z");

            Location spawnLocationAudiance = new Location(player.getWorld(), audienceX, audienceY, audienceZ);

            player1.teleport(spawnLocationAudiance);
            player2.teleport(spawnLocationAudiance);
        }

        player1.getInventory().clear();
        player2.getInventory().clear();

        player1.getInventory().setContents(inventoryPlayer1);
        player2.getInventory().setContents(inventoryPlayer2);

        player.sendMessage(Component.text("Retrieved " + player1.getName() + " and " + player2.getName() + " to the audience!").color(TextColor.fromHexString("#00AA00")));
    }
}
