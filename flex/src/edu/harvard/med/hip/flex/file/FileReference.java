/*
 * File : FileReference.java
 * Classes : FileReference
 *
 * Description :
 *
 *      Represents a file in the repository on the server.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * Revision:    07-03-2001  [wmar]
 *              Added static method findFile (Container container)
 *              Added static method findFile (Result result)
 * The following information is used by CVS
 * $Revision: 1.4 $
 * $Date: 2005-02-10 19:46:32 $
 * $Author: dzuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 22, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-22-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.file;

import java.io.*;
import java.sql.*;
import java.util.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.process.*;

/**
 * Represents a file in the file repository.
 *
 * To get an instance of a file reference you must either create one with a 
 * create method or use one of the find methods.
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.4 $ $Date: 2005-02-10 19:46:32 $
 */

public class FileReference {
    
    // file type for gel images;
    public static final String GEL_TYPE="GEL_IMAGE";
    
    // file type for chromatograms
    public static final String CHROMATOGRAM_TYPE="CHROMATOGRAM";
    
    // file tye for dna gels
    public static final String DNA_GEL_TYPE="DNA_GEL_IMAGE";
    
    // following are file types for expression vector.
    public static final String EXP_GEL_TYPE = "EXP_GEL_IMAGE";
    public static final String EXP_COLONY_TYPE = "EXP_COLONY_IMAGE";
    public static final String EXP_PRO_TYPE = "EXP_PROTEIN_IMAGE";
    public static final String EXP_RESTRICTION = "EXP_RESTRICTION";
    public static final String EXP_FLO = "EXP_FLORESCENCE";
    
    public static final String CULTURE_TYPE = "CULTURE_RESULT";
    
    // id for the file reference
    private int id;
    
    // the container this file reference is for
    private Container container;
    
    // the type of the file reference
    private String fileType;
    
    /*
     * the path relative to the repository base directory this file is contained
     * in, excluding the name of the file.
     */
    private String localPath;
    
    
    //The name of the file excluding any path information.
    private String baseName;
    
    /**
     * Protected constructor, to get an instance of file Reference you must
     * use either one of the create or find methods.
     */
    protected FileReference(int id, String fileName, String fileType, 
    String localPath, Container container) {
        this.id =id;
        this.fileType = fileType;
        this.baseName = fileName;
        this.localPath = localPath;
        this.container = container;
    }
    
    /**
     * Accessor for the id.
     *
     * @return the id for this file reference.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Accessor for the fileType
     *
     * @return the type of this file
     */
    public String getFileType() {
        return this.fileType;
    }
    
    /**
     * Accessor for base name of the file
     *
     * @return the name of this file
     */
    public String getBaseName() {
        return this.baseName;
    }
    
    /**
     * Accessor for the localPath
     *
     * @return the local path for this file
     */
    public String getLocalPath() {
        return this.localPath;
    }
    
    
    /**
     * Accessor for the container this file is about.
     *
     * @return the container associated with this file.
     */
    public Container getContainer() {
        return this.container;
    }
    
    /**
     * This method finds existing filereferences linked to a
     * container in the database
     *
     * @param container The container object
     * @return fileRefList The list of filereference objects
     */
    public static LinkedList findFile (Container container) throws FlexDatabaseException{
        String sql = "SELECT * FROM filereference\n"+
        "WHERE containerid="+container.getId()+"\n"+
        "ORDER BY filetype, filereferenceid";
        FileReference fileRef = null;
        LinkedList fileRefList = new LinkedList();
        ResultSet rs = null;
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while (rs.next()) {
                int   fileid = rs.getInt("FILEREFERENCEID");
                String fileType = rs.getString("FILETYPE");
                String localPath = rs.getString("LOCALPATH");
                String fileName = rs.getString("BASENAME");
                fileRef = new FileReference(fileid,fileName,fileType,localPath,container);
                fileRefList.add(fileRef);
            } // while
            
        } catch (SQLException sqlex) {
            throw new FlexDatabaseException("Error occured while retrieving FileReferences with container id: "+
                container.getId()+"\n"+sqlex+"\nSQL: "+sql);
        }   finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return fileRefList;
    }
    
    /**
     * This method finds existing filereferences linked to a
     * result object in the database.
     *
     * @param result The result object
     * @return fileRefList The list of filereference objects
     */
    public static LinkedList findFile (Result result) throws FlexDatabaseException{
        
        String sql = "SELECT f.filereferenceid,f.containerid,f.filetype,f.localpath,f.basename\n"+ 
        "FROM filereference f, resultfilereference rf\n"+
        "WHERE f.filereferenceid = rf.filereferenceid\n"+
        "AND rf.resultid="+result.getId()+"\n"+
        "ORDER BY filetype, f.filereferenceid";
        FileReference fileRef = null;
        LinkedList fileRefList = new LinkedList();
        ResultSet rs = null;
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while (rs.next()) {
                int fileid = rs.getInt("FILEREFERENCEID");
                int containerid = rs.getInt("CONTAINERID");
                String fileType = rs.getString("FILETYPE");
                String localPath = rs.getString("LOCALPATH");
                String fileName = rs.getString("BASENAME");
                Container container = new Container(containerid);
                fileRef = new FileReference(fileid,fileName,fileType,localPath,container);
                fileRefList.add(fileRef);
            } // while
            
        } catch (SQLException sqlex) {
            throw new FlexDatabaseException("Error occured while retrieving FileReferences with result id: "+
                result.getId()+"\n"+sqlex+"\nSQL: "+sql);
        } catch (FlexCoreException ex){
            ex.printStackTrace();
        }finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return fileRefList;
    }
    
    
    /**
     * This method uploads a file to the repository and inserts info into the
     * file reference table.
     *
     * @param conn The connection to use when performing the inserts.
     * @param fileName the name of the file to create.
     * @param fileType The type of the file.
     * @param localPath the path relative to the repository to create the file 
     *  in.
     * @param container The container this file references.
     * 
     * @exception FlexDatabaseException When a database error occurs.
     */
    public static FileReference createFile(Connection conn, String fileName, 
    String fileType, String localPath, 
    Container container) throws FlexDatabaseException {
        int fileRefId = FlexIDGenerator.getID("FILEREFERENCEID");
        FileReference fileRef = new FileReference(fileRefId, fileName, fileType,
        localPath, container);
        
        // insert into the fileReference table.
        String sql = "insert into FILEREFERENCE " +
        "(FILEREFERENCEID, CONTAINERID, FILETYPE, LOCALPATH, BASENAME)" +
        " values(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,fileRefId);
            ps.setInt(2,container.getId());
            ps.setString(3,fileType);
            ps.setString(4,localPath);
            ps.setString(5,fileName);
            
            DatabaseTransaction.executeUpdate(ps);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        }
          return fileRef;
    }
  
    
    /**
     * Creates a string representation of the file.
     *
     * @return basename of the file.
     */
    public String toString() {
        return this.getBaseName();
    }
    
    /**
     * gets the full url to the file on the server.
     *
     * @Return url to file
     */
    public String getURL() {
        FlexProperties props = FlexProperties.getInstance();
                                        
        String url = props.getProperty("flex.repository.baseurl") + 
            this.localPath+this.getBaseName();
        return url;
    }
    
} // End class FileReference


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
