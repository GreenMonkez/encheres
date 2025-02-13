package fr.eni.tp.encheres.dal.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.bo.Utilisateur;

public class EnchereRowMapper implements RowMapper<Enchère> {
	

	@Override
	public Enchère mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Enchère e = new Enchère();
		e.setMontant_enchere(rs.getInt("montant_enchere"));
		e.setDateEnchère(rs.getTimestamp("date_enchere").toLocalDateTime());
		
		Utilisateur u = new Utilisateur();
		u.setNoUtilisateur(rs.getInt("no_utilisateur"));
		e.setUtilisateur(u);
		
		
		ArticleVendu a = new ArticleVendu();
		a.setNoArticle(rs.getInt("no_article"));
		e.setArtcicleVendu(a);
	
		
	
		return e;
	}

}
