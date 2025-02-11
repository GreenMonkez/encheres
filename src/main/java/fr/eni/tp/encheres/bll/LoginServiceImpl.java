package fr.eni.tp.encheres.bll;


import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.UtilisateurDAO;
import fr.eni.tp.encheres.exception.BusinessException;

@Service
public class LoginServiceImpl implements LoginService{

	private UtilisateurDAO utilisateurDAO;
	
	
	
	

	public LoginServiceImpl(UtilisateurDAO utilisateurDAO) {

		this.utilisateurDAO = utilisateurDAO;
		
	}



	@Override
	public void creerUtilisateur(Utilisateur user, String mdpConfirm) throws BusinessException{
		BusinessException be = new BusinessException();
		


		String mdpEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getMotDePasse());
		user.setMotDePasse(mdpEncode);

		
		boolean valide = validerUtilisateurPseudo(user.getPseudo(), be);
		valide &= validerUtilisateurEmail(user.getEmail(), be);
		valide &= validerConfirmMdp(mdpConfirm, user.getMotDePasse(), be);
		
		try {
			if (valide) {
				utilisateurDAO.creerUtilisateur(user);
			}else {
				throw be;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			be.addErreur("erreur.utilisateur.creation");
			throw be;
		}
		
	
	}
	
	
	public boolean validerUtilisateurPseudo(String pseudo, BusinessException be) {
		
		boolean valide = true;
		int nbPseudo = utilisateurDAO.validerPseudo(pseudo);
		if (nbPseudo == 1) {
			valide = false;
			be.addErreur("erreur.utilisateur.pseudo.exist");
		}
		
		return valide;
		
	}
	
public boolean validerUtilisateurEmail(String email, BusinessException be) {
		
		boolean valide = true;
		int nbEmail = utilisateurDAO.validerEmail(email);
		if (nbEmail == 1) {
			valide = false;
			be.addErreur("erreur.utilisateur.email.exist");
		}
		
		return valide;
		
	}




@Override
public Utilisateur consulterUtilisateur(int id) {
	Utilisateur user = utilisateurDAO.getUtilisateur(id);
	return user;
}

public boolean validerConfirmMdp(String mdp, String mdpConfirm, BusinessException be) {
	
	boolean valide = true;
	if (mdp != mdpConfirm) {
		valide = false;
		be.addErreur("erreur.password.confirm");
	}

	return valide;
	
}



@Override
public Utilisateur charger(String pseudo) {
	return this.utilisateurDAO.getUtilisateur(pseudo);
	
}


}
