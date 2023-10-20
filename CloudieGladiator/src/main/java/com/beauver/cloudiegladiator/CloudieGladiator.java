package com.beauver.cloudiegladiator;

import co.aikar.commands.PaperCommandManager;
import com.beauver.cloudiegladiator.classes.KitConfiguration;
import com.beauver.cloudiegladiator.commands.SendPeople;
import com.beauver.cloudiegladiator.commands.StartCommand;
import com.beauver.cloudiegladiator.commands.createKit;
import com.beauver.cloudiegladiator.commands.reloadConfig;
import com.beauver.cloudiegladiator.listeners.DeathListener;
import com.beauver.cloudiegladiator.listeners.InteractionListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public final class CloudieGladiator extends JavaPlugin {


    public static List<Player> playersFighting = new ArrayList<>();
    public static List<ItemStack[]> playersFightingInventory = new ArrayList<>();
    private static CloudieGladiator plugin;
    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("|---[ CloudieSMP - Gladiator ]---------------------------|");
        getLogger().info("|                                                        |");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

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
        manager.registerCommand(new StartCommand());
        manager.registerCommand(new reloadConfig());
        manager.registerCommand(new SendPeople());
        manager.registerCommand(new createKit(new KitConfiguration(this)));

        getLogger().info("|   Enabled commands                                     |");
    }

    public void enableListeners() {
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new InteractionListener(new createKit(new KitConfiguration(this))), this);
        getLogger().info("|   Enabled listeners                                    |");

    }

    public void enableClasses() throws LoginException, InterruptedException {
//        KitCreationListener.initialize(this);
        getLogger().info("|   Enabled Classes                                      |");
    }

    @Override
    public void onDisable() {
        getLogger().info("|---[ CloudieSMP - Gladiator ]---------------------------|");
        getLogger().info("|                                                        |");
        getLogger().info("|                                                        |");
        getLogger().info("|----------------------------[ DISABLED SUCCESSFULLY ]---|");
    }
}
