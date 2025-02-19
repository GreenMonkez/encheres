package fr.eni.tp.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.rowmapper.EnchèreRowMapper;

@Repository
public class EnchèreDAOImpl implements EnchèreDAO {

	// ****************** CONSTANTES ***********************************

	private static final String COUNT_BY_ID_ARTICLE = "select count(*) from encheres where no_article = :idArticle";
	private static final String SELECT_ALL_BY_ID = "select no_utilisateur, no_article, date_enchere, montant_enchere from encheres where no_article = :idArticle";
	private static final String SELECT_ALL_BY_ID_ORDER_DESC = "select no_utilisateur, no_article, date_enchere, montant_enchere from encheres where no_article = :idArticle order by montant_enchere desc";
	private static final String SELECT_USER_BY_PRIX = "SELECT * FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String COUNT_ENCHERE = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = :id_article AND montant_enchere = :prix_vente";
	private static final String FIND_ALL_BY_ID = "SELECT date_enchere FROM ENCHERES WHERE no_utilisateur = :id";
	private static final String INSERT = "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (:userId, :idArticle, GETDATE(), :montant)";
	private static final String UPDATE_PRIX_VENTE = "UPDATE ARTICLES_VENDUS SET prix_vente = :prixVente WHERE no_article = :idArticle";
	private static final String UPDATE_CREDIT = "UPDATE UTILISATEURS SET credit = :credit WHERE no_utilisateur = :id";

	// ****************** ATTRIBUT INSTANCES ***********************************

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	// ****************** METHODES ***********************************

	public EnchèreDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * Méthode retournant le nombre d'enchère en fonction de l'id de l'article
	 */
	@Override
	public int countByIdArticle(int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return namedParameterJdbcTemplate.queryForObject(COUNT_BY_ID_ARTICLE, map, Integer.class);
	}

	/**
	 * Méthode retournant la liste des enchères en fonction de l'id de l'article
	 */
	@Override
	public List<Enchère> getEncheres(int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return namedParameterJdbcTemplate.query(SELECT_ALL_BY_ID, map, new EnchèreRowMapper());
	}

	@Override
	public List<Enchère> getEncheresByIdArticleOrderDesc(int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return namedParameterJdbcTemplate.query(SELECT_ALL_BY_ID_ORDER_DESC, map, new EnchèreRowMapper());
	}

	/**
	 * Méthode permettant de trouver toutes les enchères correspondant à l'id de
	 * l'utilisateur Return List Enchère
	 */
	@Override
	public List<Enchère> consulterEncheresById(int idUser) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id", idUser);
		return namedParameterJdbcTemplate.query(FIND_ALL_BY_ID, map, new EnchèreRowMapper());
	}

	/**
	 * Méthode permettant de chercher la derniere enchere avec l'idArticle et le
	 * prixVente Return un Objet Enchère
	 */
	@Override
	public Enchère getUtilisateurParPrix(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return namedParameterJdbcTemplate.queryForObject(SELECT_USER_BY_PRIX, map, new EnchèreRowMapper());
	}

	/**
	 * Méthode permettant de savoir si une enchère a été faite sur un article Return
	 * int
	 */
	@Override
	public int getCountEnchere(int prixVente, int idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id_article", idArticle);
		map.addValue("prix_vente", prixVente);
		return namedParameterJdbcTemplate.queryForObject(COUNT_ENCHERE, map, Integer.class);
	}

	/**
	 * Méthode permettant de créer une nouvelle enchère dans la base de données
	 * 
	 * @Param int
	 * @param ArticleVendu
	 * @param Utilisateur
	 */
	@Override
	public void creerEnchere(int montant, ArticleVendu article, Utilisateur userSession) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("userId", userSession.getNoUtilisateur());
		map.addValue("idArticle", article.getNoArticle());
		map.addValue("montant", montant);

		namedParameterJdbcTemplate.update(INSERT, map);

		MapSqlParameterSource map2 = new MapSqlParameterSource();
		map2.addValue("prixVente", article.getPrixVente());
		map2.addValue("idArticle", article.getNoArticle());

		namedParameterJdbcTemplate.update(UPDATE_PRIX_VENTE, map2);
	}

	/**
	 * Méthode permettant d'update les crédit d'un utilisateur dans la base de donée
	 * 
	 * @param Utilisateur
	 */
	public void updateCredit(Utilisateur user) {

		MapSqlParameterSource map3 = new MapSqlParameterSource();
		map3.addValue("credit", user.getCredit());
		map3.addValue("id", user.getNoUtilisateur());

		namedParameterJdbcTemplate.update(UPDATE_CREDIT, map3);

	}

}
