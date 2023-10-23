package com.beauver.cloudiecrazy.listener;

import com.beauver.cloudiecrazy.CloudieCrazy;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class chatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event){
        Player player1 = event.getPlayer();
        TextComponent msgComponent = (TextComponent) event.message();

        if(!CloudieCrazy.crazyStarted){
            return;
        }

        if(msgComponent.content().equalsIgnoreCase(CloudieCrazy.crazyLines.get(CloudieCrazy.crazyPosition))){
            CloudieCrazy.crazyPosition = CloudieCrazy.crazyPosition + 1;

            if(event.getPlayer().equals(CloudieCrazy.lastMessage)){
                event.setCancelled(true);
                CloudieCrazy.crazyPosition = CloudieCrazy.crazyPosition - 1;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getPlayer().kick(Component.text("You may not say two sentences.").color(TextColor.fromHexString("#FFAA00")));
                    }
                }.runTask(CloudieCrazy.getPlugin());
            }else{
                CloudieCrazy.lastMessage = player1;
            }

            if(CloudieCrazy.crazyPosition == CloudieCrazy.crazyLines.size()){
                CloudieCrazy.crazyStarted = false;
                CloudieCrazy.crazyPosition = 0;
                CloudieCrazy.lastMessage = null;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Player player : Bukkit.getOnlinePlayers()){
                            player.sendMessage(Component.text("Congrats, you guys did it!").color(TextColor.fromHexString("#55FF55")));
                        }
                    }
                }.runTask(CloudieCrazy.getPlugin());
            }
        }else{
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().kick(Component.text("Bozo could not finish the crazy meme.").color(TextColor.fromHexString("#FFAA00")));
                }
            }.runTask(CloudieCrazy.getPlugin());
        }
    }
}
