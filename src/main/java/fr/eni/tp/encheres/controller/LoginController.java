package fr.eni.tp.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.tp.encheres.bll.mockem.LoginServiceImpl;
import fr.eni.tp.encheres.bo.Utilisateur;

@Controller
public class LoginController {
	
	private LoginServiceImpl enchereService;
	
	

	public LoginController(LoginServiceImpl enchereService) {
		this.enchereService = enchereService;
	}

	@GetMapping("/inscription")
	public String afficherInscription(Model model) {
		
		model.addAttribute("utilisateur", new Utilisateur());
		
		return "inscription";
	}
	
	@PostMapping("/inscription")
	public String creerUtilisateur(@ModelAttribute("utilisateur")Utilisateur user) {
		this.enchereService.creerUtilisateur(user);
		
		
		return "accueil" ;
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
