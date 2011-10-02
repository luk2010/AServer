package com.aserver;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ACommandExecutor implements CommandExecutor
{
	private AServer server;
	
	public ACommandExecutor(AServer aServer) {
		this.setServer(aServer);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String base,
			String[] args) {
		
		if(!(sender instanceof Player))
			return true;
		
		String arg1 = args.length >= 1 ? args[0] : "";
		String arg2 = args.length >= 2 ? args[1] : "";
		String arg3 = args.length >= 3 ? args[2] : "";
		String arg4 = args.length >= 4 ? args[3] : "";
		
		if(base.equalsIgnoreCase("debug")) {
			server.getPlayerManager().onPlayerChangeDebugging((Player)sender);
			return true;
		}
		
		
		else if(base.equalsIgnoreCase("sethome")) {
			if(arg1.isEmpty()) {
				server.getPlayerManager().onPlayerChangeMainHome((Player)sender);
				return true;
			}
			else {
				server.getPlayerManager().onPlayerChangeHome((Player) sender, arg1);
				return true;
			}
		}
		
		
		else if(base.equalsIgnoreCase("home")) {
			if(arg1.isEmpty()) {
				server.getPlayerManager().teleportHome((Player) sender, server.getPlayerManager().getMainHomeName());
				return true;
			}
			else {
				server.getPlayerManager().teleportHome((Player) sender, arg1);
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("setTPoint")) {
			if(arg1.isEmpty()) {
				return false;
			}
			else {
				server.getPlayerManager().onPlayerChangeTPoint((Player) sender, arg1);
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("goTPoint")) {
			if(arg1.isEmpty()) {
				return false;
			}
			else {
				server.getPlayerManager().teleportTPoint((Player) sender, arg1);
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("tp")) {
			if(arg1.isEmpty()) {
				return false;
			}
			else {
				Player dest = this.server.getPlayerManager().getPlayer(arg1);
				if(dest != null) {
					server.getPlayerManager().teleportPlayer((Player) sender, server.getServer().getPlayer(arg1));
					return true;
				}
				else {
					((Player)sender).sendMessage(ChatColor.RED + "Impossible de trouver le joueur " + arg1 + " !");
					return false;
				}
			}
		}
		
		else if(base.equalsIgnoreCase("god")) {
			server.getPlayerManager().onPlayerChangeGodMode((Player) sender);
			return true;
		}
		
		else if(base.equalsIgnoreCase("list")) {
			
			if(arg1.equalsIgnoreCase("players")) {
				((Player) sender).sendMessage(ChatColor.GREEN + "Liste des joueurs en ligne :");
				((Player) sender).sendMessage(ChatColor.YELLOW + "----------------------------");
				for(Player player : server.getPlayerManager().getPlayers()) {
					((Player) sender).sendMessage(ChatColor.BLUE + player.getDisplayName());
				}
				((Player) sender).sendMessage(ChatColor.YELLOW + "----------------------------");
				return true;
			}
			
			else if(arg1.equalsIgnoreCase("home")) {
				((Player) sender).sendMessage(ChatColor.GREEN + "Liste des maisons enregistrees :");
				((Player) sender).sendMessage(ChatColor.YELLOW + "--------------------------------");
				for(Place place : server.getPlayerManager().getPlayerOptions((Player)sender).getHomes()) {
					if(place.getName() == server.getPlayerManager().getMainHomeName()) {
						((Player) sender).sendMessage(ChatColor.BLUE + "Principale" + " : x = " + place.getX() + " y = " + place.getY() + " z = " + place.getZ());
					}
					else {
						((Player) sender).sendMessage(ChatColor.BLUE + place.getName() + " : x = " + place.getX() + " y = " + place.getY() + " z = " + place.getZ());
					}
				}
				((Player) sender).sendMessage(ChatColor.YELLOW + "--------------------------------");
				return true;
			}
			
			else if(arg1.equalsIgnoreCase("tpoint")) {
				((Player) sender).sendMessage(ChatColor.GREEN + "Liste des points de teleportation enregistres :");
				((Player) sender).sendMessage(ChatColor.YELLOW + "-----------------------------------------------");
				for(Place place : server.getPlayerManager().getPlayerOptions((Player)sender).getTpoints()) {
					((Player) sender).sendMessage(ChatColor.BLUE + place.getName() + " : x = " + place.getX() + " y = " + place.getY() + " z = " + place.getZ());
				}
				((Player) sender).sendMessage(ChatColor.YELLOW + "-----------------------------------------------");
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("give")) {
			if(arg3.isEmpty()) {
				Material material = Material.matchMaterial(arg2);
				if(material == null) {
					((Player) sender).sendMessage(ChatColor.RED + "Le materiau demande n'existe pas !");
					return false;
				}
				
				int amount = Integer.parseInt(arg1);
				if(amount <= 0) {
					amount = 1;
				}
				
				while(amount > 0) {
					if(amount/64 > 1) {
						Utils.give((Player) sender, material, 64);
						amount -= 64;
					}
					else {
						Utils.give((Player) sender, material, amount);
						amount -= amount + 1;
					}
				}
				
				return true;
			}
			else {
				Material material = Material.matchMaterial(arg3);
				if(material == null) {
					((Player) sender).sendMessage(ChatColor.RED + "Le materiau demande n'existe pas !");
					return false;
				}
				
				int amount = Integer.parseInt(arg2);
				if(amount <= 0) {
					amount = 1;
				}
				
				if(server.getPlayerManager().getPlayer(arg1) == null) {
					((Player) sender).sendMessage(ChatColor.RED + "Le joueur specifie n'est pas trouve !");
					return false;
				}
				
				Player player = server.getPlayerManager().getPlayer(arg1);
				
				while(amount > 0) {
					if(amount/64 > 1) {
						Utils.give(player, material, 64);
						amount -= 64;
					}
					else {
						Utils.give(player, material, amount);
						amount -= amount + 1;
					}
				}
				
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("set")) {
			
			if(arg1.equalsIgnoreCase("chat")) {
				
				if(arg2.equalsIgnoreCase("name")) {
					
					if(arg3.equalsIgnoreCase("color")) {
						
						if(arg4.isEmpty()) {
							((Player) sender).sendMessage(ChatColor.RED + "Vous n'avez pas specife la couleur !");
							return false;
						}
						
						server.getPlayerManager().getPlayerOptions(server.getPlayerManager().getPlayer(sender.getName())).setDefaultNameColor(arg4);
						return true;
					}
				}
				else if(arg2.equalsIgnoreCase("text")) {
					
					if(arg3.equalsIgnoreCase("color")) {
						
						if(arg4.isEmpty()) {
							((Player) sender).sendMessage(ChatColor.RED + "Vous n'avez pas specife la couleur !");
							return false;
						}
						
						server.getPlayerManager().getPlayerOptions(server.getPlayerManager().getPlayer(sender.getName())).setDefaultChatColor(arg4);
						return true;
					}
				}
			}
		}
		
		else if(base.equalsIgnoreCase("get")) {
			
			if(arg1.equalsIgnoreCase("itemid")) {
				
				if(arg2.isEmpty()) {
					((Player) sender).sendMessage(ChatColor.RED + "Veuillez indiquer l'ID !");
					return false;
				}
				
				((Player) sender).sendMessage(ChatColor.BLUE + "ID : " + arg2 + " Item : " + Utils.idToItem(arg2));
				return true;
			}
		}
		
		return false;
	}

	public AServer getServer() {
		return server;
	}

	public void setServer(AServer server) {
		this.server = server;
	}
}
