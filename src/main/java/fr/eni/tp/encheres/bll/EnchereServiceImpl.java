package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.ArticleVenduDAO;
import fr.eni.tp.encheres.dal.CategorieDAO;
import fr.eni.tp.encheres.dal.EnchèreDAO;
import fr.eni.tp.encheres.dal.RetraitDAO;
import fr.eni.tp.encheres.dal.UtilisateurDAO;
import fr.eni.tp.encheres.exception.BusinessException;

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
	public void createNouvelleVente(ArticleVendu article) throws BusinessException {
		BusinessException be = new BusinessException();

		boolean valide = dateDebutConforme(article.getDateDebutEncheres(), be);
		valide &= dateFinConforme(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);

		if (valide) {
			articleVenduDAO.create(article);
			retraitDAO.create(article);
		} else {
			throw be;
		}

	}

	private boolean dateFinConforme(LocalDateTime dateDebut, LocalDateTime dateFin, BusinessException be) {
		if (dateFin == null) {
			be.addErreur("date.fin.null");
			return false;
		}
		if (dateFin.isBefore(dateDebut)) {
			be.addErreur("date.fin.invalide");
			return false;
		}
		return true;
	}

	private boolean dateDebutConforme(LocalDateTime dateDebut, BusinessException be) {
		if (dateDebut == null) {
			be.addErreur("date.debut.null");
			return false;
		}
		if (dateDebut.isBefore(LocalDateTime.now())) {
			be.addErreur("date.debut.passee");
			return false;
		}
		return true;
	}

	@Override
	public List<ArticleVendu> getEncheresFiltrees(String filtre, int idCategorie) {
		List<ArticleVendu> articles = new ArrayList<ArticleVendu>();

		if (filtre.isBlank() && idCategorie == 0) {
			articles = articleVenduDAO.getArticles();
		}
		if (!filtre.isBlank() && idCategorie == 0) {
			String filtreSql = "%" + filtre + "%";
			articles = articleVenduDAO.getArticlesFiltresByString(filtreSql);
		}
		if (filtre.isBlank() && !(idCategorie == 0)) {
			articles = articleVenduDAO.getArticlesFiltresById(idCategorie);
		}
		if (!filtre.isBlank() && !(idCategorie == 0)) {
			String filtreSql = "%" + filtre + "%";
			articles = articleVenduDAO.getArticlesFiltresByStringAndId(filtreSql, idCategorie);
		}

		for (ArticleVendu articleVendu : articles) {
			articleVendu.setVendeur(utilisateurDAO.getUtilisateur(articleVendu.getVendeur().getNoUtilisateur()));
			articleVendu.setCategorieArticle(
					categorieDAO.getCategorie(articleVendu.getCategorieArticle().getNoCategorie()));
		}
		return articles;
	}

	@Override
	public ArticleVendu articleById(int noArticle) {
		
		ArticleVendu article = this.articleVenduDAO.read(noArticle);
		Utilisateur vendeur = this.utilisateurDAO.getUtilisateur(article.getVendeur().getNoUtilisateur());
		Retrait lieuRetrait = this.retraitDAO.getretraitById(noArticle);
		String libelleCategorie = this.categorieDAO.getCategorie(article.getCategorieArticle().getNoCategorie())
				.getLibelle();
		article.setLieuRetrait(lieuRetrait);
		article.getCategorieArticle().setLibelle(libelleCategorie);
		article.setVendeur(vendeur);
		return article;
	}

	@Override
	public String getPseudoAcheteur(int prixVente, int noArticle) {
		int countEnchere = this.enchèreDAO.getCountEnchere(prixVente, noArticle);
		if (countEnchere!= 0) {
			Enchère enchereAcheteur = this.enchèreDAO.getUtilisateurParPrix(prixVente, noArticle);
			Utilisateur acheteur = this.utilisateurDAO.getUtilisateur(enchereAcheteur.getUtilisateur().getNoUtilisateur());

			return acheteur.getPseudo();
		}
		return null;

	}

}
