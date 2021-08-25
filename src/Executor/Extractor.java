package Executor;

import Beans.Commit;
import Utils.RepoVisitor;
import org.repodriller.RepositoryMining;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRemoteRepository;

import java.util.ArrayList;

public class Extractor {

    ArrayList<Commit> commitList;
    String repoUrl;

    public Extractor(String repoUrl){
        this.repoUrl=repoUrl;
    }

    public ArrayList<Commit> doExtract(){

        new RepositoryMining()
                .in(GitRemoteRepository.hostedOn("https://github.com/golivax/JDX.git").buildAsSCMRepository())
                .through(Commits.all())
                .process(new RepoVisitor(this))
                .mine();

        return commitList;
    }

    public ArrayList<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(ArrayList<Commit> commitList) {
        this.commitList = commitList;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
}
