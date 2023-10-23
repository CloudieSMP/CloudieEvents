package com.beauver.cloudiecrazy.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.beauver.cloudiecrazy.CloudieCrazy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
@CommandAlias("crazy")
@CommandPermission("cloudie.admin.crazy")
public class crazy extends BaseCommand {

    @Subcommand("start")
    @Default
    public void onCrazy(Player player){

        if(CloudieCrazy.crazyStarted){
            player.sendMessage(Component.text("Crazy has already started, please finish or stop it."));
            return;
        }

        CloudieCrazy.crazyStarted = true;

        Component titleText = Component.text("Crazy..?").color(TextColor.fromHexString("#55FF55"));
        Component subtitleText = Component.text("");
        Title title = Title.title(titleText, subtitleText, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));

        for(Player player1 : Bukkit.getOnlinePlayers()){
            player1.showTitle(title);
        }
    }

    @Subcommand("stop")
    public void onCrazyStop(){
        CloudieCrazy.crazyStarted = false;
        CloudieCrazy.crazyPosition = 0;
        CloudieCrazy.lastMessage = null;

        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(Component.text("The craziness quiets down...").color(TextColor.fromHexString("#55FF55")));
        }
    }
}
