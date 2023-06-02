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
	private Carrier oppCarrier;
	private Battle_ship oppBattleship;
	private Destroyer oppDestroyer;
	private Submarine oppSubmarine;
	private PatrolBoat oppPatrolBoat;
	private Carrier myCarrier;
	private Battle_ship myBattleship;
	private Destroyer myDestroyer;
	private Submarine mySubmarine;
	private PatrolBoat myPatrolBoat;
	private int[] hits = new int[5];
	private String shotStatus = "";
	private String hitClass = "";
	private String missClass = "";
	private Square lastAIHit;
	private String lastAIShot = "";
	private String lastYourShot = "";
	private String shotOn = "Welcome";
	private String lastShotText = "";
	private String alertText = "";
	private String modalType = "";
	private boolean haveWinner;
	private String winnerName = "";

	public MainController(BoardService boardService, AI_Service ai_service) {
		this.boardService = boardService;
		this.ai_service = ai_service;
	}

	@GetMapping("/place")
	public String getPlace(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:login";
		}
		haveWinner = false;
		winnerName = "";
		oppCarrier = new Carrier();
		oppBattleship = new Battle_ship();
		oppDestroyer = new Destroyer();
		oppSubmarine = new Submarine();
		oppPatrolBoat = new PatrolBoat();
		myCarrier = new Carrier();
		myBattleship = new Battle_ship();
		myDestroyer = new Destroyer();
		mySubmarine = new Submarine();
		myPatrolBoat = new PatrolBoat();

		for (int a = 0; a < hits.length; a++) {
			hits[a] = 0;
		}

		return "place";
	}

	@PostMapping("/play")
	public String postPlay(HttpSession session, HttpServletRequest request, Model model) {
		if (session.getAttribute("name") == null) {
			return "redirect:login";
		}
		
		myBoard = new ArrayList<Square>();
		for (char r = 'A'; r <= 'J'; r++) {
			for (int c = 1; c <= 10; c++) {
				myBoard.add(new Square(String.format("%c%d", r, c)));
			}
		}

		try {
			myBoard = boardService.SetBoard(myBoard, new Coords("Carrier", request.getParameter("carrier").toString()));
			myBoard = boardService.SetBoard(myBoard,
					new Coords("Battleship", request.getParameter("battleship").toString()));
			myBoard = boardService.SetBoard(myBoard,
					new Coords("Destroyer", request.getParameter("destroyer").toString()));
			myBoard = boardService.SetBoard(myBoard,
					new Coords("Submarine", request.getParameter("submarine").toString()));
			myBoard = boardService.SetBoard(myBoard,
					new Coords("Patrol-Boat", request.getParameter("patrol").toString()));
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return "redirect:place";
		}

		oppBoard = ai_service.getAIBoard();

		model.addAttribute("ani-ship", "ship-none");
		model.addAttribute("shotOn", "");
		model.addAttribute("alertHead", "Ready to play?");
		model.addAttribute("alertText", "Click on the board squares to take your shot");
		model.addAttribute("modalType", "");
		model.addAttribute("myBoard", myBoard);
		model.addAttribute("oppBoard", oppBoard);
		model.addAttribute("hits", hits);
		return "play";
	}

	@GetMapping("/play")
	public String getPlay(HttpSession session, Model model) {
		if (session.getAttribute("name") == null) {
			return "redirect:login";
		}
		
		if (haveWinner) {
			System.out.println("Winner: " + winnerName);
			if (winnerName.equals(session.getAttribute("name").toString())) {
				modalType = "winner";
			} else {
				modalType = "loser";
			}

		}

		model.addAttribute("aniShip", shotStatus);
		model.addAttribute("hit", hitClass);
		model.addAttribute("miss", missClass);
		model.addAttribute("shotOn", shotOn);
		model.addAttribute("alertHead", lastShotText);
		model.addAttribute("alertText", alertText);
		model.addAttribute("modalType", modalType);
		model.addAttribute("myBoard", myBoard);
		model.addAttribute("yourLastShot", lastYourShot);
		model.addAttribute("oppBoard", oppBoard);
		model.addAttribute("aiLastShot", lastAIShot);
		model.addAttribute("hits", hits);
		model.addAttribute("winner", winnerName);
		return "play";
	}

	@PostMapping("/shoot")
	public String postShoot(@RequestParam String shot, Model model, HttpSession session) {
		shot = shot.toUpperCase();
		int index = boardService.findSquare(shot, oppBoard);
		Square sq = oppBoard.get(index);
		sq.setShotMade();
		shotOn = "Your shot on " + sq.getName();
		lastYourShot = sq.getName();
		modalType = "player";

		if (sq.isOccupied()) {
			lastShotText = "You have HIT the ";
			alertText = "Well done";
			hitClass = "show";
			missClass = "";

			switch (sq.getOccupied()) {
			case "C":
				oppCarrier.addHit();
				hits[0] = oppCarrier.getHits();
				shotStatus = "carrier";
				if (oppCarrier.sunk()) {
					lastShotText = "You have SUNK the Carrier!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Carrier";
				}
				break;
			case "B":
				oppBattleship.addHit();
				hits[1] = oppBattleship.getHits();
				shotStatus = "battleship";
				if (oppBattleship.sunk()) {
					lastShotText = "You have SUNK the Battleship!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Battleship";
				}
				break;
			case "D":
				oppDestroyer.addHit();
				hits[2] = oppDestroyer.getHits();
				shotStatus = "destroyer";
				if (oppDestroyer.sunk()) {
					lastShotText = "You have SUNK the Destroyer!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Destroyer";
				}
				break;
			case "S":
				oppSubmarine.addHit();
				hits[3] = oppSubmarine.getHits();
				shotStatus = "submarine";
				if (oppSubmarine.sunk()) {
					lastShotText = "You have SUNK the Submarine!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Submarine";
				}
				break;
			case "P":
				oppPatrolBoat.addHit();
				hits[4] = oppPatrolBoat.getHits();
				shotStatus = "patrolboat";
				if (oppPatrolBoat.sunk()) {
					lastShotText = "You have SUNK the Patrol Boat!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Patrol Boat";
				}
				break;
			}

			haveWinner = meWinner();
			if (haveWinner) {
				winnerName = session.getAttribute("name").toString();
				return "redirect:play";
			}
		} else {
			lastShotText = "You missed...";
			alertText = "Too bad, your opponents turn";
			shotStatus = "ship-none";
			hitClass = "";
			missClass = "show";
		}

		return "redirect:play";
	}

	@GetMapping("/oppshoot")
	private String aiShot(HttpSession session) {
		Square sq = new Square("");
		String name = session.getAttribute("opponent").toString();
		modalType = "opponent";

		if (name.equals("AI-EASY")) {
			sq = ai_service.aiShot_easy(myBoard);
		} else if (name.equals("AI-HARD")) {
			sq = ai_service.aiShot_hard(myBoard, lastAIHit, myCarrier, myBattleship, myDestroyer, mySubmarine,
					myPatrolBoat);
		}

		shotOn = "Opponent shot on " + sq.getName();
		lastAIShot = sq.getName();
		sq.setShotMade();

		if (sq.isOccupied()) {
			lastAIHit = sq;
			lastShotText = "Your opponent has HIT your ";
			alertText = "Too bad";
			hitClass = "show";
			missClass = "";

			switch (sq.getOccupied()) {
			case "C":
				myCarrier.addHit();
				shotStatus = "carrier";
				if (myCarrier.sunk()) {
					lastShotText = "Your Carrier has been SUNK!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Carrier";
				}
				break;
			case "B":
				myBattleship.addHit();
				shotStatus = "battleship";
				if (myBattleship.sunk()) {
					lastShotText = "Your Battleship has been SUNK!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Battleship";
				}
				break;
			case "D":
				myDestroyer.addHit();
				shotStatus = "destroyer";
				if (myDestroyer.sunk()) {
					lastShotText = "Your Destroyer has been SUNK!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Destroyer";
				}
				break;
			case "S":
				mySubmarine.addHit();
				shotStatus = "submarine";
				if (mySubmarine.sunk()) {
					lastShotText = "Your Submarine has been SUNK!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Submarine";
				}
				break;
			case "P":
				myPatrolBoat.addHit();
				shotStatus = "patrolboat";
				if (myPatrolBoat.sunk()) {
					lastShotText = "Your Patrol Boat has been SUNK!";
					shotStatus += " sunk";
				} else {
					lastShotText += "Patrol Boat";
				}
				break;
			}

			haveWinner = oppWinner();
			if (haveWinner) {
				winnerName = name.toUpperCase();
				return "redirect:play";
			}
		} else {
			lastShotText = "Your opponent has missed...";
			alertText = "That was lucky, your turn";
			shotStatus = "ship-none";
			hitClass = "";
			missClass = "show";
		}

		return "redirect:play";
	}

	private boolean meWinner() {
		if (oppCarrier.sunk() && oppBattleship.sunk() && oppDestroyer.sunk() && oppSubmarine.sunk()
				&& oppPatrolBoat.sunk()) {
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
