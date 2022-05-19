import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// Implémenter l'interface de l'objet distante
public class InteractBDD extends UnicastRemoteObject implements InteractInterface {

    InteractBDD()throws RemoteException{  
         super();  
    }

    Integer prix; //prix d'un article
    Integer somme; //somme des articles d'une commande
    Integer chiffreAffaire; //total chiffre d'affaire en focntiond d'une date donnée
    
    public Statement connectBDD() throws Exception
    {
        Connection conn = null;
        Statement stmt = null;

        //Enregistrer le pilote JDBC
        Class.forName("com.mysql.cj.jdbc.Driver");

        //Ouvrez une connexion
        System.out.println("Connexion à la base de données sélectionnée...");
        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/HeptathlonMagasin", "rmi", "rmi");
        System.out.println("Base de données connectée avec succès...");

        //Exécuter la requête
        System.out.println("Créer l'objet Statement...");

        stmt = conn.createStatement();
        return stmt;
    }

    public String checkLogin(String form_login, String form_mdp) throws Exception
    {
        List<Login> liste = new ArrayList<Login>();

        Statement stmt = connectBDD();
      
        String sql = "SELECT * FROM Login";
        ResultSet res = stmt.executeQuery(sql);

        //Extraire des données de ResultSet
        while(res.next()) {
            // Récupérer par nom de colonne
            String login = res.getString("login");
            String mdp = res.getString("mdp");
            String type = res.getString("type");

            // Définir les valeurs
            Login lgn = new Login();
            lgn.setLogin(login);
            lgn.setMdp(mdp);
            lgn.setType(type);
            liste.add(lgn);
        }
        res.close();

        for (Login l : liste) {
            if (l.getLogin().equals(form_login) && l.getMdp().equals(form_mdp)) {
                return l.getType();
            }
        }
        return "erreur";
    }

    public List<Article> researchByReference(String reference) throws Exception{
        List<Article> liste = new ArrayList<Article>();

        Statement stmt = connectBDD();

        String sql = "SELECT * FROM Article WHERE reference = \""+ reference +"\"";
        ResultSet res = stmt.executeQuery(sql);

        //Extraire des données de ResultSet
        while(res.next()) {
            // Récupérer par nom de colonne
            int idArticle = res.getInt("id_article");
            String referenceArticle = res.getString("reference");
            String nomArticle = res.getString("nom_article");
            int prixArticle = res.getInt("prix_article");
            int stockArticle = res.getInt("stock");

            // Définir les valeurs
            Article art = new Article();
            art.setID(idArticle);
            art.setReference(referenceArticle);
            art.setNom(nomArticle);
            art.setPrix(prixArticle);
            art.setStock(stockArticle);
            liste.add(art);
        }
        res.close();

        return liste;
    }

    public List<Article> researchByFamille(String famille) throws Exception{
        List<Article> liste = new ArrayList<Article>();

        Statement stmt = connectBDD();

        String sql = "SELECT * FROM Article WHERE stock != 0 AND famille = (SELECT id_famille FROM Famille WHERE nom = \""+ famille +"\")";
        ResultSet res = stmt.executeQuery(sql);

        //Extraire des données de ResultSet
        while(res.next()) {
            // Récupérer par nom de colonne
            int idArticle = res.getInt("id_article");
            String referenceArticle = res.getString("reference");
            String nomArticle = res.getString("nom_article");
            int prixArticle = res.getInt("prix_article");
            int stockArticle = res.getInt("stock");

            // Définir les valeurs
            Article art = new Article();
            art.setID(idArticle);
            art.setReference(referenceArticle);
            art.setNom(nomArticle);
            art.setPrix(prixArticle);
            art.setStock(stockArticle);
            liste.add(art);
        }
        res.close();

        return liste;
    }

    public void addArticleToCommande(String reference, String numeroCommande, Integer nbAleaClient) throws Exception{
        Statement stmt = connectBDD();

        //décrémente le stock de l'article en base
        String sqlUpdate = "UPDATE Article SET stock = stock -1 WHERE stock > 0 AND reference = \""+ reference +"\"";
        stmt.executeUpdate(sqlUpdate);

        //on recupere le prix de l'article en fonction de la reference donnée en paramètre
        String sqlSelect = "SELECT prix_article FROM Article WHERE reference = \""+ reference +"\"";
        ResultSet res = stmt.executeQuery(sqlSelect);
        while(res.next()) {
            prix = res.getInt("prix_article");
        }
        res.close();

        //creation d'un nom et prenom aléatoire
        String prenom = "prenom"+nbAleaClient;
        String nom = "nom"+nbAleaClient;

        //ajoute dans la tables commandes l'article
        String sqlInsert = "INSERT INTO Commandes (numero_commandes, prenom, nom, reference_article, prix) VALUES (\""+numeroCommande+"\",\""+prenom+"\",\""+nom+"\",\""+reference+"\",\""+prix+"\")";
        stmt.execute(sqlInsert);

        //res.close();
    }

    public void createCommande(String numeroCommande) throws Exception {
        Statement stmt = connectBDD();

        //récupère la somme de tous les articles correspondant à la commande
        String sqlSelect = "SELECT SUM(prix) FROM Commandes WHERE numero_commandes = \""+ numeroCommande +"\"";
        ResultSet res = stmt.executeQuery(sqlSelect);
        while(res.next()) {
            somme = res.getInt("SUM(prix)");
        }
        res.close();

        //date au moment de l'ajout en base
        Date date = new Date(); 
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Date = DATE_FORMAT.format(date);
        //System.out.println(Date);

        //Cré une commande dans la base regroupant le prix de tous les articles, le statut du paiment etc...
        String sqlInsert = "INSERT INTO Commande (numero_commande, total, date_commande) VALUES (\""+numeroCommande+"\",\""+somme+"\",\""+Date+"\")";
        stmt.execute(sqlInsert);

        ajouteFacture(numeroCommande);

    }

    public void ajouteFacture(String numCommande) throws Exception{

        Statement stmt = connectBDD();

        BufferedWriter writer = new BufferedWriter(new FileWriter("./facture", true));

        String sql_client = "SELECT prenom, nom FROM Commandes WHERE numero_commandes = \""+ numCommande +"\" LIMIT 1";
        ResultSet res_client = stmt.executeQuery(sql_client);
        while(res_client.next()) {
            writer.append(res_client.getString("nom")+" "+res_client.getString("prenom")+" - "+numCommande);
            writer.append('\n');
        }

        String sql_details = "SELECT nom_article, prix FROM Article INNER JOIN Commandes ON Article.reference = Commandes.reference_article AND Commandes.numero_commandes = \""+ numCommande +"\"";
        ResultSet res_details = stmt.executeQuery(sql_details);
        while(res_details.next()) {
            writer.append("          "+res_details.getString("nom_article")+" - "+res_details.getString("prix")+" €");
            writer.append('\n');
        }

        String sql_bilan = "SELECT date_commande, payement, total FROM Commande WHERE numero_commande = \""+ numCommande +"\"";
        ResultSet res_bilan = stmt.executeQuery(sql_bilan);
        while(res_bilan.next()) {
            writer.append("Total: "+res_bilan.getString("total")+" €");
            writer.append('\n');
            writer.append("Date de facturation: "+res_bilan.getString("date_commande"));
            writer.append('\n');
            writer.append("Status du payement: "+res_bilan.getString("payement"));
            writer.append("\n~\n");
        }

        writer.close();
    }

    public List<String> getFacture(String numCommande, String nom, String prenom) throws Exception{

        BufferedReader reader = new BufferedReader(new FileReader("./facture"));
        String line;
        List<String> facture = new ArrayList<String>();
        
        while (!(line = reader.readLine()).equals(nom+" "+prenom+" - "+numCommande)){}
        
        facture.add(line);
        while (!(line = reader.readLine()).equals("~")) {
            facture.add(line);
        }

        reader.close();
        return facture;
    }

    public void updateFacture(String numCommande, String nom, String prenom, String paiement) throws Exception{

        BufferedReader reader = new BufferedReader(new FileReader("./facture"));
        String line;
        List<String> facture = new ArrayList<String>();
        
        while (!(line = reader.readLine()).equals(nom+" "+prenom+" - "+numCommande)){
            facture.add(line);
        }
        facture.add(line); 
        while (!(line = reader.readLine()).equals("~")) {
            if (line.contains("Status du payement: non payé")){ line = line.replace("non payé", paiement); }
            facture.add(line);
        } 
        facture.add(line);
        while ((line = reader.readLine()) != null) {
            facture.add(line);
        }
        reader.close();
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("./facture"));

        for(String updatedline : facture)
            writer.write(updatedline+"\n");
        writer.flush();
        writer.close();


    }

    public List<Commande> getAllCommande() throws Exception
    {
        List<Commande> liste = new ArrayList<Commande>();

        Statement stmt = connectBDD();

        String sql = "SELECT * FROM Commande";
        ResultSet res = stmt.executeQuery(sql);

        //Extraire des données de ResultSet
        while(res.next()) {
            // Récupérer par nom de colonne
            int id_commande = res.getInt("id_commande");
            String numero_commande = res.getString("numero_commande");
            int total = res.getInt("total");
            String date = res.getString("date_commande");
            String payement = res.getString("payement");

            // Définir les valeurs
            Commande com = new Commande();
            com.setIdCommande(id_commande);
            com.setNumeroCommande(numero_commande);
            com.setTotal(total);
            com.setDateCommande(date);
            com.setPayement(payement);
            liste.add(com);
        }
        res.close();
        return liste;
    }

    public List<Commande> rechercheNom(String nom) throws Exception{

        List<Commande> liste = new ArrayList<Commande>();

        Statement stmt = connectBDD();

        String sql = "SELECT * FROM Commande WHERE numero_commande IN (SELECT numero_commandes FROM Commandes WHERE nom = \""+ nom +"\")";
        ResultSet res = stmt.executeQuery(sql);

        while(res.next()) {
            // Récupérer par nom de colonne
            int id_commande = res.getInt("id_commande");
            String numero_commande = res.getString("numero_commande");
            int total = res.getInt("total");
            String date = res.getString("date_commande");
            String payement = res.getString("payement");

            // Définir les valeurs
            Commande com = new Commande();
            com.setIdCommande(id_commande);
            com.setNumeroCommande(numero_commande);
            com.setTotal(total);
            com.setDateCommande(date);
            com.setPayement(payement);
            liste.add(com);
        }
        res.close();
        return liste;
    }

    public String getNom(String numeroCommande) throws Exception{
        String nom = "";

        Statement stmt = connectBDD();

        String sqlSelect = "SELECT nom FROM Commandes WHERE numero_commandes = \""+ numeroCommande +"\"";
        ResultSet res = stmt.executeQuery(sqlSelect);

        while(res.next()) {
            nom = res.getString("nom");
        }
        res.close();
        return nom;
    }

    public String getPrenom(String numeroCommande) throws Exception{
        String prenom = "";

        Statement stmt = connectBDD();

        String sqlSelect = "SELECT prenom FROM Commandes WHERE numero_commandes = \""+ numeroCommande +"\"";
        ResultSet res = stmt.executeQuery(sqlSelect);

        while(res.next()) {
            prenom = res.getString("prenom");
        }
        res.close();
        return prenom;
    }


    public List<Commande> researchByNumeroCommande(String numero) throws Exception{
        List<Commande> liste = new ArrayList<Commande>();

        Statement stmt = connectBDD();

        String sqlSelect = "SELECT * FROM Commande WHERE numero_commande = \""+ numero +"\"";
        ResultSet res = stmt.executeQuery(sqlSelect);

        while(res.next()) {
            int id_commande = res.getInt("id_commande");
            String numero_commande = res.getString("numero_commande");
            int total = res.getInt("total");
            String date = res.getString("date_commande");
            String payement = res.getString("payement");

            // Définir les valeurs
            Commande com = new Commande();
            com.setIdCommande(id_commande);
            com.setNumeroCommande(numero_commande);
            com.setTotal(total);
            com.setDateCommande(date);
            com.setPayement(payement);
            liste.add(com);
        }
        res.close();

        return liste;
    }

    public void UpToDatePaiementCommande(String typePaiement, String numCommande) throws Exception{
        Statement stmt = connectBDD();

        //Met a jour le statut de paiement de la commande en base
        String sqlUpdate = "UPDATE Commande SET payement = \""+ typePaiement +"\" WHERE numero_commande = \""+ numCommande +"\" AND payement = \""+"non payé"+"\"";
        stmt.executeUpdate(sqlUpdate);

    }


    public int getValeurChiffreAffaire(String date) throws Exception{
        Statement stmt = connectBDD();

        //récupère la somme de toutes commande de la date donnée en paramètre
        String sqlSelect = "SELECT SUM(total) FROM Commande WHERE date_commande LIKE \""+ date +"%\"";
        ResultSet res = stmt.executeQuery(sqlSelect);
        while(res.next()) {
            chiffreAffaire = res.getInt("SUM(total)");
        }
        res.close();
        return chiffreAffaire;
    }

    public void addArticle(String quantite, String reference) throws Exception{
        Statement stmt = connectBDD();

        //Met a jour le statut de paiement de la commande en base
        String sqlUpdate = "UPDATE Article SET stock = stock+"+ quantite +" WHERE reference = \""+ reference +"\"";
        stmt.executeUpdate(sqlUpdate);
    }

}
