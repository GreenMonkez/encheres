package fr.eni.tp.encheres.bo;

import java.time.LocalDate;

public class Enchère {
	private LocalDate dateEnchère;
	private int montant_enchere;
	ArticleVendu artcicleVendu;
	Utilisateur utilisateur;

	public Enchère() {

	}

	public Enchère(LocalDate dateEnchère, int montant_enchere, ArticleVendu artcicleVendu, Utilisateur utilisateur) {
		this.dateEnchère = dateEnchère;
		this.montant_enchere = montant_enchere;
		this.artcicleVendu = artcicleVendu;
		this.utilisateur = utilisateur;
	}

	@Override
	public String toString() {
		return "Enchère [dateEnchère=" + dateEnchère + ", montant_enchere=" + montant_enchere + ", artcicleVendu="
				+ artcicleVendu + ", utilisateur=" + utilisateur + "]";
	}

	public LocalDate getDateEnchère() {
		return dateEnchère;
	}

	public void setDateEnchère(LocalDate dateEnchère) {
		this.dateEnchère = dateEnchère;
	}

	public int getMontant_enchere() {
		return montant_enchere;
	}

	public void setMontant_enchere(int montant_enchere) {
		this.montant_enchere = montant_enchere;
	}

	public ArticleVendu getArtcicleVendu() {
		return artcicleVendu;
	}

	public void setArtcicleVendu(ArticleVendu artcicleVendu) {
		this.artcicleVendu = artcicleVendu;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}
