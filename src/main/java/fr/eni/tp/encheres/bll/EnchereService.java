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

	String getPseudoAcheteur(int prixVente, int idCategorie);
	
	ArticleVendu articleById(int id);
	
	boolean isAcheteur(int idUser, int idUserSesssion);
	
	boolean isEnchereEnCours(LocalDateTime dateFin);
	
	boolean ismeilleurOffre(String pseudoMeilleurOfrre, String pseudoUser);

	void creerEnchere(Utilisateur userSession, int montant, int idArticle) throws BusinessException;

}
