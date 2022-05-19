import java.rmi.*;
import java.util.*;
import java.sql.*;

// Créer l'interface de l'objet distante
public interface InteractInterfaceCentral extends Remote {

    public Statement connectBDD() throws Exception;
    public List<Article> updateArticlesPrix() throws Exception;
    public String sendFacture(byte[] contenu, String nom) throws RemoteException ;

}