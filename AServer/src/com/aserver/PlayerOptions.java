package com.aserver;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerOptions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String player;
	private boolean debugging = false;
	private boolean godmode = false;
	private String defaultChatColor = "&white";
	private String defaultNameColor = "&white";
	private boolean displayOnlinePlayers = false;
	
	private ArrayList<Place> homes = new ArrayList<Place>();
	private ArrayList<Place> tpoints = new ArrayList<Place>();
	
	public Place getHome(String mainHomeName) {
		if(mainHomeName.isEmpty())
			return null;
		
		for(Place place : homes) {
			if(place.getName().equals(mainHomeName)) {
				return place;
			}
		}
		
		return null;
	}
	
	public void addHome(Place home) {
		if(getHome(home.getName()) == null)
			this.homes.add(home);
		else
			this.homes.set(this.findHome(home), home);
	}

	public int findHome(Place home) {
		for(int i = 0; i < homes.size(); i++) {
			Place place = homes.get(i);
			if(place.getName().equals(home.getName()))
				return i;
		}
		return -1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public boolean isDebugging() {
		return debugging;
	}

	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

	public boolean isGodmode() {
		return godmode;
	}

	public void setGodmode(boolean godmode) {
		this.godmode = godmode;
	}

	public ArrayList<Place> getTpoints() {
		return tpoints;
	}

	public void setTpoints(ArrayList<Place> tpoints) {
		this.tpoints = tpoints;
	}
	
	public ArrayList<Place> getHomes() {
		return this.homes;
	}
	
	public void setHomes(ArrayList<Place> homes) {
		this.homes = homes;
	}

	public Place getTPoint(String tPointName) {
		if(tPointName.isEmpty())
			return null;
		
		for(Place place : tpoints) {
			if(place.getName().equals(tPointName))
				return place;
		}
		
		return null;
	}

	public void addTPoint(Place place) {
		if(getTPoint(place.getName()) == null) 
			this.tpoints.add(place);
		else
			this.tpoints.set(this.findTPoint(place), place);
	}

	public int findTPoint(Place place) {
		for(int i = 0; i < tpoints.size(); i++) {
			Place place1 = tpoints.get(i);
			if(place1.getName().equals(place.getName()))
				return i;
		}
		return -1;
	}

	public String getDefaultChatColor() {
		return defaultChatColor;
	}

	public void setDefaultChatColor(String defaultChatColor) {
		this.defaultChatColor = defaultChatColor;
	}

	public String getDefaultNameColor() {
		return defaultNameColor;
	}

	public void setDefaultNameColor(String defaultNameColor) {
		this.defaultNameColor = defaultNameColor;
	}

	public boolean isOnlinePlayersDisplay() {
		return displayOnlinePlayers;
	}

	public void setDisplayOnlinePlayers(boolean displayOnlinePlayers) {
		this.displayOnlinePlayers = displayOnlinePlayers;
	}
}
