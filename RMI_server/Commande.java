import java.io.Serializable;

public class Commande implements Serializable{
    private int id_commande;
    private String numero_commande;
    private int total;
    private String date_commande;
    private String payement;
    

    public int getIdCommande() {
        return id_commande;
    }
    public String getNumeroCommande() {
        return numero_commande;
    }
    public int getTotal() {
        return total;
    }
    public String getDateCommande() {
        return date_commande;
    }
    public String getPayement() {
        return payement;
    }

    public void setIdCommande(int id_commande) {
        this.id_commande = id_commande;
    }
    public void setNumeroCommande(String numero_commande) {
        this.numero_commande = numero_commande;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public void setDateCommande(String date) {
        this.date_commande = date;
    }
    public void setPayement(String payement) {
        this.payement = payement;
    }
}
