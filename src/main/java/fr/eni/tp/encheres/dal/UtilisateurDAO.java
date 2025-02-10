package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
	
	void creerUtilisateur(Utilisateur user);
	
	int validerPseudo(String pseudo);
	
	int validerEmail(String email);
	
	//Utilisateur readById(int id);

}
