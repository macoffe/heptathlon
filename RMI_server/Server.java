import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Server extends InteractBDD {

    public Server() throws RemoteException {}

    public static void main(String args[]) {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            // crée l'objet distant
            InteractInterface obj = new InteractBDD();

            // Liaison de l'objet distant (stub) dans le Registre
            Registry reg = LocateRegistry.createRegistry(6600); 

            reg.bind("Heptathlon", obj);
    
            System.out.println("Le Serveur Magasin est prêt...");

            reg = LocateRegistry.getRegistry("127.0.0.1", 6700);
            InteractInterfaceCentral stub2 = (InteractInterfaceCentral) reg.lookup("HeptathlonCentral");

            Calendar factureCal = Calendar.getInstance();
            factureCal.set(Calendar.HOUR_OF_DAY, 20);
            factureCal.set(Calendar.MINUTE, 0);
            factureCal.set(Calendar.SECOND, 0);

            Calendar prixCal = Calendar.getInstance();
            prixCal.set(Calendar.HOUR_OF_DAY, 6);
            prixCal.set(Calendar.MINUTE, 0);
            prixCal.set(Calendar.SECOND, 0);

            // every night at 2am you run your task
            Timer timer = new Timer();
            timer.schedule(new FactureTask(stub2), factureCal.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day
            timer.schedule(new PrixTask(stub2), prixCal.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day

        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }
}
