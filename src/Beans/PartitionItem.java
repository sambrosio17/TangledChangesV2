package Beans;

import java.util.ArrayList;

public class PartitionItem implements Comparable<PartitionItem>{

    private int partitionIndex;
    private boolean active;
    private int i,j;
    private double confidenceValue;
    private ArrayList<String> paths;

    public PartitionItem(){
        active=true;
        i=-1;
        j=-1;
        partitionIndex=-1;
        paths=new ArrayList<>();
        confidenceValue=-1;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public double getConfidenceValue() {
        return confidenceValue;
    }

    public void setConfidenceValue(double confidenceValue) {
        this.confidenceValue = confidenceValue;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }

    public int getPartitionIndex() {
        return partitionIndex;
    }

    public void setPartitionIndex(int partitionIndex) {
        this.partitionIndex = partitionIndex;
    }

    @Override
    public boolean equals(Object x){

        PartitionItem p=(PartitionItem) x;

        return p.getI()==this.i && p.getJ()==this.j && p.getConfidenceValue()==this.confidenceValue;
    }


    @Override
    public int compareTo(PartitionItem o) {
        return (confidenceValue < o.getConfidenceValue()) ? -1 : ((confidenceValue == o.getConfidenceValue()) ? 0 : 1);
    }

    @Override
    public String toString() {
        return "PartitionItem{" +
                "active=" + active +
                ", i=" + i +
                ", j=" + j +
                ", confidenceValue=" + confidenceValue +
                ", paths=" + paths +
                '}'+'\n';
    }


}
