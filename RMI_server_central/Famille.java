import java.io.Serializable;

public class Famille implements Serializable {
    private int id;
    private String nom;

    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
}