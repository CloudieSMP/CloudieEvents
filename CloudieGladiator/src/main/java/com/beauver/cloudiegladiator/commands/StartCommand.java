package com.beauver.cloudiegladiator.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.beauver.cloudiegladiator.CloudieGladiator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

@CommandAlias("gladiator")
public class StartCommand extends BaseCommand {

    @Subcommand("start")
    @CommandPermission("cloudie.events.staff.startgladiator")
    public void onStart(Player player, String[] args) {
        Plugin plugin = CloudieGladiator.getPlugin();
        if (CloudieGladiator.playersFighting.isEmpty()) {
            player.sendMessage(Component.text("There are no players ready to fight, can not initiate start.").color(TextColor.fromHexString("#FF0000")));
            return;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "datapack disable \"file/" + plugin.getConfig().getString("gravesPluginName") + "\"");
        Player player1 = CloudieGladiator.playersFighting.get(0);
        Player player2 = CloudieGladiator.playersFighting.get(1);

        int delay = 100;
        for (Player player3 : Bukkit.getOnlinePlayers()) {
            Component titleText = Component.text(player1.getName() + " VS " + player2.getName()).color(TextColor.fromHexString("#55FF55"));
            Component subtitleText = Component.text("");
            Title title = Title.title(titleText, subtitleText, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));
            player3.showTitle(title);

            // Start displaying titles with a delay
            Bukkit.getScheduler().runTaskLater(CloudieGladiator.getPlugin(), () -> {
                showTitles(player3, 3);
            }, delay);
        }
        Bukkit.getScheduler().runTaskLater(CloudieGladiator.getPlugin(), () -> {
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

                                if (block.getType() == Material.IRON_BARS) {
                                    block.setType(Material.AIR);
                                    // Spawn cloud particles at the location
                                    player.getWorld().spawnParticle(Particle.CLOUD, blockLocation, 10, 0.1, 0.1, 0.1, 0.1);
                                }
                            }
                        }
                    }, yOffset * 20L);
                }

                // Clear out the second door layer by layer
                for (int yOffset = 0; yOffset < doorHeight; yOffset++) {
                    final int currentYOffset = yOffset;

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for (int xOffset = -doorWidth / 2; xOffset <= doorWidth / 2; xOffset++) {
                            for (int zOffset = 0; zOffset < 1; zOffset++) {
                                Location blockLocation = new Location(player.getWorld(), door2X + xOffset, door2Y + currentYOffset, door2Z + zOffset);
                                Block block = blockLocation.getBlock();

                                if (block.getType() == Material.IRON_BARS) {
                                    block.setType(Material.AIR);
                                    // Spawn cloud particles at the location
                                    player.getWorld().spawnParticle(Particle.CLOUD, blockLocation, 10, 0.1, 0.1, 0.1, 0.1);
                                }
                            }
                        }
                    }, yOffset * 20L);
                }
            }
        }, delay);
    }

    public void showTitles(Player player3, int i) {
        if (i > 0) {
            String color = "#55FF55";
            switch (i) {
                case 2:
                    color = "#FFAA00";
                    break;
                case 1:
                    color = "#FF0000";
                    break;
            }

            Component titleText1 = Component.text(i).color(TextColor.fromHexString(color));
            Component subtitleText1 = Component.text("");
            Title title1 = Title.title(titleText1, subtitleText1, Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0)));
            player3.showTitle(title1);

            Bukkit.getScheduler().runTaskLater(CloudieGladiator.getPlugin(), () -> {
                showTitles(player3, i - 1);
            }, 20);
        } else if (i == 0) {
            Component titleText2 = Component.text("FIGHT!").color(TextColor.fromHexString("#FF0000"));
            Component subtitleText2 = Component.text("");
            Title title2 = Title.title(titleText2, subtitleText2, Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(2), Duration.ofSeconds(1)));
            player3.showTitle(title2);
            player3.playSound(player3.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1.0f);
        }
    }
}
