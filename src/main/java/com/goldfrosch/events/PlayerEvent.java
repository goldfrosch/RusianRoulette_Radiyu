package com.goldfrosch.events;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerEvent implements Listener {
    public static UUID selectedPlayer = null;
    private static int bullet = 6;

    @EventHandler
    public void onPlayerSelectEntity(PlayerInteractEntityEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_HOE)) {
            if (
                e.getHand().equals(EquipmentSlot.HAND) &&
                    e.getRightClicked() instanceof Player
            ) {
                Player player = (Player) e.getRightClicked();
                if (player.getUniqueId().equals(selectedPlayer)) {
                    selectedPlayer = null;
                    e.getRightClicked().setGlowing(false);
                } else {
                    Objects.requireNonNull(Bukkit.getPlayer(selectedPlayer)).setGlowing(false);
                    selectedPlayer = player.getUniqueId();
                    e.getRightClicked().setGlowing(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeftClick(PlayerInteractEvent e) {
        if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_HOE)) {
            if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                if (selectedPlayer != null) {
                    Optional<Player> player = Optional.ofNullable(Bukkit.getPlayer(selectedPlayer));
                    player.ifPresent(value -> {
                        value.setGlowing(false);
                        if (Math.random() > (double) (1 / bullet)) {
                            bullet--;
                        } else {
                            value.setHealth(0);
                            value.getWorld().playEffect(value.getLocation(), Effect.FIREWORK_SHOOT,1);
                            value.getWorld().playSound(value.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1, 1);
                            selectedPlayer = null;
                            bullet = 6;
                        }
                    });
                }
            }
        }
    }
}
