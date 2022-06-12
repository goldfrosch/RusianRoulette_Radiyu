package com.goldfrosch.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Constants {
  public final static ItemStack GUN() {
    ItemStack item = new ItemStack(Material.IRON_HOE, 1);

    var itemMeta = item.getItemMeta();

    itemMeta.setDisplayName(ChatColor.AQUA + "룰렛 총");
    itemMeta.setCustomModelData(2);

    item.setItemMeta(itemMeta);

    return item;
  }
}
