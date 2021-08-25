package Utils;

import Executor.Extractor;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class RepoVisitor implements CommitVisitor {

    Extractor caller;

    public RepoVisitor(Extractor caller){
        this.caller=caller;
    }

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {

        for(Modification m : commit.getModifications()) {
            System.out.println(commit.getHash()+" - "+m);

        }

    }
}
