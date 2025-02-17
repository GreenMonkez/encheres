package fr.eni.tp.encheres.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.rowmapper.EnchereRowMapper;
import fr.eni.tp.encheres.dal.rowmapper.EnchèreRowMapper;



@Repository
public class EnchèreDAOImpl implements EnchèreDAO {
	private static final String SELECT_USER_BY_PRIX = "SELECT * FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String COUNT_ENCHERE  = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String FIND_ALL_BY_ID = "SELECT date_enchere FROM ENCHERES WHERE no_utilisateur = :id";
	private static final String INSERT = "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (:userId, :idArticle, GETDATE(), :montant)";
	private static final String UPDATE_PRIX_VENTE = "UPDATE ARTICLES_VENDUS SET prix_vente = :prixVente WHERE no_article = :idArticle";
	private static final String UPDATE_CREDIT = "UPDATE UTILISATEURS SET credit = :credit WHERE no_utilisateur = :id";

	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;


	
	/**
	 * Méthode permettant de trouver toutes les enchères correspondant à l'id de l'utilisateur
	 * Return List Enchère
	 */
	@Override
	public List<Enchère> consulterEncheresById(int idUser) {
		 MapSqlParameterSource map = new MapSqlParameterSource();
		    map.addValue("id", idUser);
		    return jdbcTemplate.query(FIND_ALL_BY_ID, map, new EnchèreRowMapper());
	}


	@Override
	public Enchère getUtilisateurParPrix(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return jdbcTemplate.queryForObject(SELECT_USER_BY_PRIX, map, new EnchereRowMapper());
	}

	@Override
	public int getCountEnchere(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return jdbcTemplate.queryForObject(COUNT_ENCHERE, map, Integer.class);
	}

	
	/**
	 * Méthode permettant de créer une nouvelle enchère dans la base de données
	 * @Param int 
	 * @param ArticleVendu
	 * @param Utilisateur
	 */
	@Override
	public void creerEnchere(int montant, ArticleVendu article, Utilisateur userSession) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("userId", userSession.getNoUtilisateur());
		map.addValue("idArticle", article.getNoArticle());
		map.addValue("montant", montant);
		
		jdbcTemplate.update(INSERT, map);
		
		MapSqlParameterSource map2 = new MapSqlParameterSource();
		map2.addValue("prixVente", article.getPrixVente());
		map2.addValue("idArticle", article.getNoArticle());
		
		jdbcTemplate.update(UPDATE_PRIX_VENTE, map2);
	}

	/**
	 * Méthode permettant d'update les crédit d'un utilisateur dans la base de donée
	 * @param Utilisateur
	 */
	public void updateCredit(Utilisateur user) {
		
		MapSqlParameterSource map3 = new MapSqlParameterSource();
		map3.addValue("credit", user.getCredit());
		map3.addValue("id", user.getNoUtilisateur());
		
		
		jdbcTemplate.update(UPDATE_CREDIT, map3);
		
	}



	

	
}