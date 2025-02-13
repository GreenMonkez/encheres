package fr.eni.tp.encheres.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.rowmapper.EnchereRowMapper;

@Repository
public class EnchèreDAOImpl implements EnchèreDAO {
	private static final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :motDePasse, :credit, :administrateur)";
	private static final String SELECT_USER_BY_PRIX = "SELECT * FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String COUNT_ENCHERE  = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void creerUtilisateur(Utilisateur user) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", user.getPseudo());
		map.addValue("nom", user.getNom());
		map.addValue("prenom", user.getPrenom());
		map.addValue("email", user.getEmail());
		map.addValue("telephone", user.getTelephone());
		map.addValue("rue", user.getRue());
		map.addValue("code_postal", user.getCodePostal());
		map.addValue("ville", user.getVille());
		map.addValue("motDePasse", user.getMotDePasse());
		map.addValue("credit", user.getCredit());
		map.addValue("administrateur", user.isAdministrateur());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(INSERT, map, keyHolder);

		if (keyHolder != null && keyHolder.getKey() != null) {

			user.setNoUtilisateur(keyHolder.getKey().intValue());
		}
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


}
