package fr.eni.tp.encheres.bll.mockem;


import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fr.eni.tp.encheres.bll.LoginService;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.UtilisateurDAO;
import fr.eni.tp.encheres.exception.BusinessException;

@Service
public class LoginServiceImpl implements LoginService{


	
	private UtilisateurDAO utilisateurDAO;
	private PasswordEncoder passwordEncoder;
	
	
	
	public LoginServiceImpl(UtilisateurDAO utilisateurDAO/*, PasswordEncoder passwordEncoder*/) {
		this.utilisateurDAO = utilisateurDAO;
		this.passwordEncoder = passwordEncoder;
	}



	@Override
	public void creerUtilisateur(Utilisateur user) throws BusinessException{
		BusinessException be = new BusinessException();
		
		String mdpEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getMotDePasse());
		user.setMotDePasse(mdpEncode);
		
		boolean valide = validerUtilisateurPseudo(user.getPseudo(), be);
		valide &= validerUtilisateurEmail(user.getEmail(), be);
		
		try {
			if (valide) {
				utilisateurDAO.creerUtilisateur(user);
			}else {
				throw be;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			be.addErreur("erreur.utilisateur.creation");
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





}
