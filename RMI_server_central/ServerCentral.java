import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ServerCentral extends InteractBDDCentral {

    public ServerCentral() throws RemoteException {}

    public static void main(String args[]) {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            // crée l'objet distant
            InteractInterfaceCentral obj = new InteractBDDCentral();

            // Liaison de l'objet distant (stub) dans le Registre
            Registry reg = LocateRegistry.createRegistry(6700); 

            reg.bind("HeptathlonCentral", obj);
            System.out.println("Le Serveur Central est prêt...");
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }
}
