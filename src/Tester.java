import ConfVoters.ChangeCoupling;
import ConfVoters.PackageDistance;
import Executor.Extractor;

import java.util.HashMap;

public class Tester {

    public static void main(String args[]){

        HashMap<String, Beans.Commit> list=(new Extractor("https://github.com/sambrosio17/RistoManager.git")).doExtract();

        PackageDistance pd=new PackageDistance();
        ChangeCoupling cc=new ChangeCoupling(list);
        System.out.println(cc.doCalculate("RistoManager/src/it/RistoManager/Control/Utente/VisualizzaCodicePrenotato.java","RistoManager/src/it/RistoManager/Control/Utente/Registrazione.java"));
    }
}
