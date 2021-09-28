import Beans.Commit;
import Beans.Partition;
import ConfVoters.ChangeCoupling;
import ConfVoters.PackageDistance;
import Executor.Extractor;
import Untangler.Untangler;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Tester {

    public static void main(String args[]) throws IOException, GitAPIException, URISyntaxException {


        /*System.out.println("Ciao, dammi l'url della repository");
        String url= (new Scanner(System.in)).nextLine();
        System.out.println("dammi pure il commitcode");
        String commitcode= (new Scanner(System.in)).nextLine();
        System.out.println("ok, adesso elaboro");*/

        HashMap<String, Commit> list = (new Extractor("https://github.com/BeppeTemp/OliCilento.git")).doExtract();
        Untangler algo = new Untangler(list.get("3f66921c8f73f892930f8edcf7e8bbb1bd1516c8"), list, 2);
        System.out.println(list);
        List<Partition> result = algo.doUntangle();
        System.out.println(result);
        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session ) {

                session.setConfig("StrictHostKeyChecking", "no");
                session.setUserInfo(new UserInfo() {
                    @Override
                    public String getPassphrase() {
                        return null;
                    }

                    @Override
                    public String getPassword() {return null;}

                    @Override
                    public boolean promptPassword(String message) {return false;}

                    @Override
                    public boolean promptPassphrase(String message) {return true;}

                    @Override
                    public boolean promptYesNo(String message) {return false;}

                    @Override
                    public void showMessage(String message) {}
                });
            }
        };
        //Git git= Git.open(new File("./RistoManager/.git")); //passare   Git git= Git.open(new File("./RistoManager/.git"))
        /*Git git=Git.cloneRepository()
                .setURI("git@github.com:sambrosio17/secretBookTSW.git")
                .setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = ( SshTransport )transport;
                    sshTransport.setSshSessionFactory( sshSessionFactory );
                })
                .call();

        //git=Git.open(new File("./RistoManager/.git"));
        for(int i=0; i< result.size(); i++){
            System.out.println("entro");
            for(String path : result.get(i).getPaths()){
                System.out.println(path);
                git.rm().setCached(true).addFilepattern(path).call();
                //git.add().addFilepattern(path).call();
            }
            git.commit().setMessage("#Tangled - Rimozione automatica dei file - commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }

        for(int i=0; i< result.size(); i++){
            //System.out.println("entro");
            for(String path : result.get(i).getPaths()){
                //System.out.println(path);
                //git.rm().setCached(true).addFilepattern(path).call();
                git.add().addFilepattern(path).call();
            }
            git.commit().setMessage("#Tangled - Creazione dei commit corretti- commit #"+(i+1)).call();
            git.push().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = ( SshTransport )transport;
                sshTransport.setSshSessionFactory( sshSessionFactory );
            }).setForce(true)
                    .call();
        }
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



    }


}
