package com.play.battleship.models;

public class PatrolBoat {

	public final static int LENGTH = 2;
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
