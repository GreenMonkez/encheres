package fr.eni.tp.encheres.bll;

import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;

public interface EnchereService {

	List<ArticleVendu> getEncheres();

	List<Categorie> getCategories();

	Categorie getCategorie(int idCategorie);

	void createNouvelleVente(ArticleVendu article) throws BusinessException;

	List<ArticleVendu> getEncheresFiltrees(String filtre, int idCategorie);

	String getPseudoAcheteur(int prixVente, int idCategorie);

	ArticleVendu articleById(int id);

	List<ArticleVendu> getEncheresFiltreesOptions(List<ArticleVendu> articles, List<String> options,
			Utilisateur userSession);

}
