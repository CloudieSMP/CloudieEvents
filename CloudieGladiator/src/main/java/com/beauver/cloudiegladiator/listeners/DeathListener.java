package com.beauver.cloudiegladiator.listeners;

import com.beauver.cloudiegladiator.CloudieGladiator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.time.Duration;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        Plugin plugin = CloudieGladiator.getPlugin();

        if(CloudieGladiator.playersFighting.contains(player)){
            player.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            ConfigurationSection spawnLocations = plugin.getConfig().getConfigurationSection("spawnLocations");
            event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());

            Player player1 = CloudieGladiator.playersFighting.get(0);
            Player player2 = CloudieGladiator.playersFighting.get(1);

            player1.getInventory().clear();
            player2.getInventory().clear();

            ItemStack[] inventoryPlayer1 = CloudieGladiator.playersFightingInventory.get(0);
            ItemStack[] inventoryPlayer2 = CloudieGladiator.playersFightingInventory.get(1);

            player1.getInventory().setContents(inventoryPlayer1);
            player2.getInventory().setContents(inventoryPlayer2);

            if (spawnLocations != null) {
                double audienceX = spawnLocations.getDouble("audience.x");
                double audienceY = spawnLocations.getDouble("audience.y");
                double audienceZ = spawnLocations.getDouble("audience.z");

                Location spawnLocationAudiance = new Location(player.getWorld(), audienceX, audienceY, audienceZ);

                if(player1.equals(player)){
                    Component titleText = Component.text("Congratulations: " + player2.getName() + "!").color(TextColor.fromHexString("#55FF55"));
                    Component subtitleText = Component.text(player2.getName() + " has won the battle against " + player1.getName()).color(TextColor.fromHexString("#55FF55"));
                    Title title = Title.title(titleText, subtitleText, Title.Times.times(Duration.ofSeconds(1),Duration.ofSeconds(3),Duration.ofSeconds(1)));

                    player2.setGameMode(GameMode.SURVIVAL);
                    player2.setHealth(20);
                    player2.setFoodLevel(20);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        player1.teleport(spawnLocationAudiance);
                    }, 20);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        player2.teleport(spawnLocationAudiance);
                    }, 200);

                    for(Player player3 : Bukkit.getOnlinePlayers()){
                        player3.showTitle(title);
                    }
                }else{
                    Component titleText = Component.text("Congratulations: " + player1.getName() + "!").color(TextColor.fromHexString("#55FF55"));
                    Component subtitleText = Component.text(player1.getName() + " has won the battle against " + player2.getName()).color(TextColor.fromHexString("#55FF55"));
                    Title title = Title.title(titleText, subtitleText, Title.Times.times(Duration.ofSeconds(1),Duration.ofSeconds(3),Duration.ofSeconds(1)));
                    player1.setGameMode(GameMode.SURVIVAL);

                    player1.setHealth(20);
                    player1.setFoodLevel(20);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        player2.teleport(spawnLocationAudiance);
                    }, 20);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        player1.teleport(spawnLocationAudiance);
                    }, 160);

                    for(Player player3 : Bukkit.getOnlinePlayers()){
                        player3.showTitle(title);
                    }
                }
            }

            CloudieGladiator.playersFighting.clear();
            CloudieGladiator.playersFightingInventory.clear();

            ConfigurationSection doorLocations = plugin.getConfig().getConfigurationSection("doorLocations");
            if (doorLocations != null) {
                double doorX = doorLocations.getDouble("centerBottomDoor1.x");
                double doorY = doorLocations.getDouble("centerBottomDoor1.y");
                double doorZ = doorLocations.getDouble("centerBottomDoor1.z");

                double door2X = doorLocations.getDouble("centerBottomDoor2.x");
                double door2Y = doorLocations.getDouble("centerBottomDoor2.y");
                double door2Z = doorLocations.getDouble("centerBottomDoor2.z");

                int doorWidth = plugin.getConfig().getInt("doorWidth");
                int doorHeight = plugin.getConfig().getInt("doorHeight");

                for (int yOffset = 0; yOffset < doorHeight; yOffset++) {
                    final int currentYOffset = yOffset;

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for (int xOffset = -doorWidth / 2; xOffset <= doorWidth / 2; xOffset++) {
                            for (int zOffset = 0; zOffset < 1; zOffset++) {
                                Location blockLocation = new Location(player.getWorld(), doorX + xOffset, doorY + currentYOffset, doorZ + zOffset);
                                Block block = blockLocation.getBlock();

                                if (block.getType() == Material.AIR) {
                                    block.setType(Material.IRON_BARS);
                                    player.getWorld().spawnParticle(Particle.CLOUD, blockLocation, 10, 0.1, 0.1, 0.1, 0.1);
                                }
                            }
                        }
                    }, yOffset * 20L);
                }

                for (int yOffset = 0; yOffset < doorHeight; yOffset++) {
                    final int currentYOffset = yOffset;

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for (int xOffset = -doorWidth / 2; xOffset <= doorWidth / 2; xOffset++) {
                            for (int zOffset = 0; zOffset < 1; zOffset++) {
                                Location blockLocation = new Location(player.getWorld(), door2X + xOffset, door2Y + currentYOffset, door2Z + zOffset);
                                Block block = blockLocation.getBlock();

                                if (block.getType() == Material.AIR) {
                                    block.setType(Material.IRON_BARS);
                                    player.getWorld().spawnParticle(Particle.CLOUD, blockLocation, 10, 0.1, 0.1, 0.1, 0.1);
                                }
                            }
                        }
                    }, yOffset * 20L);
                }
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "datapack enable \"file/" + plugin.getConfig().getString("gravesPluginName") + "\"");
                player.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
            }, 180);
        }
    }
}