package Beans;

import java.util.ArrayList;
import java.util.List;

public class StagedCommit {

    public List<CommitChange> changes=new ArrayList<>();

    public StagedCommit(){};

    public StagedCommit(List<CommitChange> changes){
        this.changes=changes;
    }

    public List<CommitChange> getChanges() {
        return changes;
    }

    public void setChanges(List<CommitChange> changes) {
        this.changes = changes;
    }

    public CommitChange findOne(String path){
        for(CommitChange c : changes){
            if(c.getPath().equals(path)) return c;
        }
        return null;
    }
}
