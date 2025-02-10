package fr.eni.tp.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.dal.rowmapper.ArticleVenduRowMapper;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO {

	private static final String SELECT_ALL = "select no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie from articles_vendus ";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public ArticleVenduDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<ArticleVendu> getArticles() {
		return namedParameterJdbcTemplate.query(SELECT_ALL, new ArticleVenduRowMapper());

	}

}
