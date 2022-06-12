package com.goldfrosch.events;

import org.bukkit.*;
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
      Location loc = new Location(player.getWorld(), 4, 124, -272);
      player.getWorld().getBlockAt(loc).setType(Material.AIR);
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
          e.getPlayer().getInventory().getItemInMainHand().setType(Material.AIR);
          var random = Math.random();
          if (random > (1.0 / (double) bullet)) {
            bullet--;
            for(Player user: Bukkit.getOnlinePlayers()) {
              user.playSound(user.getLocation(), Sound.ITEM_CROSSBOW_LOADING_END, 2, 2);
              user.sendMessage("불발되었습니다");
              user.sendMessage("현재 남은 탄알: " + 1 + "/" + bullet);
            }
          } else {
            value.setHealth(0);
            value.getWorld().playEffect(value.getLocation(), Effect.FIREWORK_SHOOT,5, 5);
            value.getWorld().playSound(value.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 4, 4);
            for(Player user: Bukkit.getOnlinePlayers()) {
              user.sendTitle(ChatColor.BLUE + "다음 컨텐츠 만들사람", ChatColor.AQUA + value.getName(), 20,10,10);
            }

            bullet = 6;
          }
          Location loc = new Location(value.getWorld(), 4, 124, -272);
          value.getWorld().getBlockAt(loc).setType(Material.REDSTONE_BLOCK);
        });
        selectedPlayer = null;
      }
    }

  }
}
