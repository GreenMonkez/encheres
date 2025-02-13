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
}
