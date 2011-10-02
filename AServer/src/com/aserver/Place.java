package com.aserver;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.World;

public class Place implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 396741777140870902L;
	private int id;
	private String name;
	private double x;
	private double y;
	private double z;
	
	public Place() {
		name = "";
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Place(double x, double y, double z, String name) {
		this.setName(name);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public Location toLocation(World w) {
		return new Location(w, x, y, z);
	}
}
