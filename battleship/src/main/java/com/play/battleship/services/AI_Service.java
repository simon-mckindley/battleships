package com.play.battleship.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.play.battleship.models.Battle_ship;
import com.play.battleship.models.Carrier;
import com.play.battleship.models.Coords;
import com.play.battleship.models.Destroyer;
import com.play.battleship.models.PatrolBoat;
import com.play.battleship.models.Square;
import com.play.battleship.models.Submarine;

@Service
public class AI_Service {

	private BoardService boardService;

	public AI_Service(BoardService boardService) {
		this.boardService = boardService;
	}

	public List<Square> getAIBoard() {
		final int BOARD_SIZE = 10;
		Random rand = new Random();
		String[] ships = { "Carrier", "Battleship", "Destroyer", "Submarine", "Patrol-Boat" };
		int length = 0;

		List<Square> board = new ArrayList<Square>();
		for (char r = 'A'; r <= ('A' + BOARD_SIZE - 1); r++) {
			for (int c = 1; c <= BOARD_SIZE; c++) {
				board.add(new Square(String.format("%c%d", r, c)));
			}
		}

		for (String ship : ships) {
			boolean shipSet = false;

			switch (ship) {
			case "Carrier":
				length = Carrier.LENGTH;
				break;
			case "Battleship":
				length = Battle_ship.LENGTH;
				break;
			case "Destroyer":
				length = Destroyer.LENGTH;
				break;
			case "Submarine":
				length = Submarine.LENGTH;
				break;
			case "Patrol-Boat":
				length = PatrolBoat.LENGTH;
				break;
			}

			do {
				int endIndex;
				int startIndex = rand.nextInt(board.size());
				int direction = rand.nextInt(2);
				Square start = board.get(startIndex);
				Square end;

				if (direction == 1) {
					// Down
					endIndex = startIndex + ((length - 1) * BOARD_SIZE);
					if (endIndex >= board.size()) {
						continue;
					}
					end = board.get(endIndex);
					System.out.println(ship + " Down " + start.getName() + ":" + end.getName());
					if (start.getName().charAt(1) != end.getName().charAt(1)) {
						continue;
					}
				} else {
					// Right
					endIndex = startIndex + length - 1;
					if (endIndex >= board.size()) {
						continue;
					}
					end = board.get(endIndex);
					System.out.println(ship + " Right " + start.getName() + ":" + end.getName());
					if (start.getName().charAt(0) != end.getName().charAt(0)) {
						continue;
					}
				}

				try {
					board = boardService.SetBoard(board, new Coords(ship, start.getName() + "-" + end.getName()));
					shipSet = true;
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}

			} while (!shipSet);
		}

		return board;
	}

}
