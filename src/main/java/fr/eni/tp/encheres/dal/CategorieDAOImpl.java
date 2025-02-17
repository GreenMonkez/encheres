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

	// ****************** CONSTANTES ***********************************

	private static final String SELECT_BY_ID = "select no_categorie, libelle from categories where no_categorie = :idCategorie";
	private static final String SELECT_ALL = "select no_categorie, libelle from categories";
	private static final String INSERT = "insert into categories (libelle) values (:libelle)";
	private static final String UPDATE = "update categories set libelle = :libelle where no_categorie = :idCategorie";
	private static final String DELETE = "delete from categories where no_categorie = :idCategorie";

	// ****************** ATTRIBUT INSTANCES ***********************************

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	// ****************** CONSTRUCTEURS***********************************

	public CategorieDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	// ****************** METHODES ***********************************

	/**
	 * Méthode retournant une catégorie en fonction de son id
	 */
	@Override
	public Categorie getCategorie(int noCategorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idCategorie", noCategorie);
		return namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters,
				new BeanPropertyRowMapper<>(Categorie.class));
	}

	/**
	 * Méthode retournant la liste des catégories
	 */
	@Override
	public List<Categorie> getCategories() {
		return namedParameterJdbcTemplate.query(SELECT_ALL, new CategorieRowMapper());

	}

	@Override
	public void createNouvelleCategorie(Categorie categorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("libelle", categorie.getLibelle());
		namedParameterJdbcTemplate.update(INSERT, namedParameters);
	}

	@Override
	public void updateCategorie(Categorie categorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idCategorie", categorie.getNoCategorie());
		namedParameters.addValue("libelle", categorie.getLibelle());
		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
	}

	@Override
	public void deleteCategorie(int idCategorie) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idCategorie", idCategorie);
		namedParameterJdbcTemplate.update(DELETE, namedParameters);
	}

}
