package Utils;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.eclipse.jgit.transport.OpenSshConfig;

public class JschConfigSessionFactory extends org.eclipse.jgit.transport.JschConfigSessionFactory {
    @Override
    protected void configure(OpenSshConfig.Host host, Session session) {

        session.setConfig("StrictHostKeyChecking", "no");
        session.setUserInfo(new UserInfo() {
            @Override
            public String getPassphrase() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public boolean promptPassword(String message) {
                return false;
            }

            @Override
            public boolean promptPassphrase(String message) {
                return true;
            }

            @Override
            public boolean promptYesNo(String message) {
                return false;
            }

            @Override
            public void showMessage(String message) {
            }
        });
    }
}
