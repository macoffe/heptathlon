import java.io.Serializable;

public class Article implements Serializable {
    private int id;
    private String reference;
    private String nom;
    private String famille;
    private int prix;
    private int stock;

    public int getId() {
        return id;
    }
    public String getReference() {
        return reference;
    }
    public String getNom() {
        return nom;
    }
    public String getFamille() {
        return famille;
    }
    public int getPrix() {
        return prix;
    }
    public int getStock() {
        return stock;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setFamille(String famille) {
        this.famille = famille;
    }
    public void setPrix(int prix) {
        this.prix = prix;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}