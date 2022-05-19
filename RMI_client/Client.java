import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;  
import java.lang.Exception; 


public class Client extends JFrame implements ActionListener {

    //initialize button, panel, label, and text field  
    JButton b1;  
    JPanel newPanel;  
    JPanel panelFormulaire;
    JLabel userLabel, passLabel;  
    final JTextField  textField1, textField2;
    Registry reg;
    InteractInterface stub;
     
    private Client(){
        try {
            reg = LocateRegistry.getRegistry("127.0.0.1", 6600);
            stub = (InteractInterface) reg.lookup("Heptathlon");  
        }
        catch (RemoteException re) {
            System.err.println(re.toString());
        }
        catch (NotBoundException nbe) {
             System.err.println(nbe.toString());
        }
 
        //username
        userLabel = new JLabel();  
        userLabel.setText("Identifiant");  
        textField1 = new JTextField(15);

        //password
        passLabel = new JLabel();  
        passLabel.setText("Mot de passe");       
        textField2 = new JPasswordField(15);   
          
        //Bouton de validation
        b1 = new JButton("Entrer");
          
        //create panel to put form elements  
        newPanel = new JPanel(new GridLayout(2,1,50,50));

        panelFormulaire = new JPanel(new GridLayout(2,2,50,100));
        panelFormulaire.add(userLabel);
        panelFormulaire.add(textField1);
        panelFormulaire.add(passLabel);
        panelFormulaire.add(textField2);
        newPanel.add(panelFormulaire);
        
        newPanel.add(b1);    

        //set border to panel   
        add(newPanel);  
          
        //perform action on button click   
        b1.addActionListener(this);
        setTitle("Heptathlon");
    }

    public void actionPerformed(ActionEvent ae) { 

        String userValue = textField1.getText();  
        String passValue = textField2.getText(); 
        
        try{
            String checkLogin = this.stub.checkLogin(userValue, passValue);
            if (checkLogin.equals("erreur")) {
                JLabel pLabel = new JLabel();  
                pLabel.setText("Please enter valid username and password");
                newPanel.add(pLabel);
            } else {
                newPanel.setVisible(false);
            
                UIManager.setLookAndFeel( new NimbusLookAndFeel() );
                GUIMain userInterface = new GUIMain(reg, stub);
            
                userInterface.setVisible(true);

                //supprime la JFrame de login
                this.setVisible(false);
                this.dispose();
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
          
    }

    public static void main(String[] args) throws Exception {

        Client form = new Client();

        try  {      
            form.setSize(900,450);  
            form.setLocationRelativeTo(null);
            form.setVisible(true); 
        }  
        catch(Exception e)  {      
            JOptionPane.showMessageDialog(null, e.getMessage());  
        }  
          

    }
}