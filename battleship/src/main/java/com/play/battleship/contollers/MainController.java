package com.play.battleship.contollers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.play.battleship.services.AI_Service;
import com.play.battleship.services.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	private List<Square> myBoard;
	private List<Square> oppBoard;

	private BoardService boardService;
	private AI_Service ai_service;
	private Carrier oppCarrier = new Carrier();
	private Battle_ship oppBattleship = new Battle_ship();
	private Destroyer oppDestroyer = new Destroyer();
	private Submarine oppSubmarine = new Submarine();
	private PatrolBoat oppPatrolBoat = new PatrolBoat();
	private Carrier myCarrier = new Carrier();
	private Battle_ship myBattleship = new Battle_ship();
	private Destroyer myDestroyer = new Destroyer();
	private Submarine mySubmarine = new Submarine();
	private PatrolBoat myPatrolBoat = new PatrolBoat();
	private int[] hits = new int[5];
	private boolean haveWinner;
	private String winnerName = "";

	public MainController(BoardService boardService, AI_Service ai_service) {
		this.boardService = boardService;
		this.ai_service = ai_service;
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

		oppBoard = ai_service.getAIBoard();

		model.addAttribute("myBoard", myBoard);
		model.addAttribute("oppBoard", oppBoard);
		model.addAttribute("hits", hits);
		return "play";
	}

	@GetMapping("/play")
	public String getPlay(Model model) {
		System.out.println("getPlay, winner: " + haveWinner);
		if (haveWinner) {
			model.addAttribute("winner", winnerName);
			return "win";
		}
		model.addAttribute("myBoard", myBoard);
		model.addAttribute("oppBoard", oppBoard);
		model.addAttribute("hits", hits);
		return "play";
	}

	@PostMapping("/shoot")
	public String postShoot(@RequestParam String shot, Model model, HttpSession session) {
		shot = shot.toUpperCase();
		System.out.println("Shot made on: " + shot);
		int index = boardService.findSquare(shot, oppBoard);
		Square sq = oppBoard.get(index);
		sq.setShotMade();
		if (sq.isOccupied()) {
			System.out.println("Hit on " + sq.getOccupied());

			switch (sq.getOccupied()) {
			case "C":
				oppCarrier.addHit();
				hits[0] = oppCarrier.getHits();
				break;
			case "B":
				oppBattleship.addHit();
				hits[1] = oppBattleship.getHits();
				break;
			case "D":
				oppDestroyer.addHit();
				hits[2] = oppDestroyer.getHits();
				break;
			case "S":
				oppSubmarine.addHit();
				hits[3] = oppSubmarine.getHits();
				break;
			case "P":
				oppPatrolBoat.addHit();
				hits[4] = oppPatrolBoat.getHits();
				break;
			}

			haveWinner = meWinner();
			if (haveWinner) {
				winnerName = session.getAttribute("name").toString();
				return "redirect:play";
			}
		} else {
			System.out.println("Miss");
		}

		aiShot();

		return "redirect:play";
	}

	private void aiShot() {
		Random rand = new Random();
		Square sq;

		do {
			int index = rand.nextInt(0, 100);
			sq = myBoard.get(index);
		} while (sq.getShotMade());
		// TODO make shot

		sq.setShotMade();
		if (sq.isOccupied()) {
			System.out.println("Hit on " + sq.getOccupied());

			switch (sq.getOccupied()) {
			case "C":
				myCarrier.addHit();
				break;
			case "B":
				myBattleship.addHit();
				break;
			case "D":
				myDestroyer.addHit();
				break;
			case "S":
				mySubmarine.addHit();
				break;
			case "P":
				myPatrolBoat.addHit();
				break;
			}

			haveWinner = oppWinner();
			if (haveWinner) {
				winnerName = "AI";
			}
		} else {
			System.out.println("Miss");
		}
	}

	private boolean meWinner() {
		if (oppCarrier.sunk() && oppBattleship.sunk() && oppDestroyer.sunk() && oppSubmarine.sunk()
				&& oppPatrolBoat.sunk()) {
			System.out.println("MeWinner");
			return true;
		}
		return false;
	}

	private boolean oppWinner() {
		if (myCarrier.sunk() && myBattleship.sunk() && myDestroyer.sunk() && mySubmarine.sunk()
				&& myPatrolBoat.sunk()) {
			return true;
		}
		return false;
	}

}
