package fr.eni.tp.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.dal.ArticleVenduDAO;
import fr.eni.tp.encheres.dal.CategorieDAO;
import fr.eni.tp.encheres.dal.EnchèreDAO;
import fr.eni.tp.encheres.dal.RetraitDAO;
import fr.eni.tp.encheres.dal.UtilisateurDAO;

@Service
public class EnchereServiceImpl implements EnchereService {

	private ArticleVenduDAO articleVenduDAO;
	private CategorieDAO categorieDAO;
	private EnchèreDAO enchèreDAO;
	private RetraitDAO retraitDAO;
	private UtilisateurDAO utilisateurDAO;

	public EnchereServiceImpl(ArticleVenduDAO articleVenduDAO, CategorieDAO categorieDAO, EnchèreDAO enchèreDAO,
			RetraitDAO retraitDAO, UtilisateurDAO utilisateurDAO) {
		this.articleVenduDAO = articleVenduDAO;
		this.categorieDAO = categorieDAO;
		this.enchèreDAO = enchèreDAO;
		this.retraitDAO = retraitDAO;
		this.utilisateurDAO = utilisateurDAO;
	}

	@Override
	public List<ArticleVendu> getEncheres() {
		List<ArticleVendu> articles = articleVenduDAO.getArticles();
		for (ArticleVendu articleVendu : articles) {
			articleVendu.setVendeur(utilisateurDAO.getUtilisateur(articleVendu.getVendeur().getNoUtilisateur()));
			articleVendu.setCategorieArticle(
					categorieDAO.getCategorie(articleVendu.getCategorieArticle().getNoCategorie()));
		}
		return articles;
	}

	@Override
	public List<Categorie> getCategories() {
		return categorieDAO.getCategories();
	}

	@Override
	public Categorie getCategorie(int idCategorie) {
		return categorieDAO.getCategorie(idCategorie);
	}

	@Override
	public void createNouvelleVente(ArticleVendu article) {
		articleVenduDAO.create(article);
		retraitDAO.create(article);
	}

}
