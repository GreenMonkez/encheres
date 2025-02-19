package fr.eni.tp.encheres.dal;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.Utilisateur;

import fr.eni.tp.encheres.dal.rowmapper.UtilisateurRowMapper;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

	// ****************** CONSTANTES ***********************************

	private static final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :motDePasse, :credit, :administrateur)";
	private static final String FIND_UNIQUE_PSEUDO = "SELECT count(pseudo) FROM UTILISATEURS WHERE pseudo like :pseudo";
	private static final String FIND_UNIQUE_EMAIL = "SELECT count(email) FROM UTILISATEURS WHERE email like :email";
	private static final String SELECT_BY_ID = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit, administrateur from utilisateurs where no_utilisateur = :idUtilisateur";
	private static final String FIND_BY_PSEUDO = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit, administrateur from utilisateurs where pseudo = :pseudo";
	private static final String FIND_UNIQUE_PASSWORD = "SELECT count(mot_de_passe) FROM UTILISATEURS WHERE mot_de_passe like :mot_de_passe";
	private static final String UPDATE = "UPDATE UTILISATEURS SET pseudo = :pseudo, nom = :nom, prenom = :prenom, email = :email, telephone = :telephone, rue = :rue, code_postal = :code_postal, ville = :ville, mot_de_passe = :motDePasse WHERE no_utilisateur = :no_utilisateur";
	private static final String UPDATE_CREDIT = "update utilisateurs set credit = :nouveauSoldeCredit where no_utilisateur = :noUtilisateur";
	private static final String FIND_BY_EMAIL = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit, administrateur from utilisateurs where email = :email";
    private static final String INSERT_TOKEN = "INSERT INTO PASSWORD_TOKENS (token, no_utilisateur) VALUES (:token, :no_utilisateur)";
    private static final String FIND_TOKEN = "SELECT COUNT(*) FROM PASSWORD_TOKENS WHERE token = :token ";
    private static final String FIND_USER_BY_TOKEN = "SELECT no_utilisateur FROM PASSWORD_TOKENS WHERE token = :token";
    private static final String FIND_DATE_BY_TOKEN = "SELECT date_expiration FROM PASSWORD_TOKENS WHERE token = :token";
	private static final String UPDATE_PASSWORD = "UPDATE UTILISATEURS SET  mot_de_passe = :motDePasse WHERE no_utilisateur = :no_utilisateur";

	
	// ****************** ATTRIBUT INSTANCES ***********************************

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	// ****************** METHODES ***********************************

	/**
	 * Méthode permettant d'inserer un utilisateur nouvellement créer dans la base
	 * de donnée
	 */
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

		jdbcTemplate.update(INSERT, map);

	}

	/**
	 * Méthode permettant de verifier si le pseudo donné par l'user correspond au
	 * pseudo en base de donnée Return un int
	 */
	@Override
	public int validerPseudo(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(FIND_UNIQUE_PSEUDO, map, Integer.class);
	}

	/**
	 * Méthode permettant de verifier si l'email donné par l'user correspond a
	 * l'email en base de donnée Return un int
	 */
	@Override
	public int validerEmail(String email) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("email", email);
		return jdbcTemplate.queryForObject(FIND_UNIQUE_EMAIL, map, Integer.class);
	}

	/**
	 * Méthode permettant de chercher un user en base grâce à son id Return un
	 * utilisateur
	 */
	@Override
	public Utilisateur getUtilisateur(int noUtilisateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idUtilisateur", noUtilisateur);
		return jdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters, new UtilisateurRowMapper());
	}

	/**
	 * Méthode permettant de chercher un user en base grâce à son pseudo Return un
	 * Utilisateur
	 */
	@Override
	public Utilisateur getUtilisateurByPseudo(String pseudo) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(FIND_BY_PSEUDO, mapSqlParameterSource, new UtilisateurRowMapper());
	}

	/**
	 * Méthode permettant de verifier si le mot de passe donné par l'user correspond
	 * au mot de passe en base de donnée Return un int
	 */
	@Override
	public int validerMdp(String mdp) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("mot_de_passe", mdp);
		return jdbcTemplate.queryForObject(FIND_UNIQUE_PASSWORD, map, Integer.class);
	}

	/**
	 * Méthode permettant de modifier un utilisateur en base de donnée
	 */
	@Override
	public void modifierUtilisateur(Utilisateur user) {
		try {
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
			map.addValue("no_utilisateur", user.getNoUtilisateur());

			int rowsAffected = jdbcTemplate.update(UPDATE, map);
			if (rowsAffected == 0) {
				throw new RuntimeException("Aucune mise à jour effectuée, utilisateur introuvable.");
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage(), e);
		}

	}

	/**
	 * Méthode permettant de modifier le solde de crédit d'un utilisateur en
	 * fonction de son id et du montant
	 */
	@Override
	public void updateCredit(int noUtilisateur, int nouveauSoldeCredit) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noUtilisateur", noUtilisateur);
		map.addValue("nouveauSoldeCredit", nouveauSoldeCredit);
		jdbcTemplate.update(UPDATE_CREDIT, map);
	}

	@Override
	public Utilisateur getUtilisateurByEmail(String email) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("email", email);
		return jdbcTemplate.queryForObject(FIND_BY_EMAIL, mapSqlParameterSource, new UtilisateurRowMapper());
	}

	@Override
	public void sauvegarderPasswordResetToken(int idUser, String token) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("token", token);
		map.addValue("no_utilisateur", idUser);
		jdbcTemplate.update(INSERT_TOKEN, map);
		
	}

	@Override
	public boolean validerPasswordResetToken(String token) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("token", token);
		int count = jdbcTemplate.queryForObject(FIND_TOKEN, map, Integer.class);
		return count == 0;
	}

	@Override
	public Utilisateur readByToken(String token) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("token", token);
		return jdbcTemplate.queryForObject(FIND_USER_BY_TOKEN, mapSqlParameterSource, new UtilisateurRowMapper());

	}
//FIND_DATE_BY_TOKEN
	
	@Override
	public Timestamp findDateToken(String token) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("token", token);
		return jdbcTemplate.queryForObject(FIND_DATE_BY_TOKEN, mapSqlParameterSource, Timestamp.class);

	}

	@Override
	public void resetPassword(int idUser, String password) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noUtilisateur", idUser);
		map.addValue("motDePasse", password);
		jdbcTemplate.update(UPDATE_PASSWORD, map);
		
	}
}
