package com.aserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerManager extends PlayerListener {
	
	private HashMap<Player, PlayerOptions> players = new HashMap<Player, PlayerOptions>();
	private AServer server;
	
	public PlayerManager(AServer server)
	{
		this.setServer(server);
	}
	
	public Player getPlayer(String name) {
		for(Player player : players.keySet()) {
			if(player.getName().equals(name))
				return player;
		}
		
		return null;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(!players.containsKey(event.getPlayer())) {
			PlayerOptions po = this.load(event.getPlayer());
			if(po == null) {
				players.put(event.getPlayer(), new PlayerOptions());
				players.get(event.getPlayer()).setPlayer(event.getPlayer().getName());
				this.send(event.getPlayer(), ChatColor.BLUE + "Bienvenue " + event.getPlayer().getDisplayName() + " sur le serveur !");
			}
			else {
				players.put(event.getPlayer(), po);
			}
			
			if(players.get(event.getPlayer()).isOnlinePlayersDisplay()) {
				this.send(event.getPlayer(), ChatColor.BLUE + "Liste des joueurs en ligne : ");
				String players_online = "";
				for(Player player : players.keySet()) {
					players_online += player.getDisplayName() + ", ";
				}
				this.send(event.getPlayer(), ChatColor.GOLD + players_online + ".");
			}
		} else {
			Utils.log(Utils.pName() + " Attention, le joueur " + event.getPlayer().getDisplayName() + " s'est connecte plusieurs fois en meme temps !");
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		if(players.containsKey(event.getPlayer())) {
			this.save(event.getPlayer());
			players.remove(event.getPlayer());
		}
		else
		{
			Utils.log(Utils.pName() + " Impossible de retrouver le joueur " + event.getPlayer().getDisplayName() + " dans la liste des joueurs !");
		}
	}

	public AServer getServer() {
		return server;
	}

	public void setServer(AServer server) {
		this.server = server;
	}

	public void onPlayerChangeDebugging(Player sender) {
		if(players.containsKey(sender)) {
			players.get(sender).setDebugging(!(players.get(sender).isDebugging()));
			
			if(players.get(sender).isDebugging()) {
				this.send(sender,ChatColor.BLUE + "Vous etes passe en mode debug !");
			}
			else {
				this.send(sender,ChatColor.BLUE + "Vous avez quitte le mode debug !");
			}
		}
	}

	public void send(Player sender, String string) {
		sender.sendMessage(string);
	}

	public void onPlayerChangeMainHome(Player sender) {
		if(players.containsKey(sender)) {
			if(!players.get(sender).getHomes().isEmpty()) {
				players.get(sender).getHomes().set(0, new Place(sender.getLocation().getX(), 
															sender.getLocation().getY() + 1,
															sender.getLocation().getZ(), 
															this.getMainHomeName()));
			}
			else {
				players.get(sender).getHomes().add(new Place(sender.getLocation().getX(), 
						sender.getLocation().getY() + 1,
						sender.getLocation().getZ(), 
						this.getMainHomeName()));
			}
		}
	}
	
	public void onPlayerChangeHome(Player sender, String name) {
		if(players.containsKey(sender)) {
			players.get(sender).addHome(new Place(sender.getLocation().getX(),
												  sender.getLocation().getY(),
												  sender.getLocation().getZ(),
												  name));
		}
	}
	
	public void onPlayerChangeTPoint(Player sender, String name) {
		if(players.containsKey(sender)) {
			players.get(sender).addTPoint(new Place(sender.getLocation().getX(),
					  					  sender.getLocation().getY(),
					  					  sender.getLocation().getZ(),
					  					  name));
		}
	}
	
	public String getMainHomeName() {
		return "main_0XFFNOONECANTRUSTMEWITHTHIS";
	}

	public void teleportHome(final Player sender, final String mainHomeName) {
		if(players.get(sender).getHome(mainHomeName) == null) {
			this.send(sender, ChatColor.RED + "Impossible de trouver la maison " + mainHomeName);
			return;
		}
		
		this.teleportPlace(sender, players.get(sender).getHome(mainHomeName));
	}
	
	public void teleportTPoint(final Player sender, final String TPointName) {
		if(players.get(sender).getTPoint(TPointName) == null) {
			this.send(sender, ChatColor.RED + "Impossible de trouver le point de teleportation " + TPointName);
			return;
		}
		
		this.teleportPlace(sender, players.get(sender).getTPoint(TPointName));
	}
	
	public void teleportPlace(final Player sender, final Place place) {
		server.getServer().getScheduler().scheduleSyncDelayedTask(server, new Runnable() {

			@Override
			public void run() {
				sender.teleport(place.toLocation(sender.getWorld()));
			}
			
		});
	}
	
	public void teleportPlayer(final Player src, final Player dest) {
		server.getServer().getScheduler().scheduleSyncDelayedTask(server, new Runnable() {

			@Override
			public void run() {
				src.teleport(dest);
			}
			
		});
	}
	
	public PlayerOptions load(Player p) {
		String optionsPath = server.getDataFolder() + "/playerOptions";
		File playerOption = new File(optionsPath + "/" + p.getName() + ".option");
		
		try {
			if(!playerOption.exists()) {
				return null;
			}
			
			FileInputStream fis = new FileInputStream(playerOption);
			ObjectInputStream ois = new ObjectInputStream(fis);
			PlayerOptions optionFF = (PlayerOptions) ois.readObject();
			ois.close();
			
			playerOption.delete();
			
			return optionFF;
		} catch (Exception e) {
			e.printStackTrace();
			Utils.log(Utils.pName() + "Impossible de retrouver les donnees de " + p.getName() + " !");
			return null;
		}
	}
	
	public void save(Player p) {
		if(players.get(p) == null)
			return;
		
		String optionsPath = server.getDataFolder() + "/playerOptions";
		File playerOption = new File(optionsPath + "/" + p.getName() + ".option");
		
		if(playerOption.exists()) {
			playerOption.delete();
		}
		
		try {
			playerOption.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(playerOption);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(players.get(p));
			oos.close();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.log(Utils.pName() + "Impossible de sauvegarder les donnees de " + p.getName() + " !");
		}
	}

	public void onPlayerChangeGodMode(Player sender) {
		if(players.containsKey(sender)) {
			players.get(sender).setGodmode(!(players.get(sender).isGodmode()));
			if(players.get(sender).isDebugging()) {
				if(players.get(sender).isGodmode()) {
					send(sender, ChatColor.BLUE + "Vous etes en mode invincible !");
				}
				else
					send(sender, ChatColor.BLUE + "Vous n'etes plus en mode invincible !");
			}
		}
	}
	
	public PlayerOptions getPlayerOptions(Player player) {
		if(players.containsKey(player))
			return players.get(player);
		else
			return null;
	}
	
	public Set<Player> getPlayers() {
		return players.keySet();
	}
	
	public void onPlayerChat(PlayerChatEvent e) {
		String msg = "[" + this.getPlayerOptions(e.getPlayer()).getDefaultNameColor() + e.getPlayer().getDisplayName() + "&white ] " + this.getPlayerOptions(e.getPlayer()).getDefaultChatColor() + e.getMessage();
		for(Player player : players.keySet()) {
			
			player.sendMessage(Utils.formatMessage(msg));
			
		}
		e.setCancelled(true);
	}
}
