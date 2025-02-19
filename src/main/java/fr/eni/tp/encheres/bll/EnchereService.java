package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;
import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;

import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;

public interface EnchereService {
	// ********** CREATE **********

	void creerEnchere(Utilisateur userSession, int montant, int idArticle) throws BusinessException;

	// ********** READ **********

	List<Enchère> getEncheresByIdArticle(int idArticle, Utilisateur userSession) throws BusinessException;

	// ********** UPDATE **********

	// ********** DELETE **********

	// ********** ??? **********

	boolean isAcheteur(String PseudoAcheter, String pseudoUser);

	boolean isEnchereEnCours(LocalDateTime dateFin, LocalDateTime dateDebut);

	boolean ismeilleurOffre(Utilisateur pseudoAcheteur, String pseudoUser);

	int definirAffichage(ArticleVendu article, boolean isAcheteur);

}
