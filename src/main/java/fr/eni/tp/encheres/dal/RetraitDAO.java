package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Retrait;

public interface RetraitDAO {

	void create(ArticleVendu article);
	
	Retrait getretraitById(int noArticle);
}
