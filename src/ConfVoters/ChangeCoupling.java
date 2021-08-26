package ConfVoters;

import Beans.Commit;
import Beans.CommitChange;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChangeCoupling {

    private HashMap<String, Beans.Commit> map;

    public ChangeCoupling(HashMap<String, Beans.Commit> map){
        this.map=map;
    }

    private boolean inSameCommit(Beans.Commit commit, String path1, String path2){
        int counter=0;
        for(CommitChange change : commit.getChanges()){
            if(change.getPath().equals(path1)) counter++;
            if(change.getPath().equals(path2)) counter++;
        }
        return counter==2;
    }

    public int doCalculate(String path1, String path2){
        int counter=0;

        Iterator<Map.Entry<String, Commit>>it=map.entrySet().iterator();

        while(it.hasNext()){
            if(inSameCommit(it.next().getValue(),path1,path2)) counter++;
        }

        return counter;
    }
}
