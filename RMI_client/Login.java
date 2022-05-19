import java.io.Serializable;

public class Login implements Serializable {
    private int id;
    private String login;
    private String mdp;
    private String type;

    public int getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getMdp() {
        return mdp;
    }
    public String getType() {
        return type;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    public void setType(String type) {
        this.type = type;
    }
}