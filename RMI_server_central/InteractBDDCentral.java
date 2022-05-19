import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.remote.rmi.RMIServer;
import java.rmi.server.UnicastRemoteObject;

// Implémenter l'interface de l'objet distante
public class InteractBDDCentral extends UnicastRemoteObject implements InteractInterfaceCentral {

    InteractBDDCentral()throws RemoteException{  
         super();  
    } 

    Integer prix; //prix d'un article
    Integer somme; //somme des articles d'une commande
    Integer chiffreAffaire; //total chiffre d'affaire en fonction d'une date donnée
    
    public Statement connectBDD() throws Exception
    {
        Connection conn = null;
        Statement stmt = null;

        //Enregistrer le pilote JDBC
        Class.forName("com.mysql.cj.jdbc.Driver");

        //Ouvrez une connexion
        System.out.println("Connexion à la base de données sélectionnée...");
        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/HeptathlonCentral", "rmi", "rmi");
        System.out.println("Base de données connectée avec succès...");

        //Exécuter la requête
        System.out.println("Créer l'objet Statement...");

        stmt = conn.createStatement();
        return stmt;
    }

    // Implémenter la méthode de l'interface
    public List<Article> updateArticlesPrix() throws Exception
    {
        List<Article> liste = new ArrayList<Article>();

        Statement stmt = connectBDD();

        String sql = "SELECT * FROM Article";
        ResultSet res = stmt.executeQuery(sql);

        //Extraire des données de ResultSet
        while(res.next()) {
            // Récupérer par nom de colonne
            int id = res.getInt("id_article");
            String reference = res.getString("reference");
            int prix = res.getInt("prix_article");

            // Définir les valeurs
            Article art = new Article();
            art.setID(id);
            art.setReference(reference);
            art.setPrix(prix);
            liste.add(art);
        }
        res.close();
        return liste;
    }


    public String sendFacture(byte[] contenu, String nom) throws RemoteException {
        try{
            Files.write(Paths.get(nom),contenu);
        } catch (IOException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
            return "Non reçu";
        }
        return "Facture bien reçu";
    }

}