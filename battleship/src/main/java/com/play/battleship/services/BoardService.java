package com.play.battleship.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.play.battleship.models.Coords;
import com.play.battleship.models.Square;

@Service
public class BoardService {

	/**
	 * Sets the ship into the board positions given
	 * @param board Board to try placement on
	 * @param coords Co-ords to try placement
	 * @return The board with new ship placed
	 * @throws IllegalArgumentException if the ship cannot be placed in the given positions
	 */
	public List<Square> SetBoard(List<Square> board, Coords coords) throws IllegalArgumentException {

		System.out.println("Coords: " + coords.getName() + " Start: " + coords.getStart() + " End: " + coords.getEnd());
		char rowIndex = coords.getStart().charAt(0);
		int colIndex = Integer.parseInt(coords.getStart().substring(1));
		char maxRow = board.get(board.size() - 1).getName().charAt(0);
		int maxCol = board.get(board.size() - 1).getName().charAt(1);
		List<Integer> indexes = new ArrayList<>();

		if ((rowIndex < 'A' || colIndex < 0) || (rowIndex > maxRow || colIndex > maxCol)) {
			throw new IllegalArgumentException("Coords out of bounds - {ends}");
		}

		if (coords.getStart().charAt(0) == coords.getEnd().charAt(0)) {
			int a = 0;

			String key = "";
			do {
				int newCol = colIndex + a;
				key = (rowIndex + Integer.toString(newCol));
				int index = findSquare(key, board);
				if (board.get(index).isOccupied()) {
					throw new IllegalArgumentException("Space is occupied");
				} else {
					indexes.add(index);
				}
				a++;
			} while (!coords.getEnd().equals(key));

		} else if (coords.getStart().charAt(1) == coords.getEnd().charAt(1)) {
			int a = 0;

			String key = "";
			do {
				char newRow = (char) (rowIndex + a);
				key = (newRow + Integer.toString(colIndex));
				int index = findSquare(key, board);
				if (board.get(index).isOccupied()) {
					throw new IllegalArgumentException("Space is occupied");
				} else {
					indexes.add(index);
				}
				a++;
			} while (!coords.getEnd().equals(key));
		} else {
			throw new IllegalArgumentException("Coords out of bounds - {across}");
		}

		for (Integer index : indexes) {
			Square sq = board.get(index);
			sq.setOccupied(coords.getName().charAt(0) + "");
			board.set(index, sq);
		}

		return board;
	}

	/**
	 * Finds the index number of the given square
	 * @param key Co-ord to look for
	 * @param board Board to look on
	 * @return Index number
	 */
	public int findSquare(String key, List<Square> board) {
		int a = -1;
		for (int i = 0; i < board.size(); i++) {
			if (key.equals(board.get(i).getName())) {
				a = i;
				break;
			}
		}
		return a;
	}

}
