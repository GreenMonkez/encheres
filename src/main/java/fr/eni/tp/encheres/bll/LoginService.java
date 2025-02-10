package fr.eni.tp.encheres.bll;

import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;

public interface LoginService {

	void creerUtilisateur(Utilisateur user) throws BusinessException;
	
}
