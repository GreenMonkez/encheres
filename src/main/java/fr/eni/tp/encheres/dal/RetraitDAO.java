package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Retrait;

public interface RetraitDAO {

	void create(ArticleVendu article);

	Retrait getRetraitById(int noArticle);

	void deleteRetrait(int idArticle);

	void updateRetrait(ArticleVendu article);
}
