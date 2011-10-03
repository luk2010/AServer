package com.aserver;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class AEntityListener extends EntityListener {
	private final AServer server;
	
	public AEntityListener(final AServer server) {
		this.server = server;
	}
	
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && server.getPlayerManager().getPlayerOptions((Player)e.getEntity()).isGodmode()) {
			final Player player = (Player) e.getEntity();
			player.setFireTicks(0);
			player.setFoodLevel(20);
			player.setHealth(20);
		}
	}

	public AServer getServer() {
		return server;
	}
	
	public void spawmCreature(CreatureType type, Location loc) {
		ACreature creature = new ACreature();
		creature.setType(type);
		creature.spawn(loc);
	}
	
	public ArrayList<Entity> findNearbyEntity(Player player, double radius) {
		List<Entity> entities = player.getWorld().getEntities();
		ArrayList<Entity> nentities = new ArrayList<Entity>();
		
		for(Entity entity : entities) {
			double distance = entity.getLocation().distanceSquared(player.getLocation());
			if(distance <= radius) {
				nentities.add(entity);
			}
		}
		
		return nentities;
	}
}
