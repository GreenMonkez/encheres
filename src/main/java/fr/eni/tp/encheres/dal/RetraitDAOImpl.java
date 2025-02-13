package fr.eni.tp.encheres.dal;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.dal.rowmapper.RetraitRowMapper;

@Repository
public class RetraitDAOImpl implements RetraitDAO {

	private static final String INSERT = "insert into retraits (no_article, rue, code_postal, ville) values (:noArticle, :rue, :codePostal, :ville)";
	private static final String SELECT_BY_ID = "SELECT * FROM RETRAITS WHERE no_article = :no_article";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public RetraitDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * Méthode insérant en BDD un nouvel lieu de retrait lié à un article
	 */
	@Override
	public void create(ArticleVendu article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noArticle", article.getNoArticle());
		map.addValue("rue", article.getLieuRetrait().getRue());
		map.addValue("codePostal", article.getLieuRetrait().getCode_postal());
		map.addValue("ville", article.getLieuRetrait().getVille());

		namedParameterJdbcTemplate.update(INSERT, map);

	}

	@Override
	public Retrait getRetraitById(int noArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", noArticle);
		return namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, map, new RetraitRowMapper());
	}

}
