package com.goldfrosch.events;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
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

  private void selectPlayerMessage() {
    for (Player player: Bukkit.getOnlinePlayers()) {
      player.sendMessage("플레이어가 선택되었습니다.");
      player.sendMessage("선택된 플레이어: " + Objects.requireNonNull(Bukkit.getPlayer(selectedPlayer)).getName());
    }
  }

  @EventHandler
  public void onPlayerSelectEntity(PlayerInteractEntityEvent e) {
    if (
        e.getHand().equals(EquipmentSlot.HAND) &&
            e.getRightClicked().getType().equals(EntityType.PLAYER) &&
            e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_HOE)
    ) {
      Player player = (Player) e.getRightClicked();
      if (selectedPlayer == null) {
        selectedPlayer = player.getUniqueId();
        selectPlayerMessage();
        e.getRightClicked().setGlowing(true);
      } else {
        if (selectedPlayer.equals(player.getUniqueId())) {
          selectedPlayer = null;
          e.getRightClicked().setGlowing(false);
        } else {
          Objects.requireNonNull(Bukkit.getPlayer(selectedPlayer)).setGlowing(false);
          selectedPlayer = player.getUniqueId();
          selectPlayerMessage();
          e.getRightClicked().setGlowing(true);
        }
      }
    }
  }

  @EventHandler
  public void onPlayerLeftClick(PlayerInteractEvent e) {
    if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_HOE)) {
      if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
        Optional<Player> player = Optional.ofNullable(Bukkit.getPlayer(selectedPlayer));
        player.ifPresent(value -> {
          value.setGlowing(false);
          var random = Math.random();
          if (random > (1.0 / (double) bullet)) {
            bullet--;
            for(Player user: Bukkit.getOnlinePlayers()) {
              user.sendMessage(random + " / " + (1.0 / (double) bullet));
              user.sendMessage("현재 남은 탄알: " + 1 + "/" + bullet);
            }
          } else {
            value.setHealth(0);
            value.getWorld().playEffect(value.getLocation(), Effect.FIREWORK_SHOOT,5, 5);
            value.getWorld().playSound(value.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 4, 4);for(Player user: Bukkit.getOnlinePlayers()) {
              user.sendMessage("탄알이 사용되었습니다");
            }
            selectedPlayer = null;
            bullet = 6;
          }
        });
      }
    }

  }
}
