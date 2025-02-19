package fr.eni.tp.encheres.bll;

import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;

public interface UtilisateurService {
	// ********** CREATE **********

	void creerUtilisateur(Utilisateur user, String mdpConfirm) throws BusinessException;

	// ********** READ **********

	Utilisateur consulterUtilisateur(int id);

	// ********** UPDATE **********

	void modifierUtilisateur(Utilisateur user, String mdpConfirm, String newMdp) throws BusinessException;

	void updateCredit(Utilisateur userSession, int achatCredit);

	// ********** DELETE **********

	void supprimerUtilisateur(Utilisateur userSupp) throws BusinessException;

}
