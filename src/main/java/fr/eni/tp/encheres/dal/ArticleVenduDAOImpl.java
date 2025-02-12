package fr.eni.tp.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.dal.rowmapper.ArticleVenduRowMapper;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO {

	private static final String SELECT_ALL = "select no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie from articles_vendus";
	private static final String INSERT = "insert into articles_vendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie) values (:nom, :description, :dateDebut, :dateFin, :prixInitial, :idUtilisateur, :idCategorie)";
	private static final String SELECT_BY_STRING = "select no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie from articles_vendus where nom_article like :filtreSql";
	private static final String SELECT_BY_ID_CATEGORIE = "select no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie from articles_vendus where no_categorie = :idCategorie";
	private static final String SELECT_BY_STRING_AND_ID_CATEGORIE = "select no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie from articles_vendus where nom_article like :filtreSql and no_categorie = :idCategorie";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public ArticleVenduDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<ArticleVendu> getArticles() {
		return namedParameterJdbcTemplate.query(SELECT_ALL, new ArticleVenduRowMapper());

	}

	@Override
	public void create(ArticleVendu article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("nom", article.getNomArticle());
		map.addValue("description", article.getDescription());
		map.addValue("dateDebut", article.getDateDebutEncheres());
		map.addValue("dateFin", article.getDateFinEncheres());
		map.addValue("prixInitial", article.getMiseAPrix());
		map.addValue("idUtilisateur", article.getVendeur().getNoUtilisateur());
		map.addValue("idCategorie", article.getCategorieArticle().getNoCategorie());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(INSERT, map, keyHolder);

		if (keyHolder != null && keyHolder.getKey() != null) {

			article.setNoArticle(keyHolder.getKey().intValue());
		}
	}

	@Override
	public List<ArticleVendu> getArticlesFiltresByString(String filtreSql) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("filtreSql", filtreSql);
		return namedParameterJdbcTemplate.query(SELECT_BY_STRING, map, new ArticleVenduRowMapper());
	}

	@Override
	public List<ArticleVendu> getArticlesFiltresById(int idCategorie) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idCategorie", idCategorie);
		return namedParameterJdbcTemplate.query(SELECT_BY_ID_CATEGORIE, map, new ArticleVenduRowMapper());
	}

	@Override
	public List<ArticleVendu> getArticlesFiltresByStringAndId(String filtreSql, int idCategorie) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("filtreSql", filtreSql);
		map.addValue("idCategorie", idCategorie);
		return namedParameterJdbcTemplate.query(SELECT_BY_STRING_AND_ID_CATEGORIE, map, new ArticleVenduRowMapper());
	}

}
