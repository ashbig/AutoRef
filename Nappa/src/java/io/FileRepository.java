/*
 * FileRepository.java
 *
 * Created on October 5, 2007, 3:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import com.jscape.inet.sftp.Sftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import transfer.FilereferenceTO;
import util.SftpHandler;

/**
 *
 * @author dzuo
 */
public class FileRepository {
    
    /** Creates a new instance of FileRepository */
    public FileRepository() {
    }
    
    public static final void uploadFile(FilereferenceTO fileref, String filename) throws NappaIOException {
        try {
            InputStream input = new FileInputStream(new File(filename));
            uploadFile(fileref, input);
            input.close();
        } catch (IOException ex) {
            throw new NappaIOException(ex.getMessage());
        } 
    }
    
    public static final void uploadFile(FilereferenceTO fileref, InputStream input) throws NappaIOException {
        String file = fileref.getFilepath()+fileref.getFilename();
        
        try {
            Sftp ftp = SftpHandler.getSftpConnection();
            OutputStream out = ftp.getOutputStream(file, 0, false);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while((bytesRead = input.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            
            out.close();
            SftpHandler.disconnectSftp(ftp);
        } catch (IOException ex) {
            throw new NappaIOException(ex.getMessage());
        } 
    }
}
