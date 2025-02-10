package fr.eni.tp.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.tp.encheres.bll.mockem.LoginServiceImpl;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;
import jakarta.validation.Valid;

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
	public String creerUtilisateur(@Valid @ModelAttribute("utilisateur")Utilisateur user, BindingResult bindingResult) {
		
		if (!bindingResult.hasErrors()) {
			try {
				this.enchereService.creerUtilisateur(user);
				return "index";
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
	
	
}
