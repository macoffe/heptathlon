import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.Registry;

class GUIAffaire extends JPanel implements ActionListener {

    Registry reg;
    InteractInterface stub;
    
    JLabel dateChiffreAffaireLabel;
    JTextArea dateChiffreAffaireArea;
    JButton calculChiffreAffaire;

    JLabel chiffreAffaire;

    JLabel ajoutArticleRefLabel;
    JTextArea ajoutArticleRefArea;
    JLabel ajoutArticleQteLabel;
    JTextArea ajoutArticleQteArea;
    JButton ajoutArticleButton;

    GUIAffaire (Registry reg, InteractInterface stub) throws Exception {

        this.reg = reg;
        this.stub = stub;
        
        //label date chiffre d'affaire
        dateChiffreAffaireLabel = new JLabel("Date (format YYYY-MM-DD):");
        this.add(dateChiffreAffaireLabel);

        //zone nom à remplir par l'utilisateur
        dateChiffreAffaireArea = new JTextArea("");
        dateChiffreAffaireArea.setColumns(6);
        this.add(dateChiffreAffaireArea);

        //bouton calcul chiffre d'affaire en fonction de la date donnée
        calculChiffreAffaire = new JButton("Calculer chiffre d'affaire");
        this.add(calculChiffreAffaire);
        calculChiffreAffaire.addActionListener(this);

        //label qui affichera la reponse du calcul du chiffre d'affaire
        chiffreAffaire = new JLabel("");
        this.add(chiffreAffaire);

        ajoutArticleRefLabel = new JLabel("Référence de l'article à ajouter:");
        this.add(ajoutArticleRefLabel);

        ajoutArticleRefArea = new JTextArea("");
        ajoutArticleRefArea.setColumns(6);
        this.add(ajoutArticleRefArea);

        ajoutArticleQteLabel = new JLabel("Quantité à ajouter:");
        this.add(ajoutArticleQteLabel);

        ajoutArticleQteArea = new JTextArea("");
        ajoutArticleQteArea.setColumns(6);
        this.add(ajoutArticleQteArea);

        ajoutArticleButton = new JButton("Ajouter stock");
        this.add(ajoutArticleButton);
        ajoutArticleButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) { //actions en fonction du bouton sur lequel l'utilisateur clique

        String s = e.getActionCommand();

        //afficher toutes les commandes disponible
        if(s.equals("Calculer chiffre d'affaire")) {
            try {
                int valeurChiffreAffaire = this.stub.getValeurChiffreAffaire(this.dateChiffreAffaireArea.getText());
                String chiffreAffaireString = String.valueOf(valeurChiffreAffaire);
                chiffreAffaire.setText(chiffreAffaireString+" € de chiffre d'affaire le "+this.dateChiffreAffaireArea.getText());
                chiffreAffaire.setVisible(true);
            } 
            catch (Exception e1) { e1.printStackTrace();}
            dateChiffreAffaireArea.setText("");
        }

        //ajoute du stock pour l'article
        if(s.equals("Ajouter stock")) {
            try {
                this.stub.addArticle(this.ajoutArticleQteArea.getText(), this.ajoutArticleRefArea.getText());
            } 
            catch (Exception e1) { e1.printStackTrace();}
            ajoutArticleQteArea.setText("");
            ajoutArticleRefArea.setText("");
        }

    }

}