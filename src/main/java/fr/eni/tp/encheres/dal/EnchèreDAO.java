package fr.eni.tp.encheres.dal;

import java.util.List;

import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;

public interface EnchèreDAO {

	void creerUtilisateur(Utilisateur user);
	
	List<Enchère> consulterEncheresById(int idUser);

}
