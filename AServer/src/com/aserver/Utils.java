package com.aserver;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
	static public void log(String str) {
		System.out.println(str);
	}
	
	static public String pName() {
		return "[AServer]";
	}
	
	static public void give(Player player, Material item, int amount) {
		player.getInventory().addItem(new ItemStack(item, amount));
	}
	
	static public String formatMessage(String msg) {
		if(msg.isEmpty()) 
			return msg;
		
		msg = msg.replace("&aqua", ChatColor.AQUA.toString());
		msg = msg.replace("&black", ChatColor.BLACK.toString());
		msg = msg.replace("&blue", ChatColor.BLUE.toString());
		msg = msg.replace("&gold", ChatColor.GOLD.toString());
		msg = msg.replace("&gray", ChatColor.GRAY + "");
		msg = msg.replace("&green", ChatColor.GREEN + "");
		msg = msg.replace("&lpurple", ChatColor.LIGHT_PURPLE + "");
		msg = msg.replace("&red", ChatColor.RED + "");
		msg = msg.replace("&white", ChatColor.WHITE + "");
		msg = msg.replace("&yellow", ChatColor.YELLOW + "");
		
		return msg;
	}
	
	static public String idToItem(String id) {
		return Material.matchMaterial(id).name();
	}
}
