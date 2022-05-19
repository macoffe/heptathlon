import java.rmi.*;
import java.util.*;
import java.sql.*;

// Créer l'interface de l'objet distante
public interface InteractInterface extends Remote {

    public Statement connectBDD() throws Exception;
    public String checkLogin(String form_login, String form_mdp) throws Exception;
    public List<Article> researchByReference(String reference) throws Exception;
    public List<Article> researchByFamille(String famille) throws Exception;
    public void addArticleToCommande(String reference, String numeroCommande, Integer nbAleaClient) throws Exception;
    public void createCommande(String numeroCommande) throws Exception;
    public List<Commande> getAllCommande() throws Exception;
    public List<Commande> rechercheNom(String nom) throws Exception;
    public String getNom(String numeroCommande) throws Exception;
    public String getPrenom(String numeroCommande) throws Exception;
    public List<Commande> researchByNumeroCommande(String reference) throws Exception;
    public void UpToDatePaiementCommande(String typePaiement, String numCommande) throws Exception;
    public void ajouteFacture(String numCommande) throws Exception;
    public List<String> getFacture(String numCommande, String nom, String prenom) throws Exception;
    public void updateFacture(String numCommande, String nom, String prenom, String paiement) throws Exception;
    public int getValeurChiffreAffaire(String date) throws Exception;
    public void addArticle(String quantite, String reference) throws Exception;
    
}
