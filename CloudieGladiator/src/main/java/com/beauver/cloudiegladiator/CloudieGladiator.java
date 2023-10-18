package com.beauver.cloudiegladiator;

import co.aikar.commands.PaperCommandManager;
import com.beauver.cloudiegladiator.commands.SendPeople;
import com.beauver.cloudiegladiator.commands.StartCommand;
import com.beauver.cloudiegladiator.listeners.DeathListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class CloudieGladiator extends JavaPlugin {

    private static CloudieGladiator plugin;
    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("|---[ CloudieSMP - Gladiator ]---------------------------|");
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
        manager.registerCommand(new StartCommand());
        manager.registerCommand(new SendPeople());

        getLogger().info("|   Enabled commands                                     |");
    }

    public void enableListeners() {
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);

        getLogger().info("|   Enabled listeners                                    |");

    }

    public void enableClasses() throws LoginException, InterruptedException {
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
