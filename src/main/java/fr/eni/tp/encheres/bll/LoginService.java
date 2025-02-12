package fr.eni.tp.encheres.bll;

import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.exception.BusinessException;


public interface LoginService {

	void creerUtilisateur(Utilisateur user, String mdpConfirm) throws BusinessException;
	
	Utilisateur consulterUtilisateur(int id);
	
	public Utilisateur charger(String pseudo);
	
	void modifierUtilisateur(Utilisateur user, String mdpConfirm, String newMdp) throws BusinessException;
	
}
