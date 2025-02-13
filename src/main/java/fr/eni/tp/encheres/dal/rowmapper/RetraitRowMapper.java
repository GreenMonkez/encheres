package fr.eni.tp.encheres.dal.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.bo.Utilisateur;

public class RetraitRowMapper implements RowMapper<Retrait> {
	

	@Override
	public Retrait mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Retrait r =new Retrait();
		r.setRue(rs.getString("rue"));
		r.setVille(rs.getString("ville"));
		r.setCode_postal(rs.getString("code_postal"));
		
		
		ArticleVendu a = new ArticleVendu();
		a.setNoArticle(rs.getInt("no_article"));
		r.setActicleVendu(a);
	
		
	
		return r;
	}

}
