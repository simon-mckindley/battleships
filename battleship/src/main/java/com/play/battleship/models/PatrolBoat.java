package com.play.battleship.models;

public class PatrolBoat {
	
	private int hits = 0;

	public int getHits () {
		return hits;
	}
	
	public boolean addHit() {
		if (sunk()) {
			return true;
		}
		
		hits++;
		return sunk();
	}
	
	public boolean sunk() {
		return hits == 2;
	}
}
