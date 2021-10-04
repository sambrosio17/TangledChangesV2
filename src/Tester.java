import Beans.Commit;
import Beans.CommitChange;
import Beans.Partition;
import Beans.StagedCommit;
import Executor.Extractor;
import Untangler.Untangler;
import Utils.JschConfigSessionFactory;
import Utils.StringCostants;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Tester {

    public static void main(String args[]) throws IOException, GitAPIException, URISyntaxException {


        /*System.out.println("Ciao, dammi l'url della repository");
        String url= (new Scanner(System.in)).nextLine();
        System.out.println("dammi pure il commitcode");
        String commitcode= (new Scanner(System.in)).nextLine();
        System.out.println("ok, adesso elaboro");*/

        /*
        String repoUrl="https://github.com/sambrosio17/secretBookTSW.git";
        String commitId="deb41ddf46f1acd0864c0b9120cbad4a18c183be";
        int stopCondition=15;

        HashMap<String, Commit> list = (new Extractor(repoUrl)).doExtract();
        Untangler algo = new Untangler(list.get(commitId), list, stopCondition);
        List<Partition> result = algo.doUntangle();



        System.out.println("Repository: "+repoUrl);
        System.out.println("Commmit ID: "+commitId);
        System.out.println("La repository ha "+list.size()+" commit.");
        System.out.println("Il commit contine "+list.get(commitId).getChanges().size()+" file.");
        System.out.println("Partizioni Create: "+result.size());

        for(int i=0; i<result.size(); i++){
            System.out.println("PARTIZIONE #"+i);
            //System.out.println("-- Generated From: "+ result.get(i).getGeneratedFrom());
            for(String s: result.get(i).getPaths()){
                System.out.println("- "+s);
            }
        }

        //System.out.println(result);


         */


        String repoUrl="D:/TestRepos/TangledChangesV2";
        //String commitId="deb41ddf46f1acd0864c0b9120cbad4a18c183be";
        int stopCondition=15;

        HashMap<String, Commit> list = (new Extractor(repoUrl)).doExtract();


        //Apri la repository
        Git git=Git.open(new File("D:/TestRepos/TangledChangesV2"));
        //recupera i file all'interno della staging area
        StagedCommit staged=new StagedCommit();

        for(String path : git.status().call().getAdded()){
            if(!path.contains(".java")) continue;
            CommitChange change=new CommitChange(path, StringCostants.ADD);
            staged.getChanges().add(change);
        }

        for(String path : git.status().call().getChanged()){
            if(!path.contains(".java")) continue;
            CommitChange change=new CommitChange(path, StringCostants.CHANGE);
            staged.getChanges().add(change);
        }

        for(String path : git.status().call().getModified()){
            if(!path.contains(".java")) continue;
            CommitChange change=new CommitChange(path, StringCostants.MODIFY);
            staged.getChanges().add(change);
        }

        for(String path : git.status().call().getRemoved()){
            if(!path.contains(".java")) continue;
            CommitChange change=new CommitChange(path, StringCostants.REMOVE);
            staged.getChanges().add(change);
        }

        Untangler algo = new Untangler(staged, list, stopCondition);
        List<Partition> result = algo.doUntangle();

        System.out.println("Repository: "+repoUrl);
        //System.out.println("Commmit ID: "+commitId);
        System.out.println("La repository ha "+list.size()+" commit.");
        System.out.println("La stage area contiene "+staged.getChanges().size()+" file.");
        System.out.println("Partizioni Create: "+result.size());
        System.out.println(result);

        System.out.println("\n\n "+list);

        git.reset().call();

        System.out.println(git.status().call().getUncommittedChanges());

        for(Partition p : result){
            for(String path : p.getPaths()){
                CommitChange c= staged.findOne(path);
                if(c==null) continue;
                switch (c.getAction()){
                    case StringCostants.REMOVE:
                        git.rm().addFilepattern(path).call(); break;
                    case StringCostants.CHANGE:
                    case StringCostants.MODIFY:
                    case StringCostants.ADD:
                        git.add().addFilepattern(path).call(); break;
                    default: break;

                }
            }
            git.commit().setMessage("buonaseraaaaaaaa").setAuthor("sasino","s@outlook.it").call();
        }




































        /*
        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory();

        //trasforma l'url dello repo con protocollo ssh
        String sshRepoUrl=repoUrl.replace("https://github.com/","git@github.com:");

        //Clona la repository
        Git git=Git.cloneRepository()
                .setURI(sshRepoUrl)
                .setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory( sshSessionFactory );
                })
                .call();



        //soluzione 1: rimozione dei file dalla repo e successiva aggiunta

        /*
        //rimuovo i file dalla repo
        for(int i=0; i< result.size(); i++){
            for(String path : result.get(i).getPaths()){
                git.rm().setCached(true).addFilepattern(path).call();

            }
            git.commit().setMessage((new Date()).toString() +" - #Tangled - Rimozione automatica dei file - commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }

        //aggiungo i file alla repo
        for(int i=0; i< result.size(); i++){
            for(String path : result.get(i).getPaths()){
                git.add().addFilepattern(path).call();
            }
            git.commit().setMessage((new Date()).toString() +" - #Tangled - Aggiunta automatica dei file - commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }
         */





        //soluzione 2: aggiungere un commento che indica l'esecuzione dell'algoritmo all'interno di ciascun file di cui fare il commit

        //String radix="./"+repoUrl.replace("https://github.com/","").split("/")[1].replace(".git","")+"/.git/";

        /*
        Writer output;
        for(int i=0; i< result.size(); i++){
            for(String path : result.get(i).getPaths()){
                output = new BufferedWriter(new FileWriter(radix+path,true));  //clears file every time
                output.append("\n//Untangled partizione: "+i);
                output.close();
                git.add().addFilepattern(path).call();
            }
            git.commit().setMessage((new Date()).toString() +" - #Tangled - Creazione dei commit corretti- commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }

         */




        //soluzione 3: creare una nuova branch riservata all'untangling (non funziona modificare il file)

        /*
        git.checkout().setCreateBranch(true).setName("UNTANGLED").setOrphan(true).setForce(true).call();

        for(int i=0; i< result.size(); i++){
            for(String path : result.get(i).getPaths()){
                git.add().addFilepattern(path).call();
            }
            git.commit().setMessage((new Date()).toString() +" - #Tangled - Rimozione automatica dei file - commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }



        git.close();

         */

        //elimino la clone della repository

        //FileUtils.deleteDirectory(new File(radix.replace("/.git/","")));





















        /* *************************** POTREBBERO SERVIRE ***************************************************/
        /*
        //Git git= Git.open(new File("./RistoManager/.git")); //passare   Git git= Git.open(new File("./RistoManager/.git"))
        Git git=Git.cloneRepository()
                .setURI("git@github.com:sambrosio17/secretBookTSW.git")
                .setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = ( SshTransport )transport;
                    sshTransport.setSshSessionFactory( sshSessionFactory );
                })
                .call();

        //git.branchCreate().setName("03untangled").call();

        git.checkout().setCreateBranch(true).setName("06untangled").setOrphan(true).setForce(true).call();




        //git=Git.open(new File("./RistoManager/.git"));
        /*for(int i=0; i< result.size(); i++){
            System.out.println("entro");
            for(String path : result.get(i).getPaths()){
                System.out.println(path);
                git.rm().setCached(true).addFilepattern(path).call();
                //git.add().addFilepattern(path).call();
            }
            git.commit().setMessage((new Date()).toString() +" - #Tangled - Rimozione automatica dei file - commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }*/



        /*
        Writer output;
        for(int i=0; i< result.size(); i++){
            //System.out.println("entro");
            for(String path : result.get(i).getPaths()){
                //System.out.println(path);
                //git.rm().setCached(true).addFilepattern(path).call();

                output = new BufferedWriter(new FileWriter("./secretBookTSW/.git/"+path,true));  //clears file every time
                output.append("//Untangled partizione: "+i);
                output.close();
                git.add().addFilepattern(path).call();
            }
            git.commit().setMessage((new Date()).toString() +" - #Tangled - Creazione dei commit corretti- commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }


        /*Ref checkout = git.checkout().setName("master").call();


        ObjectId createdBranch=git.getRepository().resolve("untangled01");

        MergeResult merge = git.merge().
                include(createdBranch).
                setCommit(true).
                setFastForward(MergeCommand.FastForwardMode.NO_FF).
                //setSquash(false).
                        setMessage("Merged changes").
                        call();*/
        /*git.add().addFilepattern("t/test.text").call();
        git.commit().setMessage("outer").call();
        git.push().setTransportConfigCallback(transport -> {
            SshTransport sshTransport = ( SshTransport )transport;
            sshTransport.setSshSessionFactory( sshSessionFactory );
        }).call();*/
        //Git git=Git.open(new File("./RistoManager/.git"));
        /*git.add().addFilepattern("RistoManager/WebContent/ciaoWeb.txt").call();
        git.commit().setMessage("cartellatEST").call();
        */

        //FileUtils.deleteDirectory(new File("./secretBookTSW/.git"));


    }


}
