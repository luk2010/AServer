package com.aserver;

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
}
