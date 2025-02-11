package fr.eni.tp.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.tp.encheres.bll.LoginService;
import fr.eni.tp.encheres.bll.LoginServiceImpl;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
public class LoginController {
	
	private LoginService loginService;
	
	

	public LoginController(LoginServiceImpl loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/inscription")
	public String afficherInscription(Model model) {
		
		model.addAttribute("utilisateur", new Utilisateur());
		
		return "inscription";
	}
	
	@PostMapping("/inscription")
	public String creerUtilisateur(@Valid @RequestParam("PasswordConfirm") @ModelAttribute("utilisateur")String mdpConfirm, Utilisateur user, BindingResult bindingResult) {
		
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
	
	@GetMapping("/profil")
	public String afficherProfil(@RequestParam("id")int id, Model model) {
		Utilisateur user = this.loginService.consulterUtilisateur(id);
		model.addAttribute("utilisateur", user);
		
		return "profil";
	}
	
	
}
