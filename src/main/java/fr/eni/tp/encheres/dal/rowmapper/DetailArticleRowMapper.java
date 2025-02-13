package fr.eni.tp.encheres.dal.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Categorie;
import fr.eni.tp.encheres.bo.Retrait;
import fr.eni.tp.encheres.bo.Utilisateur;

public class DetailArticleRowMapper implements RowMapper<ArticleVendu> {
	

	@Override
	public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ArticleVendu a = new ArticleVendu();
		Utilisateur u =new Utilisateur();
		Utilisateur uu =new Utilisateur();
		Retrait r = new Retrait();
		Categorie c = new Categorie();
				
		uu.setPseudo(rs.getString("Acheteur"));
		a.setPrixVente(rs.getInt("prix_vente"));
		a.setNoArticle(rs.getInt("no_article"));
		a.setNomArticle(rs.getString("nom_article"));
		c.setLibelle(rs.getString("libelle"));
		a.setDescription(rs.getString("description"));
		a.setDateDebutEncheres(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
		a.setDateFinEncheres(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
		a.setMiseAPrix(rs.getInt("prix_initial"));
		u.setPseudo(rs.getString("Vendeur"));
		r.setRue(rs.getString("rue"));
		r.setVille(rs.getString("ville"));
		r.setCode_postal(rs.getString("code_postal"));
	
		a.setCategorieArticle(c);
		a.setVendeur(u);
		a.setAcheteur(uu);
		a.setLieuRetrait(r);
		
	
		return a;
	}

}
