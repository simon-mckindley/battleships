package com.play.battleship.models;

public class Coords {
	
	String name;
	String coords;
	
	public Coords (String name, String coords) {
		this.name = name;
		this.coords = coords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoords() {
		return coords;
	}

	public void setCoords(String coords) {
		this.coords = coords;
	}
	
	public String getStart() {
		return coords.split("-")[0];
	}
	
	public String getEnd() {
		return coords.split("-")[1];
	}

}
