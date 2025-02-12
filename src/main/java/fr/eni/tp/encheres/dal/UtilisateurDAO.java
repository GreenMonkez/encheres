package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

	
	void creerUtilisateur(Utilisateur user);
	
	int validerPseudo(String pseudo);
	
	int validerEmail(String email);
	Utilisateur getUtilisateur(int noUtilisateur);
	
	Utilisateur getUtilisateur(String pseudo);
	
	int validerMdp(String mdp);
	
	void modifierUtilisateur(Utilisateur user);

}
