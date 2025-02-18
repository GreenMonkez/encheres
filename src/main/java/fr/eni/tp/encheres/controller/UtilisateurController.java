package fr.eni.tp.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.tp.encheres.bll.LoginService;
import fr.eni.tp.encheres.bo.Utilisateur;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SessionAttributes("userSession")
public class UtilisateurController {

	private LoginService loginService;

	public UtilisateurController(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * Méthode renvoyant la vue permettant l'achat de crédit par l'utilisateur
	 * 
	 * @param userSession l'utilisateur en session
	 * @param model       avec les données de l'utilisateur
	 * @return la vue de l'achat de crédit
	 */
	@GetMapping("/monProfil/achatCredit")
	public String getAchatCredit(@ModelAttribute("userSession") Utilisateur userSession, Model model) {
		Utilisateur user = loginService.consulterUtilisateur(userSession.getNoUtilisateur());
		model.addAttribute("user", user);
		return "view-achat-credit";
	}

	/**
	 * Méthode permettant de modifier la valeur des crédits en BDD de l'utilisatur
	 * en fonction de son id d'utilisateur et de son choix d'achat
	 * 
	 * @param achatCredit choix du nombre de crédits
	 * @param userSession utilisateur en session
	 * @return la vue des enchères
	 */
	@PostMapping("/monProfil/achatCredit")
	public String postAchatCredit(@RequestParam("achat") int achatCredit,
			@ModelAttribute("userSession") Utilisateur userSession) {
		loginService.updateCredit(userSession, achatCredit);
		return "redirect:/encheres";
	}
}
