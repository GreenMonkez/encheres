package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

	// ********** CREATE **********

	void createUtilisateur(Utilisateur user);

	// ********** READ **********

	Utilisateur getUtilisateurById(int noUtilisateur);

	Utilisateur getUtilisateurByPseudo(String pseudo);

	// ********** UPDATE **********

	void updateUtilisateur(Utilisateur user);

	void updateCredit(int noUtilisateur, int nouveauSoldeCredit);

	void updateCredit(Utilisateur user);

	// ********** DELETE **********

	// ********** VALIDATION **********

	int getCountUtilisateurByPseudo(String pseudo);

	int getCountUtilisateurByEmail(String email);

	int getCountUtilisateurByMdp(String mdp);

}
