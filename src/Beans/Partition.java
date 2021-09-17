package Beans;

import java.util.ArrayList;
import java.util.List;

public class Partition {

    private int id;
    private List<Integer> generatedFrom;
    private List<String> paths;
    private List<PartitionItem> partitionItemList;
    private boolean active;

    public Partition(){
        id=-1;
        partitionItemList=new ArrayList<>();
        active=true;
        generatedFrom=new ArrayList<>();
        paths=new ArrayList<>();
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

    public List<Integer> getGeneratedFrom() {
        return generatedFrom;
    }

    public void setGeneratedFrom(List<Integer> generatedFrom) {
        this.generatedFrom = generatedFrom;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }


    public PartitionItem findMax(){

        List<PartitionItem> actives=new ArrayList<>();
        for(PartitionItem pItem : partitionItemList){
            if(pItem.isActive()) actives.add(pItem);
        }

        if(actives.isEmpty()) return null;
        PartitionItem max=actives.get(0);


        for(PartitionItem item : actives){
            if(item.getConfidenceValue()> max.getConfidenceValue()){
                max=item;
            }
        }

        return max;
    }


    @Override
    public String toString() {
        return "Partition{" +
                "id=" + id +
                ", generatedFrom=" + generatedFrom +
                ", paths=" + paths +
                ", partitionItemList=" + partitionItemList +
                ", active=" + active +
                '}'+'\n';
    }
}
