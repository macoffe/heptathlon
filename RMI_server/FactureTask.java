import java.util.TimerTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FactureTask extends TimerTask {

  InteractInterfaceCentral stub;

  FactureTask(InteractInterfaceCentral stub){
    this.stub = stub;
  }

  @Override
  public void run() {
    byte[] contenu = null;
    String text = "";
    try {
      contenu = Files.readAllBytes(Paths.get("facture"));
      text = stub.sendFacture(contenu, "facture");
      System.out.println(text);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}