package fr.eni.tp.encheres.bo;

public class Utilisateur {
	
	public String pseudo;
	public String nom;
	public String prenom;
	public String email;
	public String telephone;
	public String rue;
	public long codePostal;
	public String ville;
	public String motDePasse;
	public long credit;
	public boolean administrateur;
	public boolean noUtilisateur;
	

	public Utilisateur() {
		
	}
	
	
	public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue,
			long codePostal, String ville, String motDePasse) {
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Utilisateur pseudo : ").append(pseudo).append(", nom : ").append(nom).append(", prenom : ")
				.append(prenom).append(", email : ").append(email).append(", telephone : ").append(telephone)
				.append(", rue : ").append(rue).append(", codePostal : ").append(codePostal).append(", ville : ")
				.append(ville).append(", credit : ").append(credit);
		return builder.toString();
	}
	//******************	GETTERS & SETTERS	***********************************
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public long getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(long codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public long getCredit() {
		return credit;
	}
	public void setCredit(long credit) {
		this.credit = credit;
	}
	public boolean isAdministrateur() {
		return administrateur;
	}
	public void setAdministrateur(boolean administrateur) {
		this.administrateur = administrateur;
	}
	public boolean isNoUtilisateur() {
		return noUtilisateur;
	}
	public void setNoUtilisateur(boolean noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}
	
	
	

}
