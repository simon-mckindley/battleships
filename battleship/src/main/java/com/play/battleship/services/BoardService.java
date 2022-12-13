package com.play.battleship.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.play.battleship.models.Coords;
import com.play.battleship.models.Square;

@Service
public class BoardService {

	public List<Square> SetBoard(List<Square> board, Coords coords) {
		
		System.out.println("Coords: " + coords.getName() + " Start: "+ coords.getStart() + " End: " + coords.getEnd());

		if (coords.getStart().charAt(0) == coords.getEnd().charAt(0)) {
			int a = 0;
			String rowIndex = coords.getStart().substring(0, 1);
			int colIndex = Integer.parseInt(coords.getStart().substring(1));
			String key = "";
			do {
				int newCol = colIndex + a;
				key = (rowIndex + Integer.toString(newCol));
				Square sq = new Square(key);
				sq.setOccupied(coords.getName().charAt(0) + "");
				int index = findSquare(key, board);
				board.set(index, sq);
				a++;
			} while (!coords.getEnd().equals(key) && a <= 10);
		}

		if (coords.getStart().charAt(1) == coords.getEnd().charAt(1)) {
			int a = 0;
			char rowIndex = coords.getStart().charAt(0);
			String colIndex = coords.getStart().substring(1);
			String key = "";
			do {
				char newRow = (char) (rowIndex + a);
				key = (newRow + colIndex);
				Square sq = new Square(key);
				sq.setOccupied(coords.getName().charAt(0) + "");
				int index = findSquare(key, board);
				board.set(index, sq);
				a++;
			} while (!coords.getEnd().equals(key) && a <= 10);
		}

		return board;
	}
	
	public int findSquare (String key, List<Square> board) {
		int a = -1;
		for (int i = 0; i < board.size(); i++) {
			if (key.equals(board.get(i).getName())){
				a = i;
				break;
			}
		}
		return a;
	}
	
	public List<Square> getAIBoard () {
		List<Square> board = new ArrayList<Square>();
		for (char r = 'A'; r <= 'J'; r++) {
			for (int c = 1; c <= 10; c++) {
				board.add(new Square(String.format("%c%d", r, c)));
			}
		}
		
		board = SetBoard(board, new Coords("Carrier", "B2-B6"));
		board = SetBoard(board, new Coords("Battleship", "D3-G3"));
		board = SetBoard(board, new Coords("Destroyer", "E6-E8"));
		board = SetBoard(board, new Coords("Submarine", "H8-H10"));
		board = SetBoard(board, new Coords("Patrol-Boat", "I2-I3"));
		
		return board;
	}

}
