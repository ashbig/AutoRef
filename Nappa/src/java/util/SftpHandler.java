/*
 * SftpHandler.java
 *
 * Created on March 28, 2007, 3:23 PM
 */

package util;

import com.jscape.inet.sftp.*;
import com.jscape.inet.ssh.util.SshParameters;

/**
 *
 * @author  DZuo
 */
public class SftpHandler {
    public static final String SFTP_HOST = "128.103.32.160";
    public static final String SFTP_USERID = "plasmid";
    public static final String SFTP_PASSWORD = "plasmID";
    
    /** Creates a new instance of SftpHandler */
    public SftpHandler() {
    }
    
    public static Sftp getSftpConnection() throws SftpException {
        SshParameters params = new SshParameters(SFTP_HOST,SFTP_USERID,SFTP_PASSWORD);
        Sftp ftp = new Sftp(params);
        ftp.connect();
        return ftp;
    }
    
    public static void disconnectSftp(Sftp ftp) throws SftpException {
        ftp.disconnect();
    }
}
