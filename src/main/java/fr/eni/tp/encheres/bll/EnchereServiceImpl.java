package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// ****************************** ARTICLE ******************************

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

	/**
	 * Méthode retournant la liste des articles en cours avec leur vendeur, leur
	 * catégorie, leurs enchères et les utilisateurs à l'origine de ces enchères
	 */
	@Override
	public List<ArticleVendu> getEncheres() {
		List<ArticleVendu> articles = articleVenduDAO.getArticles();

		for (ArticleVendu articleVendu : articles) {
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

		List<ArticleVendu> articlesEnCours = new ArrayList<ArticleVendu>();
		for (ArticleVendu articleVendu : articles) {
			if ((articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now()))
					&& (articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now()))) {
				articleVendu.setEtatVente("enCours");
				articlesEnCours.add(articleVendu);
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

		for (ArticleVendu articleVendu : articlesFiltres) {
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

		List<ArticleVendu> articlesFiltresEnCours = new ArrayList<ArticleVendu>();
		for (ArticleVendu articleVendu : articlesFiltres) {
			if ((articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now()))
					&& (articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now()))) {
				articleVendu.setEtatVente("enCours");
				articlesFiltresEnCours.add(articleVendu);
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

		for (ArticleVendu articleVendu : articlesFiltres) {
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

		List<ArticleVendu> articlesFiltresOptions = new ArrayList<ArticleVendu>();
		for (String option : options) {
			if (option.equals("achats1")) { // ajoute l'article si la vente est en cours et que l'utilisateur n'est pas
											// le vendeur
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getDateDebutEncheres().isBefore(LocalDateTime.now()))
							&& (article.getDateFinEncheres().isAfter(LocalDateTime.now()))
							&& (article.getVendeur().getNoUtilisateur() != userSession.getNoUtilisateur())) {
						article.setEtatVente("enCours");
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
								article.setEtatVente("enCours");
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
								article.setEtatVente("finie");
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
						article.setEtatVente("enCours");
						articlesFiltresOptions.add(article);
					}
				}
			}

			if (option.equals("ventes2")) { // ajoute l'article si la vente n'est pas commencée et que l'utilisateur est
											// le vendeur
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getVendeur().getNoUtilisateur() == userSession.getNoUtilisateur())
							&& (article.getDateDebutEncheres().isAfter(LocalDateTime.now()))) {
						article.setEtatVente("pasCommencee");
						articlesFiltresOptions.add(article);
					}
				}
			}

			if (option.equals("ventes3")) { // ajoute l'article si la vente est terminée et que l'utilisateur est le
											// vendeur
				for (ArticleVendu article : articlesFiltres) {
					if ((article.getVendeur().getNoUtilisateur() == userSession.getNoUtilisateur())
							&& (article.getDateFinEncheres().isBefore(LocalDateTime.now()))) {
						article.setEtatVente("finie");
						articlesFiltresOptions.add(article);
					}
				}
			}
		}

		// filtre la liste des articles pour ne pas avoir de doublon
		Set<Integer> seenIds = new HashSet<>();

		List<ArticleVendu> articlesFiltresOptionsUnique = articlesFiltresOptions.stream()
				.filter(article -> seenIds.add(article.getNoArticle())).collect(Collectors.toList());

		return articlesFiltresOptionsUnique;
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
	public ArticleVendu getArticleByIdArticle(int idArticle, Utilisateur userSession) throws BusinessException {
		BusinessException be = new BusinessException();
		boolean valide = validerUtilisateurArticle(userSession.getNoUtilisateur(), idArticle, be);
		valide = encherePasEnCours(idArticle, be);

		if (valide) {
			ArticleVendu article = articleVenduDAO.read(idArticle);
			article.setLieuRetrait(retraitDAO.getRetraitById(idArticle));
			article.setCategorieArticle(categorieDAO.getCategorie(article.getCategorieArticle().getNoCategorie()));
			return article;
		} else {
			throw be;
		}

	}

	@Override
	public void updateArticle(ArticleVendu article) throws BusinessException {
		BusinessException be = new BusinessException();

		boolean valide = dateDebutConforme(article.getDateDebutEncheres(), be);
		valide &= dateFinConforme(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);

		if (valide) {
			articleVenduDAO.updateArticle(article);
			retraitDAO.updateRetrait(article);
		} else {
			throw be;
		}

	}

	@Override
	public void deleteArticle(int noUtilisateur, int idArticle) throws BusinessException {
		BusinessException be = new BusinessException();
		boolean valide = validerUtilisateurArticle(noUtilisateur, idArticle, be);
		valide = encherePasEnCours(idArticle, be);

		if (valide) {
			retraitDAO.deleteRetrait(idArticle);
			articleVenduDAO.deleteArticle(idArticle);
		} else {
			throw be;
		}

	}

	// ****************************** CATÉGORIE ******************************

	/**
	 * Méthode permettant de créer une nouvelle catégorie
	 */
	@Override
	public void createNouvelleCategorie(Categorie categorie) {
		categorieDAO.createNouvelleCategorie(categorie);
	}

	/**
	 * Méthode permettant de rechercher ArticleVendu + Utilisateur pour pouvoir
	 * implemter Utilisateur dans l'ArticleVendu
	 * 
	 * @param int id de l' ArticleVendu
	 * @return ArticleVendu
	 */
	public ArticleVendu chercherArticleComplet(int id) {

		ArticleVendu article = articleById(id);
		Utilisateur acheteur = getAcheteur(article.getPrixVente(), id);

		if (acheteur == null) {
			acheteur = new Utilisateur();
		}
		article.setAcheteur(acheteur);
		return article;

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
	 * Méthode permettant de modifier une catégorie
	 */
	@Override
	public void updateCategorie(Categorie categorie) {
		categorieDAO.updateCategorie(categorie);

	}

	/**
	 * Méthode permettant de supprimer une catégorie si celle-ci n'est pas utilisée
	 * par un article
	 */
	@Override
	public void deleteCategorie(int idCategorie) throws BusinessException {
		BusinessException be = new BusinessException();
		boolean valide = categorieNotUsed(idCategorie, be);

		if (valide) {
			categorieDAO.deleteCategorie(idCategorie);
		} else {
			throw be;
		}
	}

	// ****************************** ENCHERE ******************************

	@Override
	public List<Enchère> getEncheresByIdArticle(int idArticle, Utilisateur userSession) throws BusinessException {
		BusinessException be = new BusinessException();
		boolean valide = validerUtilisateurArticle(userSession.getNoUtilisateur(), idArticle, be);

		if (valide) {
			List<Enchère> encheres = enchèreDAO.getEncheresByIdArticleOrderDesc(idArticle);
			for (Enchère enchere : encheres) {
				enchere.setUtilisateur(utilisateurDAO.getUtilisateur(enchere.getUtilisateur().getNoUtilisateur()));
			}
			return encheres;
		} else {
			throw be;
		}

	}

	/**
	 * Méthode permettant de créer une enchère
	 * 
	 * @paramUtilisateur
	 * @param int
	 * @param int
	 */
	@Override
	@Transactional(rollbackFor = BusinessException.class)
	public void creerEnchere(Utilisateur userSession, int montant, int idArticle) throws BusinessException {
		BusinessException be = new BusinessException();
		ArticleVendu article = articleVenduDAO.read(idArticle);

		boolean valide = validerMontant(montant, article, userSession, be);

		try {
			if (valide) {

				int countEnchere = enchèreDAO.getCountEnchere(article.getPrixVente(), idArticle);
				if (countEnchere != 0) {
					Enchère AncienneEnchere = this.enchèreDAO.getUtilisateurParPrix(article.getPrixVente(), idArticle);

					Utilisateur ancienAcheteur = this.utilisateurDAO
							.getUtilisateur(AncienneEnchere.getUtilisateur().getNoUtilisateur());
					int creditRajout = transactionAjout(ancienAcheteur.getCredit(),
							AncienneEnchere.getMontant_enchere());

					ancienAcheteur.setCredit(creditRajout);
					// TODOO A METTRE DANS UTILISATEURDAO?
					enchèreDAO.updateCredit(ancienAcheteur);
				}
				int creditRetrait = transactionRetrait(userSession.getCredit(), montant);
				userSession.setCredit(creditRetrait);
				enchèreDAO.updateCredit(userSession);

				article.setPrixVente(montant);
				enchèreDAO.creerEnchere(montant, article, userSession);

			} else {
				throw be;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			be.addErreur("erreur.enchere.creation");
			throw be;
		}

	}

	/**
	 * Méthode permettant de déterminer l'affichage selon les conditions : Si
	 * userSession n'est pas acheteur et prixVente != 0 Si prixVente == 0 Si
	 * userSession est acheteur
	 * 
	 * @param ArticleVendu
	 * @Param boolean
	 */
	public int definirAffichage(ArticleVendu article, boolean isAcheteur) {

		int affichage = 0;

		if (article.getPrixVente() != 0 && !isAcheteur) {
			affichage = 1;

		}

		if (article.getPrixVente() == 0) {
			affichage = 0;

		}

		if (isAcheteur) {
			affichage = 3;

		}
		return affichage;

	}

	// ****************************** UTILISATEUR ******************************

	/**
	 * 
	 * Méthode permettant de récupérer un utilisateur grâce aux enchère qu'il a
	 * faite
	 * 
	 * @Param int
	 * @Param int
	 * @Return Utilisateur
	 */
	@Override
	public Utilisateur getAcheteur(int prixVente, int noArticle) {
		int countEnchere = this.enchèreDAO.getCountEnchere(prixVente, noArticle);
		if (countEnchere != 0) {
			Enchère enchereAcheteur = this.enchèreDAO.getUtilisateurParPrix(prixVente, noArticle);
			Utilisateur acheteur = this.utilisateurDAO
					.getUtilisateur(enchereAcheteur.getUtilisateur().getNoUtilisateur());

			return acheteur;
		}
		return null;

	}

	/**
	 * Méthode permettant de retirer le montant de l'enchère des crédits de
	 * l'utilisateur
	 * 
	 * @param creditAcheteur
	 * @param montant
	 * @return int Crédit utilisateur
	 */
	private int transactionRetrait(int creditAcheteur, int montant) {

		creditAcheteur -= montant;

		return creditAcheteur;
	}

	/**
	 * Méthode permettant de rajouter les crédits à l'utilisateur précédent
	 * 
	 * @param creditAcheteurPreview
	 * @param prixVente
	 * @return int Crédit
	 */
	private int transactionAjout(int creditActuel, int montantRendu) {
		return creditActuel + montantRendu;
	}

	// ****************************** VALIDATION ******************************

	/**
	 * Méthode validant la date de fin d'un article en vente
	 * 
	 * @param dateDebut date de début de la vente de l'article
	 * @param dateFin   date de fin de la vente de l'article
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
	 * @param dateDebut date de début de la vente de l'article
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

	/**
	 * Vérifie si l'user est l'acheteur, si oui le bouton enchérir est désactivé
	 * 
	 * @Return boolean valide
	 */
	@Override
	public boolean isAcheteur(String PseudoAcheteur, String pseudoUser) {
		boolean valide = false;

		if (PseudoAcheteur != null) {

			if (PseudoAcheteur.equals(pseudoUser)) {

				valide = true;
			}
		}
		return valide;
	}

	/**
	 * Vérifie si l'enchere est en cours, si non le bouton enchérir est désactiver
	 * 
	 * @Return boolean valide
	 */
	@Override
	public boolean isEnchereEnCours(LocalDateTime dateFin, LocalDateTime dateDebut) {

		boolean valide = true;
		if (dateFin.isBefore(LocalDateTime.now()) || dateDebut.isAfter(dateDebut)) {

			valide = false;
		}
		return valide;
	}

	/**
	 * Verifie si l'user en session est l'actuel détenteur de la meilleur offre, si
	 * oui le bouton enchérir est désactive
	 * 
	 * @return boolean valide
	 */
	@Override
	public boolean ismeilleurOffre(Utilisateur pseudoMeilleurOfrre, String pseudoUser) {
		boolean valide = true;
		if (pseudoMeilleurOfrre != null) {
			if (pseudoMeilleurOfrre.equals(pseudoUser)) {
				valide = false;
			}
		}
		return valide;
	}

	/**
	 * Vérifie que le montant de l'enchère entrée par l'utilisateur est supérieur au
	 * prix de vente actuelle Vérifie aussi que l'utilisateur a assez de crédit sur
	 * son compte pour payer le montant
	 * 
	 * @param montant
	 * @param article
	 * @param userSession
	 * @return boolean valide
	 */
	private boolean validerMontant(int montant, ArticleVendu article, Utilisateur userSession, BusinessException be) {
		boolean valide = true;

		if (montant < article.getPrixVente() || montant > userSession.getCredit()) {
			valide = false;
			be.addErreur("erreur.montant.enchere");
		}

		return valide;
	}

	/**
	 * Vérifie que la catégorie n'est pas utilisée par un article
	 * 
	 * @param idCategorie id de la catégorie à tester
	 * @param be
	 * @return true si conforme, false sinon
	 */
	private boolean categorieNotUsed(int idCategorie, BusinessException be) {
		if (articleVenduDAO.getCountByIdCategorie(idCategorie) != 0) {
			be.addErreur("erreur.categorie.utilisee");
			return false;
		}
		return true;
	}

	private boolean encherePasEnCours(int idArticle, BusinessException be) {
		if (articleVenduDAO.read(idArticle).getDateDebutEncheres().isBefore(LocalDateTime.now())) {
			be.addErreur("erreur.supprimer.article.commencee");
			return false;
		}
		return true;
	}

	private boolean validerUtilisateurArticle(int noUtilisateur, int idArticle, BusinessException be) {
		if (articleVenduDAO.read(idArticle).getVendeur().getNoUtilisateur() != noUtilisateur) {
			be.addErreur("erreur.supprimer.article.utilisateur");
			return false;
		}
		return true;
	}

}
