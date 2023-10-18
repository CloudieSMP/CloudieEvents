package com.beauver.cloudiegladiator.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.beauver.cloudiegladiator.CloudieGladiator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class reloadConfig extends BaseCommand {

    @CommandAlias("cgreload")
    @CommandPermission("cloudie.admin.reload")
    public void onReload(Player player) {
        Plugin plugin = CloudieGladiator.getPlugin();
        plugin.reloadConfig();

        player.sendMessage(Component.text("Cloudie-Gladiator config has been reloaded!").color(TextColor.fromHexString("#FFAA00")));
    }

}
