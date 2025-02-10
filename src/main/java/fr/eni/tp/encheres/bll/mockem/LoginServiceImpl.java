package fr.eni.tp.encheres.bll.mockem;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fr.eni.tp.encheres.bll.LoginService;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.EnchereDAO;

@Service
public class LoginServiceImpl implements LoginService{


	private EnchereDAO enchereDAO;
	private PasswordEncoder passwordEncoder;
	
	
	
	public LoginServiceImpl(EnchereDAO enchereDAO, PasswordEncoder passwordEncoder) {
		this.enchereDAO = enchereDAO;
		this.passwordEncoder = passwordEncoder;
	}



	@Override
	public void creerUtilisateur(Utilisateur user) {
		String mdpEncode = passwordEncoder.encode(user.getMotDePasse());
		user.setMotDePasse(mdpEncode);
		System.out.println(user);
		enchereDAO.creerUtilisateur(user);
	}

}
