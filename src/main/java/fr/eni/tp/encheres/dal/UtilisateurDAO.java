package fr.eni.tp.encheres.dal;

import fr.eni.tp.encheres.bo.PasswordToken;
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

	void sauvegarderPasswordResetToken(PasswordToken passwordToken);

	PasswordToken findToken(String token);

	void resetPassword(PasswordToken passwordToken, String password);

}
