package com.aserver;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ABlockListener extends BlockListener {
	
	private final AServer server;
	private HashSet<Material> tablePlate;
	private HashSet<Material> tableBase;
	
	public ABlockListener(final AServer server) {
		this.server = server;
		
		tablePlate = new HashSet<Material>();
		tablePlate.add(Material.WOOD_PLATE);
		tablePlate.add(Material.STONE_PLATE);
		
		tableBase = new HashSet<Material>();
		tableBase.add(Material.FENCE);
		tableBase.add(Material.GLASS);
	}
	
	public void onBlockCanBuild(BlockCanBuildEvent e) {
		Material placing = e.getMaterial();
		Block theBlock = e.getBlock();
		
		if(tablePlate.contains(placing)) {
			Block belowBlock = theBlock.getRelative(BlockFace.DOWN);
			if(theBlock.getType().equals(Material.AIR)) {
				if(tableBase.contains(belowBlock.getType())) {
					e.setBuildable(true);
				}
			}
		}
	}
	
	public void onBlockPhysics(BlockPhysicsEvent e) {
		Block affected = e.getBlock();
		
		if(tablePlate.contains(affected)) {
			Material below = affected.getRelative(BlockFace.DOWN).getType();
			if(tableBase.contains(below)) {
				e.setCancelled(true);
			}
		}
	}
	
	public void onBlockPlace(BlockPlaceEvent e) {
		e.setBuild(server.getPermanager().canBuild(e.getPlayer()));
	}
	
	public void onBlockBreak(BlockBreakEvent e) {
		if(!server.getPermanager().canBuild(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	public AServer getServer() {
		return server;
	}
}
