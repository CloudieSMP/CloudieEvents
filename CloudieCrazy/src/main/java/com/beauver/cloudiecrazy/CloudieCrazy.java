package com.beauver.cloudiecrazy;

import co.aikar.commands.PaperCommandManager;
import com.beauver.cloudiecrazy.Commands.crazy;
import com.beauver.cloudiecrazy.listener.chatListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import javax.security.auth.login.LoginException;
import java.util.*;

public final class CloudieCrazy extends JavaPlugin {

    public static int crazyPosition = 0;
    public static boolean crazyStarted = false;
    public static List<String> crazyLines = new ArrayList<>();
    public static Player lastMessage = null;

    private static CloudieCrazy plugin;
    public static Plugin getPlugin() {
        return plugin;
    }
    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("|---[ CloudieCrazy ]-------------------------------------|");
        getLogger().info("|                                                        |");

        try {
            enableClasses();
        } catch (LoginException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Enable commands, listeners, etc.
        enableCommands();
        enableListeners();

        getLogger().info("|                                                        |");
        getLogger().info("|-----------------------------[ ENABLED SUCCESSFULLY ]---|");
    }

    public void enableCommands(){
        PaperCommandManager manager = new PaperCommandManager(this);
        //Moderation commands
        manager.registerCommand(new crazy());

        getLogger().info("|   Enabled commands                                     |");
    }

    public void enableListeners() {
        this.getServer().getPluginManager().registerEvents(new chatListener(), this);
        getLogger().info("|   Enabled listeners                                    |");

    }

    public void enableClasses() throws LoginException, InterruptedException {
        crazyLines.add("I was Crazy Once");
        crazyLines.add("They Locked Me In A Room");
        crazyLines.add("A Rubber Room");
        crazyLines.add("A Rubber Room With Rats");
        crazyLines.add("And Rats Make Me Crazy");

        getLogger().info("|   Enabled Classes                                      |");
    }

    @Override
    public void onDisable() {
        getLogger().info("|---[ CloudieCrazy ]-------------------------------------|");
        getLogger().info("|                                                        |");
        getLogger().info("|                                                        |");
        getLogger().info("|----------------------------[ DISABLED SUCCESSFULLY ]---|");
    }
}
