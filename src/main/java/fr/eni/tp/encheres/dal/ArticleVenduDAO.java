package fr.eni.tp.encheres.dal;

import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;

public interface ArticleVenduDAO {

	List<ArticleVendu> getArticles();

}
