package fr.eni.tp.encheres.controller;

import java.security.Principal;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.tp.encheres.bll.LoginService;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;
import jakarta.validation.Valid;

/**
 * 
 */
@Controller
@SessionAttributes({ "userSession" })
public class LoginController {

	private LoginService loginService;
	private MessageSource messageSource;

	public LoginController(LoginService loginService, MessageSource messageSource) {
		this.loginService = loginService;
		this.messageSource = messageSource;
		
	}
	
	/**
	 * Méthode permettant d'afficher la vue d'inscription
	 * @param model
	 * @return  le formulaire d'inscription
	 */
	@GetMapping("/inscription")
	public String afficherInscription(Model model) {

		model.addAttribute("utilisateur", new Utilisateur());

		return "inscription";
	}

	/**
	 * Méthode permettant de soumettre le formulaire d'inscription
	 * @param mdpConfirm
	 * @param user
	 * @param bindingResult
	 * @return la page listes des enchères; Si erreur @Return le formulaire d'inscription
	 * 
	 */
	@PostMapping("/inscription")
	public String creerUtilisateur(@RequestParam("PasswordConfirm") String mdpConfirm,
			@Valid @ModelAttribute("utilisateur") Utilisateur user, BindingResult bindingResult) {

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
	
	/**
	 * Méthode permettant d'afficher la page du profil avec les infos de l'utilisateur en session
	 * @param model
	 * @param userSession
	 * @return la page mon profil
	 */
	@GetMapping("/monProfil")
	public String afficherProfil(Model model, @ModelAttribute("userSession") Utilisateur userSession) {
		Utilisateur user = this.loginService.consulterUtilisateur(userSession.getNoUtilisateur());
		model.addAttribute("utilisateur", user);

		return "mon-profil";
	}
	
	/**
	 * Méthode permettant d'accéder au formulaire de modification de l'utilisateur
	 * @param model
	 * @param userSession
	 * @return la page formulaire modifier-profil
	 */
	@PostMapping("/monProfil")
	public String allerModifierProfil(Model model, @ModelAttribute("userSession") Utilisateur userSession) {

		Utilisateur user = this.loginService.consulterUtilisateur(userSession.getNoUtilisateur());
		model.addAttribute("utilisateur", user);

		return "modifier-profil";
	}
	
	/**
	 * Méthode permettant d'afficher la page de formulaire pour modifier le profil
	 * @param model
	 * @param userSession
	 * @return page modifier le profil
	 */
	@GetMapping("/monProfil/modifier")
	public String afficherModifierProfil(Model model, @ModelAttribute("userSession") Utilisateur userSession) {

		Utilisateur user = this.loginService.consulterUtilisateur(userSession.getNoUtilisateur());
		model.addAttribute("utilisateur", user);

		return "modifier-profil";
	}
	
	/**
	 * Méthode permettant de soumettre les informations modifier de l'utilisateur puis après traitement de modifier l'user en session
	 * @param newMdp
	 * @param mdpConfirm
	 * @param userSession
	 * @param user
	 * @param bindingResult
	 * @return la page mon-profil; Si erreur : @Return le formulaire de modification 
	 */
	@PostMapping("/monProfil/modifier")
	public String modifierProfil(@RequestParam("NewMotDePasse") String newMdp,
			@RequestParam("PasswordConfirm") String mdpConfirm, @ModelAttribute("userSession") Utilisateur userSession,
			@Valid @ModelAttribute("utilisateur") Utilisateur user, BindingResult bindingResult) {
		
		user.setNoUtilisateur(userSession.getNoUtilisateur());
		if (!bindingResult.hasErrors()) {

			try {
				this.loginService.modifierUtilisateur(user, mdpConfirm, newMdp);

				Utilisateur utilisateur = this.loginService.consulterUtilisateur(user.getNoUtilisateur());

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
				System.out.println(userSession);
				return "redirect:/monProfil";
			} catch (BusinessException e) {
				e.printStackTrace();
				e.getClesErreurs().forEach(cle -> {
					ObjectError error = new ObjectError("globalError", cle);
					bindingResult.addError(error);
				});
			}
			return "modifier-profil";
		} else {
			return "modifier-profil";
		}

	}

	/**
	 * Méthode permettant d'afficher le profil d'un utilisateur
	 * @param id
	 * @param model
	 * @return la page profil-utilisateur
	 */
	@GetMapping("/profil/vendeur")
	public String afficherProfilVendeur(@RequestParam("id") int id, Model model) {
		Utilisateur user = this.loginService.consulterUtilisateur(id);
		model.addAttribute("utilisateur", user);

		return "profil-vendeur";

	}

	@PostMapping("/monProfil/supprimer")
	public String supprimerProfil(@ModelAttribute("userSession") Utilisateur userSession, RedirectAttributes redirectAttributes, Locale locale) throws BusinessException {

		try {
			
			loginService.supprimerUtilisateur(userSession);
			return "redirect:/logout";
		} catch (BusinessException e) {
			e.printStackTrace();
		     e.getClesErreurs().forEach(cle -> {
		     String message = messageSource.getMessage(cle, null, locale); // Traduire la clé en message
		     redirectAttributes.addFlashAttribute("erreurs", message); // Ajouter le message
		        });
		        
		        return "redirect:/monProfil"; // Retourner à la page du profil avec les messages d'erreur
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
	public String connexion(@ModelAttribute("userSession") Utilisateur userSession, Principal principal) {

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
			userSession.setAdministrateur(utilisateur.isAdministrateur());

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

		return "redirect:/encheres";

	}

}
