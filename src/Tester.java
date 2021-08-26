import ConfVoters.ChangeCoupling;
import ConfVoters.PackageDistance;
import Executor.Extractor;
import Untangler.Untangler;

import java.util.HashMap;

public class Tester {

    public static void main(String args[]) {

        HashMap<String, Beans.Commit> list = (new Extractor("https://github.com/sambrosio17/TestRepo.git")).doExtract();



       Untangler algo = new Untangler(list.get("cb5743ce995c43c03a49e2bc83fecb097a171aa8"), list, 3);
       System.out.println(algo.doUntangle());
    }


}
