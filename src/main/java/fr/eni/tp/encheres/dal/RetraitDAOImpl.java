package fr.eni.tp.encheres.dal;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.dal.rowmapper.RetraitRowMapper;

@Repository
public class RetraitDAOImpl implements RetraitDAO {
	
	// ****************** CONSTANTES ***********************************

	private static final String INSERT = "insert into retraits (no_article, rue, code_postal, ville) values (:noArticle, :rue, :codePostal, :ville)";
	private static final String SELECT_BY_ID ="SELECT * FROM RETRAITS WHERE no_article = :no_article";
	
	// ****************** ATTRIBUT INSTANCES ***********************************
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	// ****************** CONSTRUCTEURS***********************************
	
	public RetraitDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	// ****************** METHODES ***********************************
	
	/**
	 * Méthode permettant d'inserer un retrait nouvellement créer dans la base de donnée
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

	/**
	 * Méthode permettant de chercher un retrait en base grâce à au numéro d'un article (noArticle)
	 * Return un retrait
	 */
	@Override
	public Retrait getretraitById(int noArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", noArticle);
		return namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, map, new RetraitRowMapper());
	}
	

}
