package fr.eni.tp.encheres.dal;

import java.util.List;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

	
	void creerUtilisateur(Utilisateur user);
	
	int validerPseudo(String pseudo);
	
	int validerEmail(String email);
	
	Utilisateur getUtilisateur(int noUtilisateur);
	
	Utilisateur getUtilisateurByPseudo(String pseudo);
	
	int validerMdp(String mdp);
	
	void modifierUtilisateur(Utilisateur user);
	
	
	
	
	
	

}
