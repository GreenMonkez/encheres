package fr.eni.tp.encheres.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.tp.encheres.bll.EnchereService;
import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Utilisateur;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "userEnSession" })
public class EnchereController {

	private EnchereService enchereService;

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@GetMapping("/")
	public String redirectToEncheres() {
		return "redirect:/encheres";
	}

	@GetMapping("/encheres")
	public String getEncheres(Model model) {
		List<ArticleVendu> articles = enchereService.getEncheres();

		model.addAttribute("articles", articles);

		return "view-encheres";
	}

	@GetMapping("/encheres/nouvelleVente")
	public String getNouvelleVente(Model model, @ModelAttribute("userEnSession") Utilisateur userSession) {
		ArticleVendu article = new ArticleVendu();

		article.setVendeur(userSession);

		model.addAttribute("article", article);

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-nouvelle-vente";
	}

	@PostMapping("/encheres/nouvelleVente")
	public String postNouvelleVente(@Valid @ModelAttribute("article") ArticleVendu article, BindingResult bindingResult,
			Model model, @ModelAttribute("userEnSession") Utilisateur userSession) {

		if (bindingResult.hasErrors()) {
			List<Categorie> categories = enchereService.getCategories();
			model.addAttribute("categories", categories);
			return "view-nouvelle-vente";
		} else {
			article.setVendeur(userSession);
			enchereService.createNouvelleVente(article);
			return "redirect:/encheres";
		}
	}

	@ModelAttribute("userEnSession")
	public Utilisateur addUtilisateurEnSession() {
		Utilisateur user = new Utilisateur();
		user.setNoUtilisateur(1);
		user.setRue("Rue du feur");
		user.setCodePostal("44800");
		user.setVille("Nantes");
		return user;
	}

}
