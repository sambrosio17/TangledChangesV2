package Untangler;

import Beans.Commit;
import Beans.Partition;
import Beans.PartitionItem;
import ConfVoters.ChangeCoupling;
import ConfVoters.PackageDistance;

import java.util.*;

public class Untangler {

    private Beans.Commit commit;
    private HashMap<String, Commit> map;
    private PackageDistance packageDistance;
    private ChangeCoupling changeCoupling;
    private List<Partition> partitionList;
    private int stopCondition;

    public Untangler(Beans.Commit commit, HashMap<String, Commit> map, int stopCondition){
        this.commit = commit;
        this.map= map;
        this.stopCondition = stopCondition;
        packageDistance=new PackageDistance();
        changeCoupling=new ChangeCoupling(map);
        partitionList=new ArrayList<>();

    }

    public void buildPartitionMatrix(){

        for(int i=0; i<commit.getChanges().size(); i++){
            Partition currentPartition=new Partition();
            currentPartition.setId(i);
            for(int j=0; j<commit.getChanges().size(); j++){
                if(i>=j) continue;
                PartitionItem currentPartitionItem= new PartitionItem();
                currentPartitionItem.setI(i);
                currentPartitionItem.setJ(j);
                int pdValue=packageDistance.doCalculate(commit.getChanges().get(i).getPath(),commit.getChanges().get(j).getPath());
                int ccValue=changeCoupling.doCalculate(commit.getChanges().get(i).getPath(),commit.getChanges().get(j).getPath());
                currentPartitionItem.setConfidenceValue(pdValue); //dobbiamo cangià
                currentPartitionItem.getPaths().add(commit.getChanges().get(i).getPath());
                currentPartitionItem.getPaths().add(commit.getChanges().get(j).getPath());
                currentPartition.getPartitionItemList().add(currentPartitionItem);
            }
            partitionList.add(currentPartition);
        }

        //System.out.println(partitionList);

    }

    private PartitionItem findMax(){
        PartitionItem max= partitionList.get(0).findMax();

        for(int i=0; i<partitionList.size(); i++){
            if(max==null || partitionList.get(i).findMax()==null) continue;
            if(partitionList.get(i).findMax().getConfidenceValue() > max.getConfidenceValue()) max=partitionList.get(i).findMax();
        }

        return max;
    }

    private int compositeConfVoter(Partition x, Partition y){

        PriorityQueue<Integer> maxPQueue = new PriorityQueue<>(Collections.reverseOrder());

        //System.out.println(x);
        //System.out.println(y);
        for(int i=0; i<x.getPartitionItemList().size(); i++){
            for(int j=0; j<y.getPartitionItemList().size(); j++ ){
                if(partitionList.get(i).findOne(i,j) == null) continue;
                maxPQueue.add(partitionList.get(i).findOne(i,j).getConfidenceValue());

                //System.out.println(x+"\n"+y+" i: "+i+" j: "+j+" item: "+partitionList.get(i).findOne(i,j));
            }
        }
        return maxPQueue.poll();
    }

    private int activePartition(){
        int counter=0;
        for(Partition p:partitionList){
            if(p.isActive()) counter++;
        }
        return counter;
    }

    private List<Partition> getActivePartitions(){
        List<Partition> resultPartition=new ArrayList<>();
        for(Partition p:partitionList){
            if(p.isActive()) resultPartition.add(p);
        }
        return resultPartition;
    }

    public List<Partition> doUntangle(){

        buildPartitionMatrix();

        while(activePartition()>stopCondition){
            //troviamo il max tra tutte le celle della matrice, ovvero il PartitionItem
            PartitionItem max=findMax();
            int indexI=max.getI();
            int indexJ=max.getJ();
            //disiattivamo le righe e le colonne relative a indexI e indexJ (relative al max)
            //disiattivamo le righe
            partitionList.get(indexI).setActive(false);
            partitionList.get(indexJ).setActive(false);
            //disattivamo le colonne
            for(Partition p: partitionList){
                p.deactiveIndex(indexI);
                p.deactiveIndex(indexJ);
            }
            //creiamo una partizione partitionList.size()+1 che unisce le due precedenti
            Partition compositePartition= new Partition();
            compositePartition.getPartitionItemList().add(max);
            compositePartition.setId(partitionList.size());
            for(int j=0; j<partitionList.get(j).getPartitionItemList().size(); j++){
                PartitionItem compositePartitionItem=new PartitionItem();
                compositePartitionItem.setI(partitionList.size());
                compositePartitionItem.setJ(j);
                compositePartitionItem.setConfidenceValue(compositeConfVoter(compositePartition,partitionList.get(j)));
                /*for(String path : partitionList.get(j).getPartitionItemList().get(partitionList.size()).getPaths()){
                    compositePartitionItem.getPaths().add(path);
                }*/
                compositePartition.getPartitionItemList().add(compositePartitionItem);
            }
            partitionList.add(compositePartition);
            for(int k=0; k<partitionList.size(); k++){
                PartitionItem compositePartionItem= new PartitionItem();
                compositePartionItem.setI(k);
                compositePartionItem.setJ(partitionList.size());
                int confidenceValue=compositeConfVoter(partitionList.get(k),compositePartition);
                compositePartionItem.setConfidenceValue(confidenceValue);
                /*for(String path: partitionList.get(partitionList.size()).getPartitionItemList().get(k).getPaths()){
                    compositePartionItem.getPaths().add(path);
                }*/
                partitionList.get(k).getPartitionItemList().add(compositePartionItem);
            }

        }


        return getActivePartitions();
    }



}
