package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

	Utilisateur getUtilisateur(int noUtilisateur);
	
	Utilisateur getUtilisateur(String pseudo);

}
