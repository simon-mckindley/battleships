package com.play.battleship.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@GetMapping("/")
	public String getHome() {
		return "login";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@PostMapping("/login")
	public String postLogin(String name, String opponent, HttpSession session) {
		if (name.isBlank()) {
			return "login";
		}
		
		session.setAttribute("name", name.strip());
		session.setAttribute("opponent", opponent.strip());
		
		return "redirect:place";
	}

}
