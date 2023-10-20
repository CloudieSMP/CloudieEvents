package com.beauver.cloudiegladiator.listeners;

import com.beauver.cloudiegladiator.CloudieGladiator;
import com.beauver.cloudiegladiator.classes.KitConfiguration;
import com.beauver.cloudiegladiator.commands.createKit;
import com.beauver.cloudiegladiator.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.List;

public class InteractionListener implements Listener {

    private final createKit createKit;

    public InteractionListener(createKit createKit) {
        this.createKit = createKit;
    }

    @EventHandler
    public void onInteraction(PlayerInteractAtEntityEvent event){
        Player player =  event.getPlayer();

        if(Util.isHoldingItem(player)){
            if(player.getInventory().getItemInMainHand().getType() == Material.NAME_TAG){
                if(player.hasPermission("cloudie.admin.renameArmorStand")){
                    event.setCancelled(true);
                    return;
                }
            }else{
                return;
            }
        }

        if(event.getRightClicked() instanceof ArmorStand){
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();
            String name = armorStand.getName();
            List<String> kitNames = createKit.listKitNames(); // Get the list of kit names
            if (kitNames.contains(name)) {
                player.getInventory().clear();
                createKit.onGet(player, name);
                event.setCancelled(true);
            }
        }
    }

}
