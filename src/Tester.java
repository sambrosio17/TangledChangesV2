import ConfVoters.ChangeCoupling;
import ConfVoters.PackageDistance;
import Executor.Extractor;
import Untangler.Untangler;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Tester {

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {

        HashMap<String, Beans.Commit> list = (new Extractor("https://github.com/sambrosio17/RistoManager.git")).doExtract();



        Untangler algo = new Untangler(list.get("4296ab366c9895ef7c16b33b7dea68c3a7d33b1a"), list, 2);
        //algo.buildPartitionMatrix();
        System.out.println(algo.doUntangle());
    }


}
