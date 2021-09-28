package ConfVoters;

import org.eclipse.jgit.internal.storage.file.Pack;

public class PackageDistance {

    private int upperBound;

    public PackageDistance(){

    }

    public PackageDistance(int upperBound){
        this.upperBound = upperBound;
    }

    public int doCalculate(String path1,String path2){

        String[] firstPath=path1.split("/");
        String[] secondPath=path2.split("/");

        int counter=0;

        int bigger= firstPath.length > secondPath.length ? firstPath.length : secondPath.length;
        int minor= firstPath.length < secondPath.length ? firstPath.length : secondPath.length;

        for(int k=0; k<bigger-1; k++) {

            if(k>=minor){
                break;
            }
            if (firstPath[k].equals(secondPath[k])) {
                continue;
            }
            if (!firstPath[k].equals(secondPath[k])) {

                counter++;
            }
        }

        return  1-(counter/upperBound); //porto il valore di counter a decimale e gli sottraggo 1 per dargli un livello di bontà positivo
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }
}
