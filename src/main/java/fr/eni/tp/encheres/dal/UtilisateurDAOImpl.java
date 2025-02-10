package fr.eni.tp.encheres.dal;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.Utilisateur;


@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{
	
	private final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :motDePasse, :credit, :administrateur)";
	//private final String FIND_BY_ID = "SELECT noUtilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM UTILISATEUR WHERE id = :id";
	private final String FIND_UNIQUE_PSEUDO = "SELECT count(pseudo) FROM UTILISATEURS WHERE pseudo like :pseudo";
	private final String FIND_UNIQUE_EMAIL = "SELECT count(email) FROM UTILISATEURS WHERE email like :email";

	
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
		
		if(keyHolder != null && keyHolder.getKey() != null) {
		
			user.setNoUtilisateur(keyHolder.getKey().intValue());
		}
	}

	@Override
	public int validerPseudo(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(FIND_UNIQUE_PSEUDO, map, Integer.class);
	}

	@Override
	public int validerEmail(String email) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("email", email);
		return jdbcTemplate.queryForObject(FIND_UNIQUE_EMAIL, map, Integer.class);
	}


	

}
