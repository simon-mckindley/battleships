package com.play.battleship.models;

public class Battle_ship {

	public final static int LENGTH = 4;
	private int hits = 0;

	public int getHits() {
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
		return hits == LENGTH;
	}
}
