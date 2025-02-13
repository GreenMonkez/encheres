package fr.eni.tp.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.Enchère;

import fr.eni.tp.encheres.dal.rowmapper.EnchèreRowMapper;

@Repository
public class EnchèreDAOImpl implements EnchèreDAO {
	private static final String COUNT_BY_ID_ARTICLE = "select count(*) from encheres where no_article = :idArticle";
	private static final String SELECT_ALL_BY_ID = "select no_utilisateur, no_article, date_enchere, montant_enchere from encheres where no_article = :idArticle";
	private static final String SELECT_USER_BY_PRIX = "SELECT * FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String COUNT_ENCHERE = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String FIND_ALL_BY_ID = "SELECT date_enchere FROM ENCHERES WHERE no_utilisateur = :id";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public EnchèreDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * Méthode retournant le nombre d'enchère en fonction de l'id de l'article
	 */
	@Override
	public int countByIdArticle(int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return namedParameterJdbcTemplate.queryForObject(COUNT_BY_ID_ARTICLE, map, Integer.class);
	}

	/**
	 * Méthode retournant la liste des enchères en fonction de l'id de l'article
	 */
	@Override
	public List<Enchère> getEncheres(int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return namedParameterJdbcTemplate.query(SELECT_ALL_BY_ID, map, new EnchèreRowMapper());
	}

	/**
	 * Méthode permettant de trouver toutes les enchères correspondant à l'id de
	 * l'utilisateur Return List Enchère
	 */
	@Override
	public List<Enchère> consulterEncheresById(int idUser) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id", idUser);
		return namedParameterJdbcTemplate.query(FIND_ALL_BY_ID, map, new EnchèreRowMapper());
	}

	@Override
	public Enchère getUtilisateurParPrix(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return namedParameterJdbcTemplate.queryForObject(SELECT_USER_BY_PRIX, map, new EnchèreRowMapper());
	}

	@Override
	public int getCountEnchere(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return namedParameterJdbcTemplate.queryForObject(COUNT_ENCHERE, map, Integer.class);
	}

}
