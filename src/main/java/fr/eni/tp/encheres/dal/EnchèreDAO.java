package fr.eni.tp.encheres.dal;

import java.util.List;

import fr.eni.tp.encheres.bo.Enchère;

public interface EnchèreDAO {

	int countByIdArticle(int idArticle);

	List<Enchère> getEncheres(int idArticle);


	List<Enchère> consulterEncheresById(int idUser);

	Enchère getUtilisateurParPrix(int prixVente, int idArticle);

	int getCountEnchere(int prixVente, int idArticle);
}
