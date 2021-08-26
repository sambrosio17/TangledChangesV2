package Beans;

import java.util.ArrayList;
import java.util.List;

public class Partition {

    private int id;
    private List<PartitionItem> partitionItemList;
    private boolean active;

    public Partition(){
        id=-1;
        partitionItemList=new ArrayList<>();
        active=true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PartitionItem> getPartitionItemList() {
        return partitionItemList;
    }

    public void setPartitionItemList(List<PartitionItem> partitionItemList) {
        this.partitionItemList = partitionItemList;
    }

    public boolean isActive() {
        return active;
    }

    private void deactiveItem(){
        for(PartitionItem p : partitionItemList){
            p.setActive(this.active);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        deactiveItem();
    }

    public void deactiveIndex(int j){
        for(PartitionItem p : partitionItemList){
            if(p.getJ()==j) p.setActive(false);
        }
    }

    public PartitionItem findMax(){
        if(partitionItemList.isEmpty()) return null;

        PartitionItem max=partitionItemList.get(0);

        for(int i=0; i<partitionItemList.size(); i++){
            for(int j=1; j<partitionItemList.size()-1; j++){
                if(partitionItemList.get(j).getConfidenceValue()>max.getConfidenceValue())
                    max=partitionItemList.get(j);
            }
        }

        return max;
    }

    public PartitionItem findOne(int i, int  j){

        for(PartitionItem p : partitionItemList){
            if(p.getI()==i && p.getJ()==j)
                return p;
        }

        return null;
    }

    @Override
    public String toString() {
        return "Partition{" +
                "id=" + id +
                ", partitionItemList=" + partitionItemList +
                ", active=" + active +
                '}';
    }
}
