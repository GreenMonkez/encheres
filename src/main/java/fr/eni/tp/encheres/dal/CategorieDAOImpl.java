package fr.eni.tp.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.dal.rowmapper.CategorieRowMapper;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

	private static final String SELECT_BY_ID = "select no_categorie, libelle from categories where no_categorie = :idCategorie";
	private static final String SELECT_ALL = "select no_categorie, libelle from categories";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public CategorieDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Categorie getCategorie(int noCategorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idCategorie", noCategorie);
		return namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters,
				new BeanPropertyRowMapper<>(Categorie.class));
	}

	@Override
	public List<Categorie> getCategories() {
		return namedParameterJdbcTemplate.query(SELECT_ALL, new CategorieRowMapper());

	}

}
