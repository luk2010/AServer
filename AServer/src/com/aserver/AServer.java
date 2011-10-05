package com.aserver;

import java.io.File;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AServer extends JavaPlugin {
	
	private PlayerManager playerManager = new PlayerManager(this);
	private ACommandExecutor cmdExecutor = new ACommandExecutor(this);
	private AEntityListener eListener = new AEntityListener(this);
	private ABlockListener bListener = new ABlockListener(this);
	private APluginListener pListener = new APluginListener(this);
	private PermissionManager permanager = new PermissionManager(this);

	@Override
	public void onDisable() {
		Utils.log(Utils.pName() + " Desactivation...");	
		
		permanager.save();
		
		Utils.log(Utils.pName() + " Termine !");
	}

	@Override
	public void onEnable() {
		
		Utils.log(Utils.pName() + " Activation des fonctions principales...");
		PluginDescriptionFile pdf = this.getDescription();
		Utils.log(Utils.pName() + " Version utilisee : " + pdf.getVersion());
		
		if(!this.getDataFolder().exists()) {
			Utils.log(Utils.pName() + " Creation du dossier " + this.getDataFolder().getName() + "...");
			this.getDataFolder().mkdir();
		}
		
		File datasFolder = new File(this.getDataFolder(), "playerOptions");
		if(!datasFolder.exists()) {
			Utils.log(Utils.pName() + "Creating player folder...");
			datasFolder.mkdir();
		}
		
		File permissionsFolder = new File(this.getDataFolder(), "permissions");
		if(!permissionsFolder.exists()) {
			Utils.log(Utils.pName() + " Creationd ud dossier des permissions des mondes.");
			permissionsFolder.mkdir();
		}
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, pListener, Event.Priority.Normal, this);
		
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerManager, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerManager, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerManager, Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerManager, Event.Priority.Highest, this);
		
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, eListener, Event.Priority.Normal, this);
		
		pm.registerEvent(Event.Type.BLOCK_PLACE, bListener, Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, bListener, Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.BLOCK_PHYSICS, bListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_CANBUILD, bListener, Event.Priority.Normal, this);
		
		this.getCommand("debug").setExecutor(cmdExecutor);
		this.getCommand("sethome").setExecutor(cmdExecutor);
		this.getCommand("home").setExecutor(cmdExecutor);
		this.getCommand("setTPoint").setExecutor(cmdExecutor);
		this.getCommand("goTPoint").setExecutor(cmdExecutor);
		this.getCommand("tp").setExecutor(cmdExecutor);
		this.getCommand("god").setExecutor(cmdExecutor);
		this.getCommand("list").setExecutor(cmdExecutor);
		this.getCommand("give").setExecutor(cmdExecutor);
		this.getCommand("set").setExecutor(cmdExecutor);
		this.getCommand("get").setExecutor(cmdExecutor);
		this.getCommand("time").setExecutor(cmdExecutor);
		this.getCommand("weather").setExecutor(cmdExecutor);
		this.getCommand("help").setExecutor(cmdExecutor);
		this.getCommand("spawn").setExecutor(cmdExecutor);
		
		permanager.init();
		
		Utils.log(Utils.pName() + " Activation terminee !");
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public ACommandExecutor getCmdExecutor() {
		return cmdExecutor;
	}

	public void setCmdExecutor(ACommandExecutor cmdExecutor) {
		this.cmdExecutor = cmdExecutor;
	}

	public ABlockListener getbListener() {
		return bListener;
	}

	public void setbListener(ABlockListener bListener) {
		this.bListener = bListener;
	}
	
	public void seteListener(AEntityListener eListener) {
		this.eListener = eListener;
	}
	
	public AEntityListener geteListener() {
		return eListener;
	}

	public APluginListener getpListener() {
		return pListener;
	}

	public void setpListener(APluginListener pListener) {
		this.pListener = pListener;
	}
	
	public PermissionManager getPermanager() {
		return permanager;
	}

}
