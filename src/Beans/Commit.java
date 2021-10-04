package Beans;

import java.util.ArrayList;
import java.util.List;

public class Commit extends StagedCommit{

    public String id;
    public String author;
    public List<CommitChange> changes=new ArrayList<>();

    public Commit() {
    }

    public Commit(String id, String author) {
        this.id = id;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<CommitChange> getChanges() {
        return changes;
    }

    public void setChanges(List<CommitChange> changes) {
        this.changes = changes;
    }

    @Override
    public boolean equals(Object x){
        return id.equals(((Commit) x).getId());
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", changes=" + changes +
                "}\n";
    }
}
