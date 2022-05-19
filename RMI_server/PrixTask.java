import java.util.List;
import java.util.TimerTask;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class PrixTask extends TimerTask {

    InteractInterfaceCentral stub;
    Connection conn = null;
    Statement stmt = null;

    PrixTask(InteractInterfaceCentral stub){
        this.stub = stub;
    }

    @Override
    public void run() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/HeptathlonMagasin", "rmi", "rmi");
            stmt = conn.createStatement();

            List<Article> listeArticle = stub.updateArticlesPrix();
            for (Article a : listeArticle) {
                String sql = "UPDATE Article SET prix_article = "+a.getPrix()+" WHERE reference = \""+ a.getReference()+"\"";
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
		    e.printStackTrace();
        }
        System.out.println("Mise à jour des prix");
  
    }

}   