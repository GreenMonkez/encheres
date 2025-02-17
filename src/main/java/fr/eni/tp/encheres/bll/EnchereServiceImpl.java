package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.ArticleVenduDAO;
import fr.eni.tp.encheres.dal.CategorieDAO;
import fr.eni.tp.encheres.dal.EnchèreDAO;
import fr.eni.tp.encheres.dal.RetraitDAO;
import fr.eni.tp.encheres.dal.UtilisateurDAO;
import fr.eni.tp.encheres.exception.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {

	private ArticleVenduDAO articleVenduDAO;
	private CategorieDAO categorieDAO;
	private EnchèreDAO enchèreDAO;
	private RetraitDAO retraitDAO;
	private UtilisateurDAO utilisateurDAO;

	public EnchereServiceImpl(ArticleVenduDAO articleVenduDAO, CategorieDAO categorieDAO, EnchèreDAO enchèreDAO,
			RetraitDAO retraitDAO, UtilisateurDAO utilisateurDAO) {
		this.articleVenduDAO = articleVenduDAO;
		this.categorieDAO = categorieDAO;
		this.enchèreDAO = enchèreDAO;
		this.retraitDAO = retraitDAO;
		this.utilisateurDAO = utilisateurDAO;
	}

	/**
	 * Méthode retournant la liste des articles en cours avec leur vendeur, leur
	 * catégorie, leurs enchères et les utilisateurs à l'origine de ces enchères
	 */
	@Override
	public List<ArticleVendu> getEncheres() {
		List<ArticleVendu> articles = articleVenduDAO.getArticles();

		List<ArticleVendu> articlesEnCours = new ArrayList<ArticleVendu>();
		for (ArticleVendu articleVendu : articles) {
			if ((articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now()))
					&& (articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now()))) {
				articlesEnCours.add(articleVendu);
			}
		}

		for (ArticleVendu articleVendu : articlesEnCours) {
			articleVendu.setVendeur(utilisateurDAO.getUtilisateur(articleVendu.getVendeur().getNoUtilisateur()));
			articleVendu.setCategorieArticle(
					categorieDAO.getCategorie(articleVendu.getCategorieArticle().getNoCategorie()));
			if ((enchèreDAO.countByIdArticle(articleVendu.getNoArticle())) != 0) {
				articleVendu.setEncheres(enchèreDAO.getEncheres(articleVendu.getNoArticle()));
				for (Enchère enchère : articleVendu.getEncheres()) {
					enchère.setUtilisateur(utilisateurDAO.getUtilisateur(enchère.getUtilisateur().getNoUtilisateur()));
				}
			}
		}

		return articlesEnCours;
	}

	/**
	 * Méthode retournant la liste des articles en cours filtrée par un mot-clé et
	 * par une catégorie
	 */
	@Override
	public List<ArticleVendu> getEncheresFiltrees(String filtre, int idCategorie) {
		List<ArticleVendu> articlesFiltres = new ArrayList<ArticleVendu>();
		if (filtre.isBlank() && idCategorie == 0) { // mot-clé vide et catégorie vide
			articlesFiltres = articleVenduDAO.getArticles();
		}
		if (!filtre.isBlank() && idCategorie == 0) { // mot-clé rempli et catégorie vide
			String filtreSql = "%" + filtre + "%";
			articlesFiltres = articleVenduDAO.getArticlesFiltresByString(filtreSql);
		}
		if (filtre.isBlank() && !(idCategorie == 0)) { // mot clé vide et catégorie remplie
			articlesFiltres = articleVenduDAO.getArticlesFiltresById(idCategorie);
		}
		if (!filtre.isBlank() && !(idCategorie == 0)) { // mot clé rempli et catégorie remplie
			String filtreSql = "%" + filtre + "%";
			articlesFiltres = articleVenduDAO.getArticlesFiltresByStringAndId(filtreSql, idCategorie);
		}

		List<ArticleVendu> articlesFiltresEnCours = new ArrayList<ArticleVendu>();
		for (ArticleVendu articleVendu : articlesFiltres) {
			if ((articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now()))
					&& (articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now()))) {
				articlesFiltresEnCours.add(articleVendu);
			}
		}

		for (ArticleVendu articleVendu : articlesFiltresEnCours) {
			articleVendu.setVendeur(utilisateurDAO.getUtilisateur(articleVendu.getVendeur().getNoUtilisateur()));
			articleVendu.setCategorieArticle(
					categorieDAO.getCategorie(articleVendu.getCategorieArticle().getNoCategorie()));
			if ((enchèreDAO.countByIdArticle(articleVendu.getNoArticle())) != 0) {
				articleVendu.setEncheres(enchèreDAO.getEncheres(articleVendu.getNoArticle()));
				for (Enchère enchère : articleVendu.getEncheres()) {
					enchère.setUtilisateur(utilisateurDAO.getUtilisateur(enchère.getUtilisateur().getNoUtilisateur()));
				}
			}
		}

		return articlesFiltresEnCours;
	}

	/**
	 * Méthode retournant la liste des articles filtrée en fonction d'un mot-clé, de
	 * la catégorie et des choix optionnels de l'utilisateur, liés à l'achat ou à la
	 * vente
	 */
	@Override
	public List<ArticleVendu> getEncheresFiltreesOptions(String filtre, int idCategorie, List<String> options,
			Utilisateur userSession) {
		List<ArticleVendu> articlesFiltres = new ArrayList<ArticleVendu>();
		if (filtre.isBlank() && idCategorie == 0) { // mot-clé vide et catégorie vide
			articlesFiltres = articleVenduDAO.getArticles();
		}
		if (!filtre.isBlank() && idCategorie == 0) { // mot-clé rempli et catégorie vide
			String filtreSql = "%" + filtre + "%";
			articlesFiltres = articleVenduDAO.getArticlesFiltresByString(filtreSql);
		}
		if (filtre.isBlank() && !(idCategorie == 0)) { // mot clé vide et catégorie remplie
			articlesFiltres = articleVenduDAO.getArticlesFiltresById(idCategorie);
		}
		if (!filtre.isBlank() && !(idCategorie == 0)) { // mot clé rempli et catégorie remplie
			String filtreSql = "%" + filtre + "%";
			articlesFiltres = articleVenduDAO.getArticlesFiltresByStringAndId(filtreSql, idCategorie);
		}

		List<ArticleVendu> articlesFiltresOptions = new ArrayList<ArticleVendu>();
		for (String option : options) {
			if (option.equals("achats1")) { // ajoute l'article si la vente est en cours et que l'utilisateur n'est pas
											// le vendeur
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getDateDebutEncheres().isBefore(LocalDateTime.now()))
							&& (article.getDateFinEncheres().isAfter(LocalDateTime.now()))
							&& (article.getVendeur().getNoUtilisateur() != userSession.getNoUtilisateur())) {
						articlesFiltresOptions.add(article);
					}
				}
			}

			if (option.equals("achats2")) { // ajoute l'article si la vente est en cours, que l'utilisateur n'est pas le
											// vendeur et que l'utilisateur possède une enchère sur l'article
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getDateDebutEncheres().isBefore(LocalDateTime.now()))
							&& (article.getDateFinEncheres().isAfter(LocalDateTime.now()))
							&& (article.getVendeur().getNoUtilisateur() != userSession.getNoUtilisateur())) {
						for (Enchère enchère : article.getEncheres()) {
							if (enchère.getUtilisateur().getNoUtilisateur() == userSession.getNoUtilisateur()) {
								articlesFiltresOptions.add(article);
							}
						}
					}
				}
			}

			if (option.equals("achats3")) { // ajoute l'article si la vente est terminée, que l'utilisateur n'est pas le
											// vendeur et que l'utilisateur a remporté la vente
				for (ArticleVendu article : articlesFiltres) {
					if (article.getDateFinEncheres().isBefore(LocalDateTime.now())
							&& (article.getVendeur().getNoUtilisateur() != userSession.getNoUtilisateur())) {
						for (Enchère enchère : article.getEncheres()) {
							if (enchère.getMontant_enchere() == article.getPrixVente()
									&& enchère.getUtilisateur().getNoUtilisateur() == userSession.getNoUtilisateur()) {
								articlesFiltresOptions.add(article);
							}
						}
					}
				}
			}

			if (option.equals("ventes1")) { // ajoute l'article si la vente est en cours et que l'utilisateur est le
											// vendeur
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getVendeur().getNoUtilisateur() == userSession.getNoUtilisateur())
							&& (article.getDateDebutEncheres().isBefore(LocalDateTime.now()))
							&& (article.getDateFinEncheres().isAfter(LocalDateTime.now()))) {
						articlesFiltresOptions.add(article);
					}
				}
			}

			if (option.equals("ventes2")) { // ajoute l'article si la vente est terminée et que l'utilisateur est le
											// vendeur
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getVendeur().getNoUtilisateur() == userSession.getNoUtilisateur())
							&& (article.getDateDebutEncheres().isAfter(LocalDateTime.now()))) {
						articlesFiltresOptions.add(article);
					}
				}
			}

			if (option.equals("ventes3")) { // ajoute l'article si la vente n'est pas commencée et que l'utilisateur est
											// le vendeur
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getVendeur().getNoUtilisateur() == userSession.getNoUtilisateur())
							&& (article.getDateFinEncheres().isBefore(LocalDateTime.now()))) {
						articlesFiltresOptions.add(article);
					}
				}
			}
		}

		// filtre la liste des articles pour ne pas avoir de doublon
		Set<Integer> seenIds = new HashSet<>();

		List<ArticleVendu> articlesFiltresOptionsUnique = articlesFiltresOptions.stream()
				.filter(article -> seenIds.add(article.getNoArticle())).collect(Collectors.toList());

		for (ArticleVendu articleVendu : articlesFiltresOptionsUnique) {
			articleVendu.setVendeur(utilisateurDAO.getUtilisateur(articleVendu.getVendeur().getNoUtilisateur()));
			articleVendu.setCategorieArticle(
					categorieDAO.getCategorie(articleVendu.getCategorieArticle().getNoCategorie()));
			if ((enchèreDAO.countByIdArticle(articleVendu.getNoArticle())) != 0) {
				articleVendu.setEncheres(enchèreDAO.getEncheres(articleVendu.getNoArticle()));
				for (Enchère enchère : articleVendu.getEncheres()) {
					enchère.setUtilisateur(utilisateurDAO.getUtilisateur(enchère.getUtilisateur().getNoUtilisateur()));
				}
			}
		}

		return articlesFiltresOptionsUnique;
	}

	/**
	 * Méthode retournant la liste des catégories
	 */
	@Override
	public List<Categorie> getCategories() {
		return categorieDAO.getCategories();
	}

	/**
	 * Méthode retournant une catégorie en fonction de son ID
	 */
	@Override
	public Categorie getCategorie(int idCategorie) {
		return categorieDAO.getCategorie(idCategorie);
	}

	/**
	 * Méthode permettant de créer un nouvel article en BDD à partir d'un article
	 * Insère par la même occasion un lieu de retrait correspondant en BDD
	 */
	@Override
	public void createNouvelleVente(ArticleVendu article) throws BusinessException {
		BusinessException be = new BusinessException();

		boolean valide = dateDebutConforme(article.getDateDebutEncheres(), be);
		valide &= dateFinConforme(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);

		if (valide) {
			articleVenduDAO.create(article);
			retraitDAO.create(article);
		} else {
			throw be;
		}

	}

	@Override
	public ArticleVendu articleById(int noArticle) {

		ArticleVendu article = this.articleVenduDAO.read(noArticle);
		Utilisateur vendeur = this.utilisateurDAO.getUtilisateur(article.getVendeur().getNoUtilisateur());
		Retrait lieuRetrait = this.retraitDAO.getRetraitById(noArticle);
		String libelleCategorie = this.categorieDAO.getCategorie(article.getCategorieArticle().getNoCategorie())
				.getLibelle();
		article.setLieuRetrait(lieuRetrait);
		article.getCategorieArticle().setLibelle(libelleCategorie);
		article.setVendeur(vendeur);
		return article;
	}

	@Override
	public String getPseudoAcheteur(int prixVente, int noArticle) {
		int countEnchere = this.enchèreDAO.getCountEnchere(prixVente, noArticle);
		if (countEnchere != 0) {
			Enchère enchereAcheteur = this.enchèreDAO.getUtilisateurParPrix(prixVente, noArticle);
			Utilisateur acheteur = this.utilisateurDAO
					.getUtilisateur(enchereAcheteur.getUtilisateur().getNoUtilisateur());

			return acheteur.getPseudo();
		}
		return null;
	}

	/**
	 * Méthode validant la date de fin d'un article en vente
	 * 
	 * @param dateDebut = date de début de la vente de l'article
	 * @param dateFin   = date de fin de la vente de l'article
	 * @param be
	 * @return true si conforme, false sinon
	 */
	private boolean dateFinConforme(LocalDateTime dateDebut, LocalDateTime dateFin, BusinessException be) {
		if (dateFin == null) {
			be.addErreur("date.fin.null");
			return false;
		}
		if (dateFin.isBefore(dateDebut)) {
			be.addErreur("date.fin.invalide");
			return false;
		}
		return true;
	}

	/**
	 * Méthode validant la date de début d'un article en vente
	 * 
	 * @param dateDebut = date de début de la vente de l'article
	 * @param be
	 * @return true si conforme, false sinon
	 */
	private boolean dateDebutConforme(LocalDateTime dateDebut, BusinessException be) {
		if (dateDebut == null) {
			be.addErreur("date.debut.null");
			return false;
		}
		if (dateDebut.isBefore(LocalDateTime.now())) {
			be.addErreur("date.debut.passee");
			return false;
		}
		return true;
	}
}
