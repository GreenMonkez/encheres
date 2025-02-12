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
import fr.eni.tp.encheres.bo.Retrait;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("userSession")
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
	public String getNouvelleVente(Model model) {
		ArticleVendu article = new ArticleVendu();
		Retrait retrait = new Retrait();

		retrait.setRue("Rue des mouettes");
		retrait.setCode_postal("44800");
		retrait.setVille("Saint Herblain");

		article.setLieuRetrait(retrait);

		model.addAttribute("article", article);

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-nouvelle-vente";
	}

	@PostMapping("/encheres/nouvelleVente")
	public String postNouvelleVente(@Valid @ModelAttribute("article") ArticleVendu article,
			BindingResult bindingResult) {

		return "redirect:/encheres";
	}

}
