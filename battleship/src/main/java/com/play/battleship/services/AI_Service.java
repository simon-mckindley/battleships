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

	private final BoardService boardService;

	Random rand = new Random();
	final int BOARD_SIZE = 10;

	public AI_Service(BoardService boardService) {
		this.boardService = boardService;
	}

	/*
	 * Finds the ship positions on the board for the AI player
	 */
	public List<Square> getAIBoard() {
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
				case "Carrier" -> length = Carrier.LENGTH;
				case "Battleship" -> length = Battle_ship.LENGTH;
				case "Destroyer" -> length = Destroyer.LENGTH;
				case "Submarine" -> length = Submarine.LENGTH;
				case "Patrol-Boat" -> length = PatrolBoat.LENGTH;
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

	/**
	 * AI-Easy shot making
	 * 
	 * @param board Players board
	 * @return Square made shot on
	 */
	public Square aiShot_easy(List<Square> board) {
		Square sq;

		do {
			int index = rand.nextInt(0, BOARD_SIZE * BOARD_SIZE);
			sq = board.get(index);
		} while (sq.getShotMade());

		return sq;
	}

	/**
	 * AI-Hard shot making algorithm
	 * 
	 * @param board Players board
	 * @param lastHit Last hit location
	 * @param carrier Players carrier
	 * @param battleship Players battleship
	 * @param destroyer Players destroyer
	 * @param submarine Players sub
	 * @param patrolBoat Players patrol boat
	 * @return Square made shot on
	 */
	public Square aiShot_hard(List<Square> board, Square lastHit, Carrier carrier, Battle_ship battleship,
			Destroyer destroyer, Submarine submarine, PatrolBoat patrolBoat) {
		int unsunk_hits = 0;
		int length = 0;
		int index = 0;

		if (carrier.getHits() > 0 && !carrier.sunk()) {
			unsunk_hits = carrier.getHits();
			length = Carrier.LENGTH;
		} else if (battleship.getHits() > 0 && !battleship.sunk()) {
			unsunk_hits = battleship.getHits();
			length = Battle_ship.LENGTH;
		} else if (destroyer.getHits() > 0 && !destroyer.sunk()) {
			unsunk_hits = destroyer.getHits();
			length = Destroyer.LENGTH;
		} else if (submarine.getHits() > 0 && !submarine.sunk()) {
			unsunk_hits = submarine.getHits();
			length = Submarine.LENGTH;
		} else if (patrolBoat.getHits() > 0 && !patrolBoat.sunk()) {
			unsunk_hits = patrolBoat.getHits();
			length = PatrolBoat.LENGTH;
		}

		// If there is a hit & unsunk ship
		if (unsunk_hits > 0) {
			index = boardService.findSquare(lastHit.getName(), board);
			// If the ship has been hit once
			if (unsunk_hits == 1) {
				// Check enough space horizontal
				if (spaceHorizontal(board, lastHit, length)) {
					if (index + 1 < board.size()
							&& (board.get(index + 1).getName().charAt(0) == lastHit.getName().charAt(0))
							&& !board.get(index + 1).getShotMade()) {
						index = index + 1;
					} else {
						index = index - 1;
					}
				} else {
					if (index - 10 >= 0 && !board.get(index - 10).getShotMade()) {
						index = index - 10;
					} else {
						index = index + 10;
					}
				}
				// If the ship has been hit more than once
			} else {
				// Check horizontal alignment
				if (alignmentHorizontal(board, lastHit)) {
					if (index + 1 < board.size()
							&& (board.get(index + 1).getName().charAt(0) == lastHit.getName().charAt(0))
							&& !board.get(index + 1).getShotMade()) {
						index = index + 1;
					} else {
						for (int i = 1; i < 100; i++) {
							if (!board.get(index - i).getShotMade()) {
								index = index - i;
								break;
							}
						}
					}
				} else {
					if (index - 10 >= 0 && !board.get(index - 10).getShotMade()) {
						index = index - 10;
					} else {
						for (int i = 10; i < 100; i += 10) {
							if (!board.get(index + i).getShotMade()) {
								index = index + i;
								break;
							}
						}
					}
				}
			}

		} else {

			// Tries to find a random square two away from previous shots (50 tries)
			boolean valid;
			int a = 0;
			do {
				valid = true;
				index = rand.nextInt(0, BOARD_SIZE * BOARD_SIZE);
				if (board.get(index).getShotMade()) {
					valid = false;
				} else if ((index - 10 >= 0) && board.get(index - 10).getShotMade()) {
					valid = false;
				} else if ((index + 10 < board.size()) && board.get(index + 10).getShotMade()) {
					valid = false;
				} else if ((index - 1 >= 0) && board.get(index - 1).getShotMade()) {
					valid = false;
				} else if ((index + 1 < board.size()) && board.get(index + 1).getShotMade()) {
					valid = false;
				}

				a++;
			} while (!valid && a < 50);

			// Otherwise, any other space
			while (board.get(index).getShotMade()) {
				index = rand.nextInt(0, BOARD_SIZE * BOARD_SIZE);
			}
		}

		return board.get(index);

	}

	/**
	 * Finds if there is horizontal space for the ship on the board
	 * 
	 * @param board Players board
	 * @param lastHit Last hit location
	 * @param length Length of the ship last hit
	 * @return True if there is horizontal space
	 */
	private boolean spaceHorizontal(List<Square> board, Square lastHit, int length) {
		final char rowCoord = lastHit.getName().charAt(0);
		int spaces = 0;
		int index = boardService.findSquare(lastHit.getName(), board) + 1;

		while ((index < board.size()) && !board.get(index).getShotMade()
				&& (board.get(index).getName().charAt(0) == rowCoord)) {
			spaces++;
			index++;
		}

		index = boardService.findSquare(lastHit.getName(), board) - 1;

		while ((index > 0) && !board.get(index).getShotMade() && (board.get(index).getName().charAt(0) == rowCoord)) {
			spaces++;
			index--;
		}

		return spaces >= length - 1;
	}

	/**
	 * Finds if the alignment of the ship is horizontal after more than one hit
	 * 
	 * @param board Players board
	 * @param lastHit Last hit location
	 * @return True if the ship is aligned horizontally
	 */
	private boolean alignmentHorizontal(List<Square> board, Square lastHit) {
		int index = boardService.findSquare(lastHit.getName(), board);

		if ((index + 1 < board.size()) && (board.get(index + 1).getName().charAt(0) == lastHit.getName().charAt(0))
				&& board.get(index + 1).getShotMade() && board.get(index + 1).isOccupied()) {
			return true;
		}

		return (index - 1 >= 0) && (board.get(index - 1).getName().charAt(0) == lastHit.getName().charAt(0))
				&& board.get(index - 1).getShotMade() && board.get(index - 1).isOccupied();
	}

}
