package fr.eni.tp.encheres.bll;

import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;

public interface ArticleVenduService {
	// ********** CREATE **********

	void createNouvelleVente(ArticleVendu article) throws BusinessException;

	// ********** READ **********
	ArticleVendu articleById(int id);

	ArticleVendu chercherArticleComplet(int id);

	ArticleVendu getArticleByIdArticle(int idArticle, Utilisateur userSession) throws BusinessException;

	List<ArticleVendu> getEncheres();

	List<ArticleVendu> getEncheresFiltrees(String filtre, int idCategorie);

	List<ArticleVendu> getEncheresFiltreesOptions(String filtre, int idCategorie, List<String> options,
			Utilisateur userSession);

	// ********** UPDATE **********

	void updateArticle(ArticleVendu article) throws BusinessException;

	// ********** DELETE **********

	void deleteArticle(int noUtilisateur, int idArticle) throws BusinessException;

}
// ********** CREATE **********

// ********** READ **********

// ********** UPDATE **********

// ********** DELETE **********

// ********** VALIDATION **********

// ArticleVenduServiceImpl méthode utilisateur à virer

// Photo de base à afficher

// Ménage controller

// Ménage DAO