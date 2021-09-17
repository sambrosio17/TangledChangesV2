package Untangler;

import Beans.Commit;
import Beans.Partition;
import Beans.PartitionItem;
import ConfVoters.ChangeCoupling;
import ConfVoters.PackageDistance;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Untangler {

    private Beans.Commit commit;
    private HashMap<String, Commit> map;
    private PackageDistance packageDistance;
    private ChangeCoupling changeCoupling;
    public List<Partition> partitionList;
    private int stopCondition;
    private int startingMatrixSize=0;

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
            currentPartition.getGeneratedFrom().add(i);
            currentPartition.getPaths().add(commit.getChanges().get(i).getPath());
            for(int j=0; j<commit.getChanges().size(); j++){
                //if(i>=j) continue;
                PartitionItem currentPartitionItem= new PartitionItem();
                currentPartitionItem.setPartitionIndex(i);
                currentPartitionItem.setI(i);
                currentPartitionItem.setJ(j);
                int pdValue=packageDistance.doCalculate(commit.getChanges().get(i).getPath(),commit.getChanges().get(j).getPath());
                int ccValue=changeCoupling.doCalculate(commit.getChanges().get(i).getPath(),commit.getChanges().get(j).getPath());
                if(i>=j){
                    currentPartitionItem.setConfidenceValue(-1);
                }
                else {
                    currentPartitionItem.setConfidenceValue(pdValue); //dobbiamo cangià
                }
                currentPartitionItem.getPaths().add(commit.getChanges().get(i).getPath());
                currentPartitionItem.getPaths().add(commit.getChanges().get(j).getPath());
                currentPartition.getPartitionItemList().add(currentPartitionItem);
            }
            currentPartition.setActive(true);
            partitionList.add(currentPartition);
        }

        startingMatrixSize=partitionList.size();

    }

    //ok
    private PartitionItem findMax(){
        PartitionItem max=new PartitionItem();

        for(Partition p : partitionList){
            if(!p.isActive()) continue;
            max=p.findMax();
            break;
        }
        for(int i=0; i<partitionList.size(); i++){
            if(!partitionList.get(i).isActive()) continue;
            if(max==null || partitionList.get(i).findMax()==null) continue;
            if(partitionList.get(i).findMax().getConfidenceValue() > max.getConfidenceValue())
                max=partitionList.get(i).findMax();
        }

        return max;
    }

    private PartitionItem findOne(int i, int j){
        if(i<=j){
            return partitionList.get(i).getPartitionItemList().get(j);
        }
        else {
            return partitionList.get(j).getPartitionItemList().get(i);
        }
    }

    private int compositeValue(Partition a, Partition b){

        PriorityQueue<Integer> maxPQueue = new PriorityQueue<>(Collections.reverseOrder());
        for(int i=0; i<a.getGeneratedFrom().size(); i++){
            int k=a.getGeneratedFrom().get(i);
            if(a.getGeneratedFrom().get(i)>=startingMatrixSize) continue;
            for(int j=0; j<b.getGeneratedFrom().size(); j++){
                if(b.getGeneratedFrom().get(j)>=startingMatrixSize) continue;
                int z=b.getGeneratedFrom().get(j);
                PartitionItem item=findOne(k,z);

                if(item!=null){
                    maxPQueue.add(item.getConfidenceValue());
                }

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

    public List<Partition> doUntangle() throws FileNotFoundException, UnsupportedEncodingException {

        buildPartitionMatrix();

        while(activePartition()>stopCondition){

            //cella della matrice con il confidence value (primo incontrato)
            PartitionItem max=findMax();
            int indexI=max.getI();
            int indexJ=max.getJ();
            //elimino (disattivo) le partizioni indexI e indexJ (righe)
            partitionList.get(indexI).setActive(false);
            partitionList.get(indexJ).setActive(false);
            //disattivo tutti i PartitionItem che hanno come indice j o indexI o indexJ
            for(Partition p: partitionList){
                p.deactiveIndex(indexI);
                p.deactiveIndex(indexJ);
            }
            int matrixSize=partitionList.size();
            //creo la partizione matrixSize+1
            Partition compositePartition= new Partition();
            compositePartition.setId(matrixSize);
            compositePartition.getGeneratedFrom().addAll(Arrays.asList(matrixSize,indexI,indexJ));
            compositePartition.getPaths().addAll(partitionList.get(indexI).getPaths());
            compositePartition.getPaths().addAll(partitionList.get(indexJ).getPaths());
            for(int j=0; j<matrixSize; j++){
                if(partitionList.get(j).isActive()==false){
                    continue;
                }
                PartitionItem compositeItem=new PartitionItem();
                compositeItem.setI(matrixSize);
                compositeItem.setJ(j);
                compositeItem.getPaths().addAll(compositePartition.getPaths());
                compositeItem.getPaths().addAll(partitionList.get(j).getPaths());
                compositeItem.setConfidenceValue(compositeValue(compositePartition,partitionList.get(j)));
                compositePartition.getPartitionItemList().add(compositeItem);
            }
            partitionList.add(compositePartition);
            matrixSize=partitionList.size();
            for(int i=0; i<matrixSize; i++){
                if(partitionList.get(i).isActive()==false){
                    continue;
                }
                PartitionItem compositeItem=new PartitionItem();
                compositeItem.setI(i);
                compositeItem.setJ(matrixSize);
                compositeItem.getPaths().addAll(partitionList.get(i).getPaths());
                compositeItem.getPaths().addAll(partitionList.get(matrixSize-1).getPaths());
                compositeItem.setConfidenceValue(compositeValue(partitionList.get(i),partitionList.get(matrixSize-1)));
                partitionList.get(i).getPartitionItemList().add(compositeItem);
            }
        }

        return getActivePartitions();
    }



}
