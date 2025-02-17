package fr.eni.tp.encheres.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import fr.eni.tp.encheres.bll.EnchereService;
import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("userSession")
public class EnchereController {

	private EnchereService enchereService;

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	/**
	 * Méthode retournant la vue d'accueil des enchères lorsque l'utilisateur ne
	 * spécifiie pas de ressource dans l'URL
	 * 
	 * @return la vue des enchères
	 */
	@GetMapping("/")
	public String redirectToEncheres() {
		return "redirect:/encheres";
	}

	/**
	 * Méthode retournant la vue des enchères
	 * 
	 * @param model avec la liste des articles et la liste des catégories
	 * @return la vue des enchères
	 */
	@GetMapping("/encheres")
	public String getEncheres(Model model) {
		List<ArticleVendu> articles = enchereService.getEncheres();
		model.addAttribute("articles", articles);

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-encheres";
	}

	/**
	 * Méthode permettant de filtrer les articles en fonction d'un mot-clé et d'une
	 * catégorie si déconnecté, si connecté, des options supplémentaires en achats
	 * et ventes sont disponibles
	 * 
	 * @param filtre      comprenant le mot-clé
	 * @param idCategorie comprenant l'id de la catégorie
	 * @param options     comprenant la liste des options choisies
	 * @param model       avec la liste des articles filtrée et la liste des
	 *                    catégories
	 * @param session     avec les données de l'utilisateur en session
	 * @return la vue des enchères
	 */
	@PostMapping("/encheres/search")
	public String postSearch(@RequestParam(name = "filtre") String filtre,
			@RequestParam(name = "categorie") int idCategorie,
			@RequestParam(name = "option", required = false) List<String> options, Model model, HttpSession session) {
		List<ArticleVendu> articles = new ArrayList<ArticleVendu>();

		if (options == null) {
			articles = enchereService.getEncheresFiltrees(filtre, idCategorie);
		}

		if (options != null) {
			articles = enchereService.getEncheresFiltreesOptions(filtre, idCategorie, options,
					(Utilisateur) session.getAttribute("userSession"));
		}

		model.addAttribute("articles", articles);

		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);

		return "view-encheres";
	}

	/**
	 * Méthode retournant la vue de création d'un nouvel article
	 * 
	 * @param model       avec un article vide et la liste des catégories
	 * @param userSession avec les données de l'utilisateur en session
	 * @return la vue de création d'un article
	 */
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

	/**
	 * Méthode permettant d'insérer en BDD un nouvel article
	 * 
	 * @param article       comprenant les données du formulaire
	 * @param bindingResult comprenant les erreurs du formulaire
	 * @param model         avec la liste des catégories
	 * @param userSession   avec les données de l'utilisateur en session
	 * @return la vue de création d'un article si erreur, la vue des enchères sinon
	 */
	@PostMapping("/encheres/nouvelleVente")
	public String postNouvelleVente(@Valid @ModelAttribute("article") ArticleVendu article, BindingResult bindingResult,
			Model model, @ModelAttribute("userSession") Utilisateur userSession,
			@RequestParam("image") MultipartFile file) {

		if (bindingResult.hasErrors()) {
			List<Categorie> categories = enchereService.getCategories();
			model.addAttribute("categories", categories);
			return "view-nouvelle-vente";
		} else {
			try {
				article.setVendeur(userSession);
				enchereService.createNouvelleVente(article);

				if (!Files.exists(Paths.get("uploads/"))) {
					Files.createDirectories(Paths.get("uploads/"));
				}

				byte[] bytes = file.getBytes();
				Path path = Paths.get("uploads/" + article.getNoArticle() + ".png");
				Files.write(path, bytes);

				return "redirect:/encheres";
			} catch (BusinessException e) {
				e.printStackTrace();
				e.getClesErreurs().forEach(cle -> {
					ObjectError error = new ObjectError("globalError", cle);
					bindingResult.addError(error);
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		List<Categorie> categories = enchereService.getCategories();
		model.addAttribute("categories", categories);
		return "view-nouvelle-vente";
	}

	@GetMapping("/encheres/detail")
	public String detailArticle(@RequestParam("id") int id, Model model) {

		ArticleVendu article = this.enchereService.articleById(id);
		String pseudoAcheteur = enchereService.getPseudoAcheteur(article.getPrixVente(), id);
		Utilisateur acheteur = new Utilisateur();

		acheteur.setPseudo(pseudoAcheteur);
		article.setAcheteur(acheteur);
		model.addAttribute("article", article);
		return "detail_vente";
	}

}
