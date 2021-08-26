package ConfVoters;

public class PackageDistance {

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

        return counter;
    }
}
