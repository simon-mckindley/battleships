package com.play.battleship.contollers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.play.battleship.models.Battle_ship;
import com.play.battleship.models.Carrier;
import com.play.battleship.models.Coords;
import com.play.battleship.models.Destroyer;
import com.play.battleship.models.PatrolBoat;
import com.play.battleship.models.Square;
import com.play.battleship.models.Submarine;
import com.play.battleship.services.BoardService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

	private List<Square> myBoard;
	private List<Square> oppBoard;

	private BoardService boardService;
	private Carrier carrier;
	private Battle_ship battleship;
	private Destroyer destroyer;
	private Submarine submarine;
	private PatrolBoat patrolBoat;

	public MainController(BoardService boardService, Carrier carrier, Battle_ship battleship, Destroyer destroyer,
			Submarine submarine, PatrolBoat patrolBoat) {
		this.boardService = boardService;
		this.carrier = carrier;
		this.battleship = battleship;
		this.destroyer = destroyer;
		this.submarine = submarine;
		this.patrolBoat = patrolBoat;

		this.oppBoard = this.boardService.getAIBoard();
	}

	@GetMapping("/place")
	public String getPlace() {
		return "place";
	}

	@PostMapping("/play")
	public String postPlay(HttpServletRequest request, Model model) {
		System.out.println("POST PLAY");
		myBoard = new ArrayList<Square>();
		for (char r = 'A'; r <= 'J'; r++) {
			for (int c = 1; c <= 10; c++) {
				myBoard.add(new Square(String.format("%c%d", r, c)));
			}
		}

		myBoard = boardService.SetBoard(myBoard, new Coords("Carrier", request.getParameter("carrier").toString()));
		myBoard = boardService.SetBoard(myBoard,
				new Coords("Battleship", request.getParameter("battleship").toString()));
		myBoard = boardService.SetBoard(myBoard, new Coords("Destroyer", request.getParameter("destroyer").toString()));
		myBoard = boardService.SetBoard(myBoard, new Coords("Submarine", request.getParameter("submarine").toString()));
		myBoard = boardService.SetBoard(myBoard, new Coords("Patrol-Boat", request.getParameter("patrol").toString()));

		model.addAttribute("myBoard", myBoard);
		return "play";
	}

	@GetMapping("/play")
	public String getPlay(Model model) {
		model.addAttribute("myBoard", myBoard);
		return "play";
	}

	@PostMapping("/shoot")
	public String postShoot(@RequestParam String shot, Model model) {
		shot = shot.toUpperCase();
		System.out.println("Shot made on: " + shot);
		int index = boardService.findSquare(shot, oppBoard);
		Square sq = oppBoard.get(index);
		sq.setShotMade();
		if (sq.isOccupied()) {
			System.out.println("Hit on " + sq.getOccupied());

			switch (sq.getOccupied()) {
			case "C":
				carrier.addHit();
				break;
			case "B":
				battleship.addHit();
				break;
			case "D":
				destroyer.addHit();
				break;
			case "S":
				submarine.addHit();
				break;
			case "P":
				patrolBoat.addHit();
				break;
			}
		} else {
			System.out.println("Miss");
		}

		return "redirect:play";
	}

}
