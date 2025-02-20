package fr.eni.tp.encheres.bll;

import org.springframework.stereotype.Service;

import fr.eni.tp.encheres.bo.Utilisateur;

import fr.eni.tp.encheres.dal.UtilisateurDAO;

@Service
public class LoginServiceImpl implements LoginService {

	private UtilisateurDAO utilisateurDAO;

	public LoginServiceImpl(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;

	}

	// ********** CREATE **********

	// ********** READ **********

	/**
	 * Trouver un Utilisateur grâce à son pseudo
	 * 
	 * @return Utilisateur
	 */
	@Override
	public Utilisateur charger(String pseudo) {
		return this.utilisateurDAO.getUtilisateurByPseudo(pseudo);

	}

	// ********** UPDATE **********

	// ********** DELETE **********

	// ********** VALIDATION **********

}
