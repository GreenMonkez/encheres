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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.tp.encheres.bll.LoginService;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({"userSession"})
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
	public String creerUtilisateur(@RequestParam("PasswordConfirm")String mdpConfirm, @Valid @ModelAttribute("utilisateur") Utilisateur user, BindingResult bindingResult) {
		
		if (!bindingResult.hasErrors()) {
			try {
				this.loginService.creerUtilisateur(user, mdpConfirm);
				return "redirect:/encheres";
			} catch (BusinessException e) {
				e.printStackTrace();
				e.getClesErreurs().forEach(cle->{
					ObjectError error = new ObjectError("globalError", cle);
					bindingResult.addError(error);
				});
			}
		return "inscription" ;

		}else {
		return "inscription" ;
	}}
	
	
		
	

	@GetMapping("/monProfil")
	public String afficherProfil( Model model, @ModelAttribute("userSession") Utilisateur userSession) {
		Utilisateur user = this.loginService.consulterUtilisateur(userSession.getNoUtilisateur());
		model.addAttribute("utilisateur", user);

		
		return "mon-profil";
	}
	
	@PostMapping("/monProfil")
	public String allerModifierProfil(Model model, @ModelAttribute("userSession") Utilisateur userSession) {
		
		Utilisateur user = this.loginService.consulterUtilisateur(userSession.getNoUtilisateur());
		model.addAttribute("utilisateur", user);
		
		
		return "modifier-profil";
	}
	
	@GetMapping("/monProfil/modifier")
	public String  afficherModifierProfil(Model model, @ModelAttribute("userSession") Utilisateur userSession) {
		
		Utilisateur user = this.loginService.consulterUtilisateur(userSession.getNoUtilisateur());
		model.addAttribute("utilisateur", user);
		
		
		return "modifier-profil";
	}
	
	@PostMapping("/monProfil/modifier")
	public String modifierProfil(@RequestParam("NewMotDePasse")String newMdp, @RequestParam("PasswordConfirm")String mdpConfirm,@ModelAttribute("userSession") Utilisateur userSession, @Valid @ModelAttribute("utilisateur") Utilisateur user, BindingResult bindingResult) {
		
		user.setNoUtilisateur(userSession.getNoUtilisateur());
		if (!bindingResult.hasErrors()) {
			
			try {
			this.loginService.modifierUtilisateur(user, mdpConfirm, newMdp);
				return "redirect:/monProfil";
			} catch (BusinessException e) {
				e.printStackTrace();
				e.getClesErreurs().forEach(cle->{
					ObjectError error = new ObjectError("globalError", cle);
					bindingResult.addError(error);
				});
			}	
			return "modifier-profil";
		}else {
			return "modifier-profil";
		}
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@ModelAttribute("userSession")
	public Utilisateur addUserSession() {
		return new Utilisateur();
	}
	
	@GetMapping("/login/session")
	public String connexion(@ModelAttribute("userSession")Utilisateur userSession, Principal principal) {
		
		if (principal != null) {
			System.out.println("Utilisateur connecté : " + principal.getName());
		}else {
			System.out.println("Utilisateur non connecté");
		}
		
		Utilisateur utilisateur = this.loginService.charger(principal.getName());
		System.out.println(utilisateur);
		
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
			userSession.setAdministrateur(utilisateur.isAdministrateur());
		}else {
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
		
		
		return "redirect:/encheres";
		
		
	}
	
	

}
