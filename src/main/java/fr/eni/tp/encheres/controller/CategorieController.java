package fr.eni.tp.encheres.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import fr.eni.tp.encheres.bll.EnchereService;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.exception.BusinessException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategorieController {

	private EnchereService enchereService;
	private MessageSource messageSource;

	public CategorieController(EnchereService enchereService, MessageSource messageSource) {
		this.enchereService = enchereService;
		this.messageSource = messageSource;
	}

	@GetMapping("/categories")
	public String getCategories(Model model) {
		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-categories";
	}

	@GetMapping("/categories/ajouter")
	public String getNouvelleCategorie(Model model) {
		model.addAttribute("categorie", new Categorie());

		return "view-nouvelle-categorie";
	}

	@PostMapping("/categories/ajouter")
	public String postNouvelleCategorie(@ModelAttribute("categorie") Categorie categorie, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "view-nouvelle-categorie";
		} else {
			enchereService.createNouvelleCategorie(categorie);

			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/modifier")
	public String getModifier(@RequestParam("idCategorie") int idCategorie, Model model) {
		model.addAttribute("categorie", enchereService.getCategorie(idCategorie));

		return "view-modifier-categorie";
	}

	@PostMapping("/categories/modifier")
	public String postModifier(@ModelAttribute("categorie") Categorie categorie, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return "view-modifier-categorie";
		} else {
			enchereService.updateCategorie(categorie);

			return "redirect:/categories";
		}
	}

	@PostMapping("/categories/supprimer")
	public String postSupprimer(@RequestParam("idCategorie") int idCategorie, Model model) {

		try {
			enchereService.deleteCategorie(idCategorie);

			return "redirect:/categories";
		} catch (BusinessException e) {
			e.printStackTrace();
			List<String> errorMessages = new ArrayList<String>();
			e.getClesErreurs().forEach(cle -> {
				String errorMessage = messageSource.getMessage(cle, null, LocaleContextHolder.getLocale());
				errorMessages.add(errorMessage);
			});
			model.addAttribute("errorMessages", errorMessages);
		}

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);
		return "view-categories";
	}

}