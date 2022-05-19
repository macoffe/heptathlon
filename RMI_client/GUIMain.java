import javax.swing.*;
import java.awt.Dimension;
import java.rmi.registry.Registry;

class GUIMain extends JFrame { 

    Registry reg;
    InteractInterface stub;

    GUIRecherche onglet1;
    GUICommande onglet2;
    GUIAffaire onglet3;

    GUIMain(Registry reg, InteractInterface stub) throws Exception {  

        super( "Heptathlon" ); //nom de la fenetre
        this.setSize( 1050, 600 );
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

        //--------------------------------------------------------ONGLET RECHERCHE & CREATION DE COMMANDES-----------------------------------------------
        onglet1 = new GUIRecherche(reg, stub);
        onglet1.setPreferredSize(new Dimension(1000, 1000));
        onglets.addTab("Recherche & Création de commande", onglet1);
        //--------------------------------------------------------ONGLET CONSULTATION ET PAIEMENT--------------------------------------------------------
        onglet2 = new GUICommande(reg, stub);
        onglet2.setPreferredSize(new Dimension(1000, 600));
        onglets.addTab("Consultation & paiement", onglet2);
        //--------------------------------------------------------ONGLET CHIFFRE D'AFFAIRE---------------------------------------------------------------
        onglet3 = new GUIAffaire(reg, stub);
        onglet3.setPreferredSize(new Dimension(1000, 1000));
        onglets.addTab("Chiffre d'affaire & Ajout d'article", onglet3);

        //ajout des onglets
        onglets.setOpaque(true);
        mainPanel.add(onglets);
        this.getContentPane().add(mainPanel);
        this.setVisible(true);        
        
    }
    
}

