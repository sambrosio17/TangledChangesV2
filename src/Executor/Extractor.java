package Executor;

import Beans.Commit;
import Utils.RepoVisitor;
import org.repodriller.RepositoryMining;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRemoteRepository;

import java.util.HashMap;

public class Extractor {

    HashMap<String, Commit> commitList;
    String repoUrl;

    public Extractor(String repoUrl){
        this.repoUrl=repoUrl;
        this.commitList=new HashMap<>();
    }

    public HashMap<String, Commit> doExtract(){

        new RepositoryMining()
                .in(GitRemoteRepository.hostedOn(repoUrl).buildAsSCMRepository())
                .through(Commits.all())
                .process(new RepoVisitor(this))
                .mine();

        return commitList;
    }

    public HashMap<String, Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(HashMap<String, Commit> commitList) {
        this.commitList = commitList;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
}
