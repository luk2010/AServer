package com.aserver;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
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
		
		boolean isOp = ((Player) sender).isOp();
		
		if(base.equalsIgnoreCase("debug") && (isOp || server.getPermanager().hasCommand((Player) sender, "debug")) ) {
			server.getPlayerManager().onPlayerChangeDebugging((Player)sender);
			return true;
		}
			
		else if(base.equalsIgnoreCase("sethome") && (isOp || server.getPermanager().hasCommand((Player) sender, "sethome"))) {
			if(arg1.isEmpty()) {
				server.getPlayerManager().onPlayerChangeMainHome((Player)sender);
				return true;
			}
			else {
				server.getPlayerManager().onPlayerChangeHome((Player) sender, arg1);
				return true;
			}
		}
		
		
		else if(base.equalsIgnoreCase("home") && (isOp || server.getPermanager().hasCommand((Player) sender, "home"))) {
			if(arg1.isEmpty()) {
				server.getPlayerManager().teleportHome((Player) sender, server.getPlayerManager().getMainHomeName());
				return true;
			}
			else {
				server.getPlayerManager().teleportHome((Player) sender, arg1);
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("setTPoint") && (isOp || server.getPermanager().hasCommand((Player) sender, "setTPoint"))) {
			if(arg1.isEmpty()) {
				return false;
			}
			else {
				server.getPlayerManager().onPlayerChangeTPoint((Player) sender, arg1);
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("goTPoint") && (isOp || server.getPermanager().hasCommand((Player) sender, "goTPoint"))) {
			if(arg1.isEmpty()) {
				return false;
			}
			else {
				server.getPlayerManager().teleportTPoint((Player) sender, arg1);
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("tp") && (isOp || server.getPermanager().hasCommand((Player) sender, "tp"))) {
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
		
		else if(base.equalsIgnoreCase("god") && (isOp || server.getPermanager().hasCommand((Player) sender, "god"))) {
			server.getPlayerManager().onPlayerChangeGodMode((Player) sender);
			return true;
		}
		
		else if(base.equalsIgnoreCase("list") && (isOp || server.getPermanager().hasCommand((Player) sender, "list"))) {
			
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
					if(place.getName().equals(server.getPlayerManager().getMainHomeName())) {
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
		
		else if(base.equalsIgnoreCase("give") && (isOp || server.getPermanager().hasCommand((Player) sender, "give"))) {
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
		
		else if(base.equalsIgnoreCase("set") && (isOp || server.getPermanager().hasCommand((Player) sender, "set"))) {
			
			if(arg1.equalsIgnoreCase("chat")) {
				
				if(arg2.equalsIgnoreCase("name")) {
					
					if(arg3.equalsIgnoreCase("color")) {
						
						if(arg4.isEmpty()) {
							((Player) sender).sendMessage(ChatColor.RED + "Vous n'avez pas specife la couleur !");
							return false;
						}
						
						server.getPlayerManager().getPlayerOptions((Player) sender).setDefaultNameColor(arg4);
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
				else if(arg2.equalsIgnoreCase("display")) {
					
					if(arg3.equalsIgnoreCase("players")) {
						
						server.getPlayerManager().getPlayerOptions((Player) sender).setDisplayOnlinePlayers(!(server.getPlayerManager().getPlayerOptions((Player) sender).isOnlinePlayersDisplay()));
						return true;
					}
				}
			}
		}
		
		else if(base.equalsIgnoreCase("get") && (isOp || server.getPermanager().hasCommand((Player) sender, "get"))) {
			
			if(arg1.equalsIgnoreCase("itemid")) {
				
				if(arg2.isEmpty()) {
					((Player) sender).sendMessage(ChatColor.RED + "Veuillez indiquer l'ID !");
					return false;
				}
				
				((Player) sender).sendMessage(ChatColor.BLUE + "ID : " + arg2 + " Item : " + Utils.idToItem(arg2));
				return true;
			}
			
			else if(arg1.equalsIgnoreCase("chat")) {
				
				if(arg2.equalsIgnoreCase("name")) {
					
					if(arg3.equalsIgnoreCase("color")) {
						
						((Player) sender).sendMessage(Utils.formatMessage(server.getPlayerManager().getPlayerOptions((Player)sender).getDefaultNameColor() + "Couleur courante..."));
						return true;
					}
				}
				
				else if(arg2.equalsIgnoreCase("text")) {
					
					if(arg3.equalsIgnoreCase("color")) {
						
						((Player) sender).sendMessage(Utils.formatMessage(server.getPlayerManager().getPlayerOptions((Player)sender).getDefaultChatColor() + "Couleur courante..."));
						return true;
					}
				}
			}
		}
		
		else if(base.equalsIgnoreCase("time") && (isOp || server.getPermanager().hasCommand((Player) sender, "time"))) {
			
			if(arg1.equalsIgnoreCase("set")) {
				
				if(arg2.isEmpty()) {
					((Player) sender).sendMessage(ChatColor.RED + "Veuillez indiquer l'heure (de 0000 pour 7h00 jusqua 2400).");
					return false;
				}
				
				((Player) sender).getWorld().setTime(Integer.parseInt(arg2));
				return true;
			}
			
			else if(arg1.equalsIgnoreCase("get")) {
				
				((Player)sender).sendMessage(ChatColor.BLUE + "Il est " + Integer.toString((int)((Player) sender).getWorld().getTime()));
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("weather") && (isOp || server.getPermanager().hasCommand((Player) sender, "weather"))) {
			
			if(arg1.equalsIgnoreCase("set")) {
				
				if(arg2.equalsIgnoreCase("sun")) {
					
					((Player) sender).getWorld().setStorm(false);
					((Player) sender).getWorld().setThundering(false);
					return true;
				}
				
				else if(arg2.equalsIgnoreCase("storm")) {
					
					((Player) sender).getWorld().setStorm(true);
					return true;
				}
				
				else if(arg2.equalsIgnoreCase("thunder")) {
					
					((Player) sender).getWorld().setThundering(true);
					return true;
				}
			}
		}
		
		else if(base.equalsIgnoreCase("help") && (isOp || server.getPermanager().hasCommand((Player) sender, "help"))) {
			
			if(arg1.isEmpty()) {
				((Player) sender).sendMessage(ChatColor.GREEN + "Help message pour la commande help :");
				((Player) sender).sendMessage(ChatColor.YELLOW + "------------------------------------");
				((Player) sender).sendMessage(ChatColor.BLUE + "Help permet de connaitre l'usage et la description d'une fonction passee en parametre ( en tapant par exemple /help give ) ou de connaitre la liste des fonctions en tapant /help num, ou num est le numero de la page.");
				((Player) sender).sendMessage(ChatColor.RED + "Il y a actuellement " + Integer.toString(server.getpListener().getHelpPages()) + " pages.");
				((Player) sender).sendMessage(ChatColor.YELLOW + "------------------------------------");
				
				return true;
			}
			else if(server.getpListener().containsCommand(arg1)) {
				((Player) sender).sendMessage(ChatColor.GREEN + "Help message pour la commande " + arg1 + " :");
				((Player) sender).sendMessage(ChatColor.YELLOW + "---------------------------------------------");
				CommandHelp command = server.getpListener().getHelpCommand(arg1);
				if(command == null) {
					((Player) sender).sendMessage(ChatColor.RED + "La commade est inconnue !");
					((Player) sender).sendMessage(ChatColor.YELLOW + "----------------------------------------------");
					return false;
				}
				
				((Player) sender).sendMessage(ChatColor.BLUE + command.getDescription());
				((Player) sender).sendMessage(ChatColor.BLUE + "Usage : " + ChatColor.GOLD + command.getUsage());
				((Player) sender).sendMessage(ChatColor.YELLOW + "--------------------------------------------");
				
				return true;
			}
			else if(arg1.matches("[0-9]*")) {
				ArrayList<CommandHelp> listPage = server.getpListener().getHelpPage(Integer.parseInt(arg1));
				if(listPage.isEmpty()) {
					((Player) sender).sendMessage(ChatColor.RED + "La page specifiee n'existe pas !");
					return false;
				}
				
				((Player) sender).sendMessage(ChatColor.GREEN + "Page d'aide n¼ " + arg1 + " :");
				((Player) sender).sendMessage(ChatColor.YELLOW + "-----------------------------");
				for(CommandHelp command : listPage) {
					((Player) sender).sendMessage(ChatColor.BLUE + command.getName());
				}
				((Player) sender).sendMessage(ChatColor.YELLOW + "-----------------------------");
				return true;
			}
		}
		
		else if(base.equalsIgnoreCase("spawn") && (isOp || server.getPermanager().hasCommand((Player) sender, "spawn"))) {
			Location loc = server.getPlayerManager().getPlayerTarget((Player) sender);
			if(loc == null) {
				((Player) sender).sendMessage(ChatColor.RED + "La cible est soit trop loin soit inexistante (air) !");
				return false;
			}
			
			if(arg1.equalsIgnoreCase("mob")) {
				if(arg2.equalsIgnoreCase("chicken")) {
					 server.geteListener().spawmCreature(CreatureType.CHICKEN, loc);
				}
				else if(arg2.equalsIgnoreCase("cow")) {
					server.geteListener().spawmCreature(CreatureType.COW, loc);
				}
				else if(arg2.equalsIgnoreCase("pig")) {
					server.geteListener().spawmCreature(CreatureType.PIG, loc);
				}
				else if(arg2.equalsIgnoreCase("sheep")) {
					server.geteListener().spawmCreature(CreatureType.SHEEP, loc);
				}
				else if(arg2.equalsIgnoreCase("squid")) {
					server.geteListener().spawmCreature(CreatureType.SQUID, loc);
				}
				else if(arg2.equalsIgnoreCase("enderman")) {
					server.geteListener().spawmCreature(CreatureType.ENDERMAN, loc);
				}
				else if(arg2.equalsIgnoreCase("wolf")) {
					server.geteListener().spawmCreature(CreatureType.WOLF, loc);
				}
				else if(arg2.equalsIgnoreCase("zombie_pigman")) {
					server.geteListener().spawmCreature(CreatureType.PIG_ZOMBIE, loc);
				}
				else if(arg2.equalsIgnoreCase("cave_spider")) {
					server.geteListener().spawmCreature(CreatureType.CAVE_SPIDER, loc);
				}
				else if(arg2.equalsIgnoreCase("creeper")) {
					server.geteListener().spawmCreature(CreatureType.CREEPER, loc);
				}
				else if(arg2.equalsIgnoreCase("ghast")) {
					server.geteListener().spawmCreature(CreatureType.GHAST, loc);
				}
				else if(arg2.equalsIgnoreCase("silverfish")) {
					server.geteListener().spawmCreature(CreatureType.SILVERFISH, loc);
				}
				else if(arg2.equalsIgnoreCase("skeleton")) {
					server.geteListener().spawmCreature(CreatureType.SKELETON, loc);
				}
				else if(arg2.equalsIgnoreCase("slime")) {
					server.geteListener().spawmCreature(CreatureType.SLIME, loc);
				}
				else if(arg2.equalsIgnoreCase("spider")) {
					server.geteListener().spawmCreature(CreatureType.SPIDER, loc);
				}
				else if(arg2.equalsIgnoreCase("zombie")) {
					server.geteListener().spawmCreature(CreatureType.ZOMBIE, loc);
				}
				else if(arg2.equalsIgnoreCase("giant")) {
					server.geteListener().spawmCreature(CreatureType.GIANT, loc);
				}
				else {
					((Player) sender).sendMessage(ChatColor.RED + "Le mob specifie n'est pas reconnu !");
					return false;
				}
				
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
