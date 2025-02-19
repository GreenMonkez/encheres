package fr.eni.tp.encheres.bll;

import java.util.List;

import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.dal.ArticleVenduDAO;
import fr.eni.tp.encheres.dal.CategorieDAO;
import fr.eni.tp.encheres.exception.BusinessException;

public class CategorieServiceImpl implements CategorieService {

	private ArticleVenduDAO articleVenduDAO;
	private CategorieDAO categorieDAO;

	public CategorieServiceImpl(ArticleVenduDAO articleVenduDAO, CategorieDAO categorieDAO) {
		this.articleVenduDAO = articleVenduDAO;
		this.categorieDAO = categorieDAO;
	}

	// ********** CREATE **********

	/**
	 * Méthode permettant de créer une nouvelle catégorie
	 */
	@Override
	public void createNouvelleCategorie(Categorie categorie) {
		categorieDAO.createNouvelleCategorie(categorie);
	}

	// ********** READ **********

	/**
	 * Méthode retournant une catégorie en fonction de son ID
	 */
	@Override
	public Categorie getCategorie(int idCategorie) {
		return categorieDAO.getCategorie(idCategorie);
	}

	/**
	 * Méthode retournant la liste des catégories
	 */
	@Override
	public List<Categorie> getCategories() {
		return categorieDAO.getCategories();
	}

	// ********** UPDATE **********

	/**
	 * Méthode permettant de modifier une catégorie
	 */
	@Override
	public void updateCategorie(Categorie categorie) {
		categorieDAO.updateCategorie(categorie);

	}

	// ********** DELETE **********

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

	// ********** VALIDATION **********

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
}
