package Beans;

public class CommitChange {

    public String path, action;

    public CommitChange() {
    }

    public CommitChange(String path, String action) {
        this.path = path;
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "CommitChange{" +
                "path='" + path + '\'' +
                ", action='" + action + '\'' +
                "}\n";
    }
}
