package fr.eni.tp.encheres.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.tp.encheres.bll.EnchereService;
import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "userSession" })
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

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-encheres";
	}

	@GetMapping("/encheres/nouvelleVente")
	public String getNouvelleVente(Model model, @ModelAttribute("userSession") Utilisateur userSession) {
		ArticleVendu article = new ArticleVendu();
		Retrait retrait = new Retrait();

		article.setLieuRetrait(retrait);
		article.getLieuRetrait().setRue(userSession.getRue());
		article.getLieuRetrait().setCode_postal(userSession.getCodePostal());
		article.getLieuRetrait().setVille(userSession.getVille());
		model.addAttribute("article", article);

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-nouvelle-vente";
	}

	@PostMapping("/encheres/nouvelleVente")
	public String postNouvelleVente(@Valid @ModelAttribute("article") ArticleVendu article, BindingResult bindingResult,
			Model model, @ModelAttribute("userSession") Utilisateur userSession) {

		if (bindingResult.hasErrors()) {
			List<Categorie> categories = enchereService.getCategories();
			model.addAttribute("categories", categories);
			return "view-nouvelle-vente";
		} else {
			try {
				article.setVendeur(userSession);
				enchereService.createNouvelleVente(article);
				return "redirect:/encheres";
			} catch (BusinessException e) {
				e.printStackTrace();
				e.getClesErreurs().forEach(cle -> {
					ObjectError error = new ObjectError("globalError", cle);
					bindingResult.addError(error);
				});
			}
		}
		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);
		return "view-nouvelle-vente";
	}

	@GetMapping("/encheres/search")
	public String postSearch(@RequestParam(name = "filtre") String filtre,
			@RequestParam(name = "categorie") int idCategorie, Model model) {
		List<ArticleVendu> articles = enchereService.getEncheresFiltrees(filtre, idCategorie);
		model.addAttribute("articles", articles);

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-encheres";
	}

}
