package com.goldfrosch.commands;

import com.goldfrosch.RussianRoulette;
import com.goldfrosch.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class Commands extends AbstractCommand {
  public Commands(RussianRoulette plugin, String Command) {
    super(plugin, Command);
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
    return null;
  }

  @Override
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (label.equalsIgnoreCase("game")) {
        if (args.length == 0) {
          player.sendMessage("어쩔 티비");
        } else {
          if ("give".equals(args[0])) {
            Objects.requireNonNull(Bukkit.getPlayer(args[1]))
                .getWorld()
                .dropItemNaturally(
                    Objects.requireNonNull(Bukkit.getPlayer(args[1])).getLocation(),
                    Constants.GUN()
                );
          }
        }
      } else {
        player.sendMessage("없다 명령어");
      }
    }
    return false;
  }
}
