package fr.eni.tp.encheres.dal;

import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;

public interface EnchèreDAO {

	int countByIdArticle(int idArticle);

	List<Enchère> getEncheres(int idArticle);

	List<Enchère> consulterEncheresById(int idUser);

	Enchère getUtilisateurParPrix(int prixVente, int idArticle);

	int getCountEnchere(int prixVente, int idArticle);

	void creerEnchere(int montant, ArticleVendu article, Utilisateur userSession);

	void updateCredit(Utilisateur user);

	List<Enchère> getEncheresByIdArticleOrderDesc(int idArticle);

}
