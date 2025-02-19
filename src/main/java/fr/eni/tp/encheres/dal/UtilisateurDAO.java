package fr.eni.tp.encheres.dal;

import java.sql.Timestamp;

import fr.eni.tp.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

	void creerUtilisateur(Utilisateur user);

	int validerPseudo(String pseudo);

	int validerEmail(String email);

	Utilisateur getUtilisateur(int noUtilisateur);

	Utilisateur getUtilisateurByPseudo(String pseudo);

	int validerMdp(String mdp);

	void modifierUtilisateur(Utilisateur user);

	void updateCredit(int noUtilisateur, int nouveauSoldeCredit);
	
	Utilisateur getUtilisateurByEmail(String email);
	
	void sauvegarderPasswordResetToken(int idUser, String token);
	
	boolean validerPasswordResetToken(String idUser);
	
	Utilisateur readByToken (String Token);

	Timestamp findDateToken(String token);
	
	void resetPassword(int idUser, String password);

}
