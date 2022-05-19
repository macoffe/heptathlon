import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.Registry;
import java.util.List;
import java.awt.Dimension;

class GUICommande extends JPanel implements ActionListener {

    Registry reg;
    InteractInterface stub;

    JLabel rechercheNumeroCommandeLabel;
    JTextArea rechercheNumeroCommandeArea;
    JButton validationNumeroCommande;

    JLabel rechercheNomCommandeLabel;
    JTextArea rechercheNomCommandeArea;
    JButton validationNomCommande;

    JButton afficherToutesLesCommandes;
    JButton payerCommande;

    DefaultTableModel tableModel2;
    JTable resultat2;
    JScrollPane resultatSP2; 
    String colonnes2[]={"ID", "NUMERO COMMANDE", "NOM", "PRENOM","TOTAL", "DATE", "PAIEMENT"};

    JTextArea factureArea;
    JScrollPane factureSP;

    String numCommande = "";
    String nomClient = "";
    String prenomClient = "";

    //élément pour la pop-up du paiement
    String[] typePaiement = {"Carte", "Espèce", "Chèque"};
    JOptionPane jop = new JOptionPane();

    GUICommande (Registry reg, InteractInterface stub) throws Exception {

        this.reg = reg;
        this.stub = stub;
        
        //label recherche par numero de commande
        rechercheNumeroCommandeLabel = new JLabel("Numéro de commande :");
        this.add(rechercheNumeroCommandeLabel);

        //zone numero de commande à remplir par l'utilisateur
        rechercheNumeroCommandeArea = new JTextArea("");
        rechercheNumeroCommandeArea.setColumns(6);
        this.add(rechercheNumeroCommandeArea);

        //bouton recherche en fonction du numero de commande (1 seul résultat possible)
        validationNumeroCommande = new JButton("Rechercher par numéro");
        this.add(validationNumeroCommande);
        validationNumeroCommande.addActionListener(this);

        //label recherche par nom
        rechercheNomCommandeLabel = new JLabel("Nom :");
        this.add(rechercheNomCommandeLabel);

        //zone nom à remplir par l'utilisateur
        rechercheNomCommandeArea = new JTextArea("");
        rechercheNomCommandeArea.setColumns(6);
        this.add(rechercheNomCommandeArea);

        //bouton recherche en fonction du nom donnée lors de la commande
        validationNomCommande = new JButton("Rechercher par nom");
        this.add(validationNomCommande);
        validationNomCommande.addActionListener(this);

        //bouton pour afficher toutes les commandes
        afficherToutesLesCommandes = new JButton("Tout afficher");
        this.add(afficherToutesLesCommandes);
        afficherToutesLesCommandes.addActionListener(this);

        //tableau listant les articles
        tableModel2 = new DefaultTableModel(1, colonnes2.length) ;
        tableModel2.setColumnIdentifiers(colonnes2);

        resultat2 = new JTable(tableModel2);
        resultat2.setDefaultEditor(Object.class, null);
        resultatSP2 = new JScrollPane(resultat2);

        resultat2.setPreferredScrollableViewportSize(new Dimension(950,63));
        resultat2.setFillsViewportHeight(true);

        resultatSP2.setVisible(true);
        this.add(resultatSP2);

        //affichage d'une facture
        factureArea = new JTextArea();
        factureArea.setRows(15);
        factureArea.setEditable(false);
        factureSP = new JScrollPane(factureArea);
        this.add(factureSP);
        factureSP.setVisible(false);

        //bouton pour payer une facture
        payerCommande = new JButton("Payer commande");
        this.add(payerCommande);
        payerCommande.addActionListener(this);
        payerCommande.setVisible(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) { //actions en fonction du bouton sur lequel l'utilisateur clique

        String s = e.getActionCommand();
        List<Commande> liste_commande = null;

        //recherche commande par numéro de commande
        if(s.equals("Rechercher par numéro")) {
            try {
                liste_commande = this.stub.researchByNumeroCommande(this.rechercheNumeroCommandeArea.getText());
                payerCommande.setVisible(false);
                factureSP.setVisible(false);
            } 
            catch (Exception e1) { e1.printStackTrace();}
            rechercheNumeroCommandeArea.setText("");
            rechercheNomCommandeArea.setText("");
            factureArea.setText("");
        }

        //afficher toutes les commandes disponible
        if(s.equals("Tout afficher")) {
            try {
                liste_commande = this.stub.getAllCommande();
                payerCommande.setVisible(false);
                factureSP.setVisible(false);
            } 
            catch (Exception e1) { e1.printStackTrace();}
            rechercheNumeroCommandeArea.setText("");
            rechercheNomCommandeArea.setText("");
            factureArea.setText("");
        }

        if(s.equals("Rechercher par nom")) {
            try {
                liste_commande = this.stub.rechercheNom(this.rechercheNomCommandeArea.getText());
                payerCommande.setVisible(false);
                factureSP.setVisible(false);
            } 
            catch (Exception e1) { e1.printStackTrace();}
            rechercheNumeroCommandeArea.setText("");
            rechercheNomCommandeArea.setText("");
            factureArea.setText("");  
        }

        tableModel2.setRowCount(0);


        Integer cptCommande = 0;
        String[] dataReference = new String[7];

        //affiche la liste des commandes 
        if (liste_commande != null){
            for (Commande a : liste_commande) {
                if(cptCommande == 0){
                    numCommande = a.getNumeroCommande();
                }

                dataReference[0] = Integer.toString(a.getIdCommande());
                dataReference[1] = a.getNumeroCommande();

                //on recupere pour chaque commande le nom client associé
                try {nomClient = this.stub.getNom(a.getNumeroCommande());} 
                catch (Exception e1) {e1.printStackTrace();}
                dataReference[2] =  nomClient;

                //on recupere pour chaque commande le prenom client associé
                try {prenomClient = this.stub.getPrenom(a.getNumeroCommande());} 
                catch (Exception e1) {e1.printStackTrace();}
                dataReference[3] =  prenomClient;

                dataReference[4] = Integer.toString(a.getTotal());
                dataReference[5] = a.getDateCommande();
                dataReference[6] = a.getPayement();
                tableModel2.addRow(dataReference);
                cptCommande++;
            }
        }
        //si on obtient 1 correspondance d'article on affiche le bouton d'ajout de l'article à la commande
        if (cptCommande == 1 ){
            if (dataReference[6].equals("non payé")){
                payerCommande.setVisible(true); 
            }
            try {
                List<String> facture = this.stub.getFacture(dataReference[1], dataReference[2], dataReference[3]);
                for (String line : facture) {
                    factureArea.setText(factureArea.getText()+line+"\n");
                }
                factureSP.setVisible(true);
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }   

        //Paiement d'un commande
        if(s.equals("Payer commande")) {
            String paiement = (String)JOptionPane.showInputDialog(null, "Veuillez renseignez le choix de paiement !","Choix de la méthode de paiement",JOptionPane.QUESTION_MESSAGE,null,typePaiement,typePaiement[0]);
            if(paiement != null){
                try {
                    this.stub.UpToDatePaiementCommande(paiement,numCommande);
                    this.stub.updateFacture(numCommande, nomClient, prenomClient, paiement);
                } catch (Exception e1) { 
                    e1.printStackTrace();
                }
            }
            
        }

        tableModel2.fireTableDataChanged();

    }

}