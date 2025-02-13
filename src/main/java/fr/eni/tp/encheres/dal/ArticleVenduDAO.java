package fr.eni.tp.encheres.dal;

import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;

public interface ArticleVenduDAO {

	List<ArticleVendu> getArticles();

	void create(ArticleVendu article);
	
	ArticleVendu read(int id);

	List<ArticleVendu> getArticlesFiltresByString(String filtreSql);

	List<ArticleVendu> getArticlesFiltresById(int idCategorie);

	List<ArticleVendu> getArticlesFiltresByStringAndId(String filtreSql, int idCategorie);

}
