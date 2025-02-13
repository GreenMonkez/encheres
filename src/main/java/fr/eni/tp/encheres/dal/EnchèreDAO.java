package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;

public interface EnchèreDAO {

	void creerUtilisateur(Utilisateur user);

	Enchère getUtilisateurParPrix(int prixVente,int idArticle);
	
	int getCountEnchere(int prixVente,int idArticle);
}
