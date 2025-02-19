package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;
import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;

public interface EnchereService {

	List<ArticleVendu> getEncheres();

	List<Categorie> getCategories();

	Categorie getCategorie(int idCategorie);

	void createNouvelleVente(ArticleVendu article) throws BusinessException;

	List<ArticleVendu> getEncheresFiltrees(String filtre, int idCategorie);

	Utilisateur getAcheteur(int prixVente, int idCategorie);

	ArticleVendu articleById(int id);

	boolean isAcheteur(String PseudoAcheter, String pseudoUser);

	boolean isEnchereEnCours(LocalDateTime dateFin, LocalDateTime dateDebut);

	boolean ismeilleurOffre(Utilisateur pseudoAcheteur, String pseudoUser);

	void creerEnchere(Utilisateur userSession, int montant, int idArticle) throws BusinessException;

	List<ArticleVendu> getEncheresFiltreesOptions(String filtre, int idCategorie, List<String> options,
			Utilisateur userSession);

	void createNouvelleCategorie(Categorie categorie);

	void updateCategorie(Categorie categorie);

	void deleteCategorie(int idCategorie) throws BusinessException;

	int definirAffichage(ArticleVendu article, boolean isAcheteur);

	ArticleVendu chercherArticleComplet(int id);

	void deleteArticle(int noUtilisateur, int idArticle) throws BusinessException;

	List<Enchère> getEncheresByIdArticle(int idArticle, Utilisateur userSession) throws BusinessException;

	ArticleVendu getArticleByIdArticle(int idArticle, Utilisateur userSession) throws BusinessException;

	void updateArticle(ArticleVendu article) throws BusinessException;

}
