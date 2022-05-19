import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Random;
import java.awt.Dimension;

class GUIRecherche extends JPanel implements ActionListener {

    Registry reg;
    InteractInterface stub;

    JLabel rechercheReferenceLabel;
    JTextArea rechercheReferenceArea;
    JButton validationRechercheReference;

    JLabel rechercheFamilleLabel;
    JTextArea rechercheFamilleArea;
    JButton validationRechercheFamille;

    JButton acheterArticle;
    JButton validerCommande;

    DefaultTableModel tableModel;
    JTable resultat;
    JScrollPane resultatSP; 
    String colonnes[]={"ID", "REFERENCE", "NOM", "PRIX", "STOCK"};

    String referenceArticle = "";
    String numeroCommande = "";

    Random randomClient = new Random();
    Integer nbAleaClient;

    GUIRecherche (Registry reg, InteractInterface stub) throws Exception {

        this.reg = reg;
        this.stub = stub;
        
        //génère un nombre aléatoire pour créer des noms/prenoms de clients fictifs
        nbAleaClient = randomClient.nextInt(501);

        numeroCommande = genNumeroCommande();
        nbAleaClient = genNbrAleatoire();

        //label recherche par référence
        rechercheReferenceLabel = new JLabel("Référence de l'article :");
        this.add(rechercheReferenceLabel);

        //zone référence à remplir par l'utilisateur
        rechercheReferenceArea = new JTextArea("");
        rechercheReferenceArea.setColumns(6);
        this.add(rechercheReferenceArea);

        //bouton recherche en fonction de la référence de l'article (1 seul résultat possible)
        validationRechercheReference = new JButton("Rechercher par référence");
        this.add(validationRechercheReference);
        validationRechercheReference.addActionListener(this);

        //label recherche par famille
        rechercheFamilleLabel = new JLabel("Famille d'article :"); 
        this.add(rechercheFamilleLabel);

        //zone famille à remplir par l'utilisateur
        rechercheFamilleArea = new JTextArea("");
        rechercheFamilleArea.setColumns(6);
        this.add(rechercheFamilleArea);

        //bouton recherche en fonction de la famille de l'article
        validationRechercheFamille = new JButton("Rechercher par famille");
        this.add(validationRechercheFamille);
        validationRechercheFamille.addActionListener(this);

        //bouton d'ajout d'un article à la commande
        acheterArticle = new JButton("Ajouter article");
        this.add(acheterArticle);
        acheterArticle.addActionListener(this);
        acheterArticle.setVisible(false);

        //tableau listant les articles
        tableModel = new DefaultTableModel(1, colonnes.length) ;
        tableModel.setColumnIdentifiers(colonnes);

        resultat = new JTable(tableModel);
        resultatSP = new JScrollPane(resultat);

        resultat.setPreferredScrollableViewportSize(new Dimension(850,63));
        resultat.setDefaultEditor(Object.class, null);
        resultat.setFillsViewportHeight(true);

        this.add(resultatSP);

        //bouton de validation de la commande
        validerCommande = new JButton("Valider la commande");
        this.add(validerCommande);
        validerCommande.addActionListener(this);
        validerCommande.setVisible(false);
        
        this.setPreferredSize(new Dimension(1000, 1000));

    }

    @Override
    public void actionPerformed(ActionEvent e) { //actions en fonction du bouton sur lequel l'utilisateur clique

        String s = e.getActionCommand();
        List<Article> liste_article = null;

        //recherche par référence
        if(s.equals("Rechercher par référence")) {
            try {
                liste_article = this.stub.researchByReference(this.rechercheReferenceArea.getText());
                acheterArticle.setVisible(false);
            } 
            catch (Exception e1) { e1.printStackTrace();}
            rechercheReferenceArea.setText("");
        }

        //recherche par famille
        if(s.equals("Rechercher par famille")) {
            try {
                liste_article = this.stub.researchByFamille(this.rechercheFamilleArea.getText());
                acheterArticle.setVisible(false);
            } 
            catch (Exception e1) { e1.printStackTrace();}
            rechercheFamilleArea.setText("");
        }

        tableModel.setRowCount(0);
        
        Integer cptArticle = 0;
        String[] dataReference = new String[5];

        //affiche la liste des articles récupérés en base selon la requête SQL
        if (liste_article != null){
            for (Article a : liste_article) {
                if(cptArticle == 0){
                    referenceArticle = a.getReference();
                }
                dataReference[0] = Integer.toString(a.getId());
                dataReference[1] = a.getReference();
                dataReference[2] = a.getNom();
                dataReference[3] = Integer.toString(a.getPrix());
                dataReference[4] = Integer.toString(a.getStock());
                tableModel.addRow(dataReference);
                cptArticle++;
            }
        }
        //si on obtient 1 correspondance d'article on affiche le bouton d'ajout de l'article à la commande
        if (cptArticle == 1){
            acheterArticle.setVisible(true);
        }
     
        tableModel.fireTableDataChanged();

        //ajout d'un article à la commande
        if(s.equals("Ajouter article")) {
            acheterArticle.setVisible(false);
            System.out.println("Entrée dans ajout d'article ...");
            try {
                this.stub.addArticleToCommande(referenceArticle,numeroCommande,nbAleaClient);
                validerCommande.setVisible(true); //une fois qu'au moins un article a été ajouter on peut afficher le bouton de validation de commande
            } catch (Exception e1) { 
                e1.printStackTrace();
            }
        }

        //Validation d'un commande
        if(s.equals("Valider la commande")) {
            System.out.println("Entrée dans validation commande ...");
            try {
                this.stub.createCommande(numeroCommande);
                numeroCommande = genNumeroCommande();
                nbAleaClient = genNbrAleatoire();
                validerCommande.setVisible(false);
            } catch (Exception e1) { 
                e1.printStackTrace();
            }
        }

    }

    public String genNumeroCommande(){
        Random rand = new Random();
        numeroCommande = "";
        //génère un numéro de commande aléatoire de 6 caractères
        for(int i = 0 ; i < 6; i++){
          char c = (char)(rand.nextInt(26) + 97);
          numeroCommande += c;
        }
        return numeroCommande.toUpperCase();
    }

    public int genNbrAleatoire(){
        //génère un nombre aléatoire pour créer des noms/prenoms de clients fictifs
        nbAleaClient = randomClient.nextInt(501);

        return nbAleaClient;
    }

}