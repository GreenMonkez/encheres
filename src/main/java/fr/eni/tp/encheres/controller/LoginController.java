package fr.eni.tp.encheres.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.tp.encheres.bll.LoginService;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
public class LoginController {

	private LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/inscription")
	public String afficherInscription(Model model) {

		model.addAttribute("utilisateur", new Utilisateur());

		return "inscription";
	}

	@PostMapping("/inscription")
	public String creerUtilisateur(
			@Valid @RequestParam("PasswordConfirm") @ModelAttribute("utilisateur") String mdpConfirm, Utilisateur user,
			BindingResult bindingResult) {

		if (!bindingResult.hasErrors()) {
			try {
				this.loginService.creerUtilisateur(user, mdpConfirm);
				return "redirect:/encheres";
			} catch (BusinessException e) {
				e.printStackTrace();
				e.getClesErreurs().forEach(cle -> {
					ObjectError error = new ObjectError("globalError", cle);
					bindingResult.addError(error);
				});
			}
			return "inscription";

		} else {
			return "inscription";
		}
	}

	@GetMapping("/profil")
	public String afficherProfil(@RequestParam("id") int id, Model model) {
		Utilisateur user = this.loginService.consulterUtilisateur(id);
		model.addAttribute("utilisateur", user);

		return "profil";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@ModelAttribute("userSession")
	public Utilisateur addUserSession() {
		System.out.println("Add user en session");
		return new Utilisateur();
	}

	@GetMapping("/login/session")
	public String connexion(@ModelAttribute("userSession") Utilisateur userSession, Principal principal) {

		if (principal != null) {
			System.out.println("Utilisateur connecté : " + principal.getName());
		} else {
			System.out.println("Utilisateur non connecté");
		}

		Utilisateur utilisateur = this.loginService.charger(principal.getName());

		if (utilisateur != null) {
			userSession.setNoUtilisateur(utilisateur.getNoUtilisateur());
			userSession.setPseudo(utilisateur.getPseudo());
			userSession.setNom(utilisateur.getNom());
			userSession.setPrenom(utilisateur.getPrenom());
			userSession.setEmail(utilisateur.getEmail());
			userSession.setCredit(utilisateur.getCredit());
			userSession.setTelephone(utilisateur.getTelephone());
			userSession.setRue(utilisateur.getRue());
			userSession.setCodePostal(utilisateur.getCodePostal());
			userSession.setVille(utilisateur.getVille());
			userSession.setAdministrateur(utilisateur.administrateur);
		} else {
			userSession.setNoUtilisateur(0);
			userSession.setPseudo(null);
			userSession.setNom(null);
			userSession.setPrenom(null);
			userSession.setEmail(null);
			userSession.setCredit(0);
			userSession.setTelephone(null);
			userSession.setRue(null);
			userSession.setCodePostal(null);
			userSession.setVille(null);
			userSession.setAdministrateur(false);

		}
		System.out.println(userSession.toString());

		return "redirect:/encheres";

	}

}
