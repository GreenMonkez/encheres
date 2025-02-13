package fr.eni.tp.encheres.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.rowmapper.EnchereRowMapper;


@Repository
public class EnchèreDAOImpl implements EnchèreDAO {
	
	// ****************** CONSTANTES ***********************************
	
	private static final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :motDePasse, :credit, :administrateur)";
	private static final String SELECT_USER_BY_PRIX = "SELECT * FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String COUNT_ENCHERE  = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String FIND_ALL_BY_ID = "SELECT date_enchere FROM ENCHERES WHERE no_utilisateur = :id";

	// ****************** ATTRIBUT INSTANCES ***********************************
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	// ****************** METHODES ***********************************

	
	/**
	 * Méthode permettant de trouver toutes les enchères correspondant à l'id de l'utilisateur
	 * Return List Enchère
	 */
	@Override
	public List<Enchère> consulterEncheresById(int idUser) {
		 MapSqlParameterSource map = new MapSqlParameterSource();
		    map.addValue("id", idUser);
		    return jdbcTemplate.query(FIND_ALL_BY_ID, map, new EnchereRowMapper());
	}

	/**
	 * Méthode permettant de chercher la derniere enchere
	 * avec l'idArticle et le prixVente
	 * Return un Objet Enchère
	 */
	@Override
	public Enchère getUtilisateurParPrix(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return jdbcTemplate.queryForObject(SELECT_USER_BY_PRIX, map, new EnchereRowMapper());
	}
	/**
	 * Méthode permettant de savoir si une enchère a été faite sur un article
	 * Return int
	 */
	@Override
	public int getCountEnchere(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return jdbcTemplate.queryForObject(COUNT_ENCHERE, map, Integer.class);
	}



}
