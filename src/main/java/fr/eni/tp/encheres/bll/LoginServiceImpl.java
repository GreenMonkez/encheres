package fr.eni.tp.encheres.bll;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import fr.eni.tp.encheres.bo.ArticleVendu;
import fr.eni.tp.encheres.bo.Enchère;
import fr.eni.tp.encheres.bo.Utilisateur;
import fr.eni.tp.encheres.dal.ArticleVenduDAO;
import fr.eni.tp.encheres.dal.EnchèreDAO;
import fr.eni.tp.encheres.dal.UtilisateurDAO;
import fr.eni.tp.encheres.exception.BusinessException;

@Service
public class LoginServiceImpl implements LoginService {

	private UtilisateurDAO utilisateurDAO;
	private ArticleVenduDAO articleDAO;
	private EnchèreDAO enchereDAO;

	public LoginServiceImpl(UtilisateurDAO utilisateurDAO, ArticleVenduDAO articleDAO, EnchèreDAO enchereDAO) {

		this.utilisateurDAO = utilisateurDAO;
		this.articleDAO = articleDAO;
		this.enchereDAO = enchereDAO;

	}
	
	
	
	/**
	 *Méthode permettant de créer un Utilisateur 
	 *Fais appel aux méthodes de validation : 
	 *Verifier pseudo Utilisateur unique
	 *Vérifier email Utilisateur unique
	 *Vérifier si mots de passe confirmation == mot de passe
	 *Encoder le mot de passe
	 *Appelle de la DAO pour enregistrer un base de donnée
	 */
	@Override
	public void creerUtilisateur(Utilisateur user, String mdpConfirm) throws BusinessException {
		BusinessException be = new BusinessException();

		boolean valide = validerUtilisateurPseudo(user.getPseudo(), be);
		valide &= validerUtilisateurEmail(user.getEmail(), be);
		valide &= validerConfirmMdp(mdpConfirm, user.getMotDePasse(), be);

		try {
			if (valide) {
				String mdpEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder()
						.encode(user.getMotDePasse());
				user.setMotDePasse(mdpEncode);
				utilisateurDAO.creerUtilisateur(user);
			} else {
				throw be;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			be.addErreur("erreur.utilisateur.creation");
			throw be;
		}

	}
	
	
	
	/**
	 *Méthode permettant de modifier les infos d'un Utilisateur
	 *Fais appel aux méthodes de validation : 
	 *Vérifier si mots de passe confirmation == mot de passe
	 *Vérifier si mot de passe actuel est bien celui entrer par l'Utilisateur
	 *Appel de la DAO pour enregistrer les modifications en base de données
	 *Encoder le mot de passe
	 */
	@Override
	public void modifierUtilisateur(Utilisateur user, String mdpConfirm, String newMdp) throws BusinessException {
		BusinessException be = new BusinessException();
		String mdpActuel = user.getMotDePasse();

		boolean valide = validerConfirmMdp(mdpConfirm, newMdp, be);
		valide &= validerMdpActuel(mdpActuel, be);

		try {
			if (valide) {
				String mdpEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(newMdp);
				user.setMotDePasse(mdpEncode);
				utilisateurDAO.modifierUtilisateur(user);

			} else {
				throw be;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			be.addErreur("erreur.utilisateur.modification");
			throw be;
		}

	}
	
	
	
	/**
	 *Méthode permettant de supprimer un Utilisateur
	 *Fais appel aux méthodes de validation : 
	 *Vérifie si l' Utilisateur a des articles en cours de vente
	 *Vérifie si l' Utilisateur a des encheres sur des articles en cours de vente
	 *Implémentation d'info vide puis envoie vers DAO pour save en base de donnée
	 */
	@Override
	public void supprimerUtilisateur(Utilisateur userSupp) throws BusinessException {

		BusinessException be = new BusinessException();
		List<ArticleVendu> listArticles = articleDAO.consulterArticlesById(userSupp.getNoUtilisateur());
		List<Enchère> listEncheres = enchereDAO.consulterEncheresById(userSupp.getNoUtilisateur());
		String newMdp = "G7#xL9vP!mQ2zW@dT5yF";
		boolean valide = validerVenteEnCours(userSupp.getNoUtilisateur(), be, listArticles);
		valide &= validerEnchereEnCours(userSupp.getNoUtilisateur(), be, listEncheres);

		try {
			if (valide) {
				if (listArticles != null) {
					for (ArticleVendu article : listArticles) {
						article.setDescription("Inconnu");
						articleDAO.modifierArticle(article);
					}
				}
				userSupp.setPseudo("UserSupp");
				userSupp.setNom("Supprimer");
				userSupp.setPrenom("Utilisateur");
				userSupp.setEmail("userSupp@supp.com");
				userSupp.setTelephone("00.00.00.00.00");
				userSupp.setRue("adresse inconnu");
				userSupp.setCodePostal("00000");
				userSupp.setVille("Inconnu");
				userSupp.setCredit(0);
				String mdpEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(newMdp);
				userSupp.setMotDePasse(mdpEncode);
				utilisateurDAO.modifierUtilisateur(userSupp);

			} else {
				throw be;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			be.addErreur("erreur.supprimer.profil.vente");
			throw be;
		}
	}

	// ************** VALIDATION ***********************************************
	
	
	/**
	 * Méthode permettant de verifier si le pseudo est unique
	 * Appel DAO pour récupérer le nombre de pseudo associer au pseudo entrer par l'Utilisateur
	 * @param pseudo
	 * @param be
	 * @return boolean
	 */
	private boolean validerUtilisateurPseudo(String pseudo, BusinessException be) {

		boolean valide = true;
		int nbPseudo = utilisateurDAO.validerPseudo(pseudo);
		if (nbPseudo == 1) {
			valide = false;
			be.addErreur("erreur.utilisateur.pseudo.exist");
		}

		return valide;

	}
	
	
	/**
	 * Méthode permettant de verifier si l'email est unique
	 * Appel DAO pour récupérer le nombre d'email associer au email entrer par l'Utilisateur
	 * @param email
	 * @param be
	 * @return boolean
	 */
	private boolean validerUtilisateurEmail(String email, BusinessException be) {

		boolean valide = true;
		int nbEmail = utilisateurDAO.validerEmail(email);
		if (nbEmail == 1) {
			valide = false;
			be.addErreur("erreur.utilisateur.email.exist");
		}

		return valide;

	}
	
	/**
	 * Trouver un Utilisateur grâce à son id
	 * @param int id
	 * @return Utilisateur
	 */
	@Override
	public Utilisateur consulterUtilisateur(int id) {
		Utilisateur user = utilisateurDAO.getUtilisateur(id);
		return user;
	}
	
	/**
	 * Méthode permettant de comparer le mot de passe et la confirmation  mots de passe entrer par l'Utilisateur
	 * @param mdp
	 * @param mdpConfirm
	 * @param be
	 * @return boolean
	 */
	private boolean validerConfirmMdp(String mdp, String mdpConfirm, BusinessException be) {

		boolean valide = true;
		if (!mdp.equals(mdpConfirm)) {
			valide = false;
			be.addErreur("erreur.password.confirm");
		}
		return valide;
	}
	
	/**
	 * Méthode permettant de vérifier si le mots de passe entrer par l'Utilisateur est bien le mots de passe dans la base de donnée
	 * Encodage du mot de passe avant envoie en base de donnée
	 * Appel de la DAO 
	 * @param mdp
	 * @param be
	 * @return boolean
	 */
	private boolean validerMdpActuel(String mdp, BusinessException be) {
		boolean valide = true;
		String mdpEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(mdp);
		int nbMdp = utilisateurDAO.validerMdp(mdpEncode);
		if (nbMdp == 1) {
			valide = false;
			be.addErreur("erreur.password.actuel");
		}

		return valide;

	}
	/**
	 * Trouver un Utilisateur grâce à son pseudo
	 * @return Utilisateur
	 */
	@Override
	public Utilisateur charger(String pseudo) {
		return this.utilisateurDAO.getUtilisateurByPseudo(pseudo);

	}
	
	
	/**
	 * Méthode permettant de savoir si l'article est en cours de ventes
	 * @param idUser
	 * @param be
	 * @param listArticles
	 * @return boolean
	 */
	private boolean validerVenteEnCours(int idUser, BusinessException be, List<ArticleVendu> listArticles) {
		boolean valide = true;

		if (listArticles != null) {

			for (ArticleVendu articleVendu : listArticles) {

				if (articleVendu.getDateDebutEncheres().isAfter(LocalDateTime.now())) {
					valide = false;
					be.addErreur("erreur.supprimer.profil.vente.avenir");
				}
				if (articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now())) {
					valide = false;
					be.addErreur("erreur.supprimer.profil.vente.encours");
				}

			}
		}
		return valide;
	}
	
	
	/**
	 * Méthode permettant de vérifier si un Utilisateur a fait des enchères sur des article en vente
	 * @param idUser
	 * @param be
	 * @param listEncheres
	 * @return boolean
	 */
	private boolean validerEnchereEnCours(int idUser, BusinessException be, List<Enchère> listEncheres) {
		boolean valide = true;

		if (listEncheres != null) {

			for (Enchère enchère : listEncheres) {
			
				if (enchère.getDateEnchère().isBefore(LocalDateTime.now())) {
					valide = false;
					be.addErreur("erreur.supprimer.profil.enchere.encours");
				}

			}
		}

		return valide;

	}

}
