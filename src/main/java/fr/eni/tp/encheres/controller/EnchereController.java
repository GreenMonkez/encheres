package fr.eni.tp.encheres.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import fr.eni.tp.encheres.bll.EnchereService;
import fr.eni.tp.encheres.bo.ArticleVendu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
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

}
