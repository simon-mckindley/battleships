package com.play.battleship.models;

public class Square {

	private String name;
	private String occupied;
	private boolean shotMade;

	public Square(String name) {
		this.name = name;
		this.occupied = "-";
		this.shotMade = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOccupied() {
		return this.occupied;
	}

	public void setOccupied(String type) throws IllegalArgumentException {
		if (this.occupied.equals("-")) {
			this.occupied = type;
		} else {
			throw new IllegalArgumentException("Space already occupied.");
		}
	}

	public boolean isOccupied() {
		return !this.occupied.equals("-");
	}

	public boolean getShotMade() {
		return this.shotMade;
	}

	public void setShotMade() {
		this.shotMade = true;
	}

}
