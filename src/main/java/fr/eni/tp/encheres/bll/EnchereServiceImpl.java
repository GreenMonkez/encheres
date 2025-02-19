package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.tp.encheres.bo.ArticleVendu;

import fr.eni.tp.encheres.bo.Enchère;

import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.ArticleVenduDAO;

import fr.eni.tp.encheres.dal.EnchèreDAO;

import fr.eni.tp.encheres.dal.UtilisateurDAO;
import fr.eni.tp.encheres.exception.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {

	private ArticleVenduDAO articleVenduDAO;

	private EnchèreDAO enchèreDAO;

	private UtilisateurDAO utilisateurDAO;

	public EnchereServiceImpl(ArticleVenduDAO articleVenduDAO, EnchèreDAO enchèreDAO, UtilisateurDAO utilisateurDAO) {
		this.articleVenduDAO = articleVenduDAO;
		this.enchèreDAO = enchèreDAO;
		this.utilisateurDAO = utilisateurDAO;
	}

	// ********** CREATE **********

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
					Enchère ancienneEnchere = this.enchèreDAO.getUtilisateurParPrix(article.getPrixVente(), idArticle);

					Utilisateur ancienAcheteur = this.utilisateurDAO
							.getUtilisateur(ancienneEnchere.getUtilisateur().getNoUtilisateur());
					int creditRajout = ancienAcheteur.getCredit() + ancienneEnchere.getMontant_enchere();

					ancienAcheteur.setCredit(creditRajout);
					// TODOO A METTRE DANS UTILISATEURDAO?
					enchèreDAO.updateCredit(ancienAcheteur);
				}
				int creditRetrait = userSession.getCredit() - montant;
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

	// ********** READ **********

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

	// ********** UPDATE **********

	// ********** DELETE **********

	// ********** VALIDATION **********

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

	private boolean validerUtilisateurArticle(int noUtilisateur, int idArticle, BusinessException be) {
		if (articleVenduDAO.read(idArticle).getVendeur().getNoUtilisateur() != noUtilisateur) {
			be.addErreur("erreur.supprimer.article.utilisateur");
			return false;
		}
		return true;
	}

	// ********** ??? **********

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
}
