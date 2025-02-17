package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
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

	@Override
	public List<ArticleVendu> getEncheres() {
		List<ArticleVendu> articles = articleVenduDAO.getArticles();
		for (ArticleVendu articleVendu : articles) {
			articleVendu.setVendeur(utilisateurDAO.getUtilisateur(articleVendu.getVendeur().getNoUtilisateur()));
			articleVendu.setCategorieArticle(
					categorieDAO.getCategorie(articleVendu.getCategorieArticle().getNoCategorie()));
		}
		return articles;
	}

	@Override
	public List<Categorie> getCategories() {
		return categorieDAO.getCategories();
	}

	@Override
	public Categorie getCategorie(int idCategorie) {
		return categorieDAO.getCategorie(idCategorie);
	}

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

	@Override
	public List<ArticleVendu> getEncheresFiltrees(String filtre, int idCategorie) {
		List<ArticleVendu> articles = new ArrayList<ArticleVendu>();

		if (filtre.isBlank() && idCategorie == 0) {
			articles = articleVenduDAO.getArticles();
		}
		if (!filtre.isBlank() && idCategorie == 0) {
			String filtreSql = "%" + filtre + "%";
			articles = articleVenduDAO.getArticlesFiltresByString(filtreSql);
		}
		if (filtre.isBlank() && !(idCategorie == 0)) {
			articles = articleVenduDAO.getArticlesFiltresById(idCategorie);
		}
		if (!filtre.isBlank() && !(idCategorie == 0)) {
			String filtreSql = "%" + filtre + "%";
			articles = articleVenduDAO.getArticlesFiltresByStringAndId(filtreSql, idCategorie);
		}

		for (ArticleVendu articleVendu : articles) {
			articleVendu.setVendeur(utilisateurDAO.getUtilisateur(articleVendu.getVendeur().getNoUtilisateur()));
			articleVendu.setCategorieArticle(
					categorieDAO.getCategorie(articleVendu.getCategorieArticle().getNoCategorie()));
		}
		return articles;
	}

	@Override
	public ArticleVendu articleById(int noArticle) {
		
		ArticleVendu article = this.articleVenduDAO.read(noArticle);
		Utilisateur vendeur = this.utilisateurDAO.getUtilisateur(article.getVendeur().getNoUtilisateur());
		Retrait lieuRetrait = this.retraitDAO.getretraitById(noArticle);
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
		if (countEnchere!= 0) {
			Enchère enchereAcheteur = this.enchèreDAO.getUtilisateurParPrix(prixVente, noArticle);
			Utilisateur acheteur = this.utilisateurDAO.getUtilisateur(enchereAcheteur.getUtilisateur().getNoUtilisateur());

			return acheteur.getPseudo();
		}
		return null;

	}
	
	/**
	 * Vérifie si l'user est l'acheteur, si oui  le bouton enchérir est désactivé
	 * @Return boolean valide
	 */
	@Override
	public boolean isAcheteur(int idUser, int idUserSesssion) {
		boolean valide = true;
		if (idUser == idUserSesssion) {
			valide = false;
		}	
		return valide;
	}
	
	/**
	 * Vérifie si l'enchere est en cours, si non le bouton enchérir est désactiver
	 * @Return boolean valide
	 */
	@Override
	public boolean isEnchereEnCours(LocalDateTime dateFin) {
		
		boolean valide = true;
		if (dateFin.isBefore(LocalDateTime.now())) {
			valide = false;
		}
		
		return valide;
	}

	/**
	 * Verifie si l'user en session est l'actuel détenteur de la meilleur offre,
	 *  si oui le bouton enchérir est désactive
	 * @return boolean valide
	 */
	@Override
	public boolean ismeilleurOffre(String pseudoMeilleurOfrre, String pseudoUser) {
		boolean valide = true;
		if (pseudoMeilleurOfrre != null) {
			if (pseudoMeilleurOfrre.equals(pseudoUser)) {
				valide = false;
			}
		}
	return valide;
	}

	
	/**
	 * Méthode permettant de créer une enchère
	 * @paramUtilisateur
	 * @param int
	 * @param int
	 */
	@Override
	public void creerEnchere(Utilisateur userSession, int montant, int idArticle) throws BusinessException {
		BusinessException be = new BusinessException();
		ArticleVendu article = articleVenduDAO.read(idArticle);
		
		
		
		boolean valide = validerMontant(montant, article, userSession, be);
		 				
		try {
			if (valide) {
					
					int countEnchere = enchèreDAO.getCountEnchere(article.getPrixVente(), idArticle);
					if (countEnchere != 0) {
						Enchère AncienneEnchere = this.enchèreDAO.getUtilisateurParPrix(article.getPrixVente(), idArticle);
					
					
					
					
						Utilisateur ancienAcheteur = this.utilisateurDAO.getUtilisateur(AncienneEnchere.getUtilisateur().getNoUtilisateur());
						int creditRajout = transactionAjout(ancienAcheteur.getCredit(), AncienneEnchere.getMontant_enchere());

						ancienAcheteur.setCredit(creditRajout);
						//TODOO A METTRE DANS UTILISATEURDAO?
						enchèreDAO.updateCredit(ancienAcheteur);
					}	
					int creditRetrait = transactionRetrait(userSession.getCredit(), montant);
					userSession.setCredit(creditRetrait);
					enchèreDAO.updateCredit(userSession);

					article.setPrixVente(montant);
					enchèreDAO.creerEnchere(montant, article, userSession);
		

			}else {
				throw be;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			be.addErreur("erreur.enchere.creation");
			throw be;
		}

	}

	
	/**
	 * Vérifie que le montant de l'enchère entrée par l'utilisateur est supérieur au prix de vente actuelle
	 * Vérifie aussi que l'utilisateur a assez de crédit sur son compte pour payer le montant
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
	 * Méthode permettant de retirer le montant de l'enchère des crédit de l'utilisateur
	 * @param creditAcheteur
	 * @param montant
	 * @return int Crédit utilisateur
	 */
	private int transactionRetrait(int creditAcheteur, int montant) {
		
		creditAcheteur -= montant; 

		return creditAcheteur;
	}
	
	/**
	 * Méthodde permettant de rajouter les crédit à l'utilisateur précédent
	 * @param creditAcheteurPreview
	 * @param prixVente
	 * @return int Crédit
	 */
	private int transactionAjout(int creditActuel, int montantRendu) {
		return creditActuel + montantRendu;
	}
	
	

}
