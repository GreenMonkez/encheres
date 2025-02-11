package fr.eni.tp.encheres.bll;

import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;

public interface EnchereService {

	List<ArticleVendu> getEncheres();

	List<Categorie> getCategories();

	Categorie getCategorie(int idCategorie);
}
