/*
 * File : FileRepository.java
 * Classes : FileRepository
 *
 * Description :
 *
 *      Represents the file repository.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.3 $
 * $Date: 2003-09-15 20:21:22 $
 * $Author: dzuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 22, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-22-2001 : JMM - Class created
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.file;

import java.io.*;
import java.util.*;

import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.Container;

/**
 * Reresents the file repository.
 *
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.3 $ $Date: 2003-09-15 20:21:22 $
 */

public class FileRepository {
    
    // base directory for the file repository
    public final static String REPOSITORY_PATH=FlexProperties.getInstance().getProperty("flex.repository.basedir");
    
    // local path to store gelimages
    public final static String GEL_LOCAL_PATH=FlexProperties.getInstance().getProperty("flex.repository.gel.relativedir");
    
    // local path to store dna gels
    public final static String DNA_GEL_LOCAL_PATH=FlexProperties.getInstance().getProperty("flex.repository.dnagel.relativedir");
    
    // local path to store expression vector gel images
    public final static String EXP_GEL_LOCAL_PATH = FlexProperties.getInstance().getProperty("flex.repository.expgel.relativedir");
    
    /**
     * Uploads a file to the server.
     *
     * @param fileRef The file reference that is to be uploaded
     * @param iStream The input stream holding the file's information.
     *
     * @exception IOException while a file write fails
     */
    public static void uploadFile(FileReference fileRef,
    InputStream iStream) throws IOException {
        Calendar cal = Calendar.getInstance();
        // the full path of the directory where the file will be placed.
        String fullDir =  REPOSITORY_PATH + fileRef.getLocalPath();
        
        // the full path where the file will be placed.
        String fullPath= fullDir + fileRef.getBaseName();
        
        // if the directory doesn't exist create it.
        File dirFile = new File(fullDir);
        if(! dirFile.exists()) {
            dirFile.mkdirs();
        }
        
        // write the file.
        OutputStream bos = new FileOutputStream(fullPath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = iStream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bos.close();
    }
   
    public static void main (String [] args) {
        File file = new File("/juan/test");
        System.out.println("dir exists: " + file.exists());
        file.mkdirs();
        System.out.println("dir exists: " + file.exists());
    }
    
} // End class FileRepository


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
