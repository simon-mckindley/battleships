package com.play.battleship.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.play.battleship.models.Coords;
import com.play.battleship.models.Square;

@Service
public class BoardService {

	public List<Square> SetBoard(List<Square> board, Coords coords) throws IllegalArgumentException {

		System.out.println("Coords: " + coords.getName() + " Start: " + coords.getStart() + " End: " + coords.getEnd());
		char rowIndex = coords.getStart().charAt(0);
		int colIndex = Integer.parseInt(coords.getStart().substring(1));
		char maxRow = board.get(board.size() - 1).getName().charAt(0);
		int maxCol = board.get(board.size() - 1).getName().charAt(1);

		if ((rowIndex < 'A' || colIndex < 0) || (rowIndex > maxRow || colIndex > maxCol)) {
			throw new IllegalArgumentException("Coords out of bounds - {ends}");
		}

		if (coords.getStart().charAt(0) == coords.getEnd().charAt(0)) {
			int a = 0;

			String key = "";
			do {
				int newCol = colIndex + a;
				key = (rowIndex + Integer.toString(newCol));
				Square sq = new Square(key);
				sq.setOccupied(coords.getName().charAt(0) + "");
				System.out.println("Key: " + key);
				int index = findSquare(key, board);
				board.set(index, sq);
				a++;
			} while (!coords.getEnd().equals(key) && a <= 10);
		} else if (coords.getStart().charAt(1) == coords.getEnd().charAt(1)) {
			int a = 0;

			String key = "";
			do {
				char newRow = (char) (rowIndex + a);
				key = (newRow + Integer.toString(colIndex));
				Square sq = new Square(key);
				sq.setOccupied(coords.getName().charAt(0) + "");
				System.out.println("Key: " + key);
				int index = findSquare(key, board);
				board.set(index, sq);
				a++;
			} while (!coords.getEnd().equals(key) && a <= 10);
		} else {
			throw new IllegalArgumentException("Coords out of bounds - {across}");
		}

		return board;
	}

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
