package fr.eni.tp.encheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.Utilisateur;


@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

	private static final String SELECT_BY_ID = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit, administrateur from utilisateurs where no_utilisateur = :idUtilisateur";
	private static final String FIND_BY_PSEUDO = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit, administrateur from utilisateurs where pseudo = :pseudo";
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Utilisateur getUtilisateur(int noUtilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idUtilisateur", noUtilisateur);
		return namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters,
				new BeanPropertyRowMapper<>(Utilisateur.class));
	}



		@Override
		public Utilisateur getUtilisateur(String pseudo) {
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("email", pseudo);		
			return jdbcTemplate.queryForObject(FIND_BY_PSEUDO, mapSqlParameterSource, new UtilisateurRowMapper());
		}
}
	
	class UtilisateurRowMapper implements RowMapper<Utilisateur>{

		@Override
		public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
			Utilisateur m = new Utilisateur();
			m.setNoUtilisateur(rs.getInt("id"));
			m.setPseudo(rs.getString("email"));
			m.setNom(rs.getString("nom"));
			m.setPrenom(rs.getString("prenom"));
			m.setAdministrateur(rs.getBoolean("admin"));
			m.setEmail(rs.getString("email"));
			m.setTelephone(rs.getString("telephone"));
			m.setRue(rs.getString("rue"));
			m.setCodePostal(rs.getString("code_postal"));
			m.setVille(rs.getString("ville"));
			m.setCredit(rs.getInt("credit"));
			
			return m;
		}

}