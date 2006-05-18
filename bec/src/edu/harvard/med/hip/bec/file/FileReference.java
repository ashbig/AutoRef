//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * File : FileReference.java
 * Classes : FileReference
 *
 * Description :
 *
 *      Represents a file in the repository on the server.
 *
 *
 
 *
 ******************************************************************************
 
 */


package edu.harvard.med.hip.bec.file;

import java.io.*;
import java.sql.*;
import java.util.*;


import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.user.*;
import  edu.harvard.med.hip.bec.coreobjects.endreads.*;
import  edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.bec.*;

/**
 * Represents a file in the file repository.
 *
 * To get an instance of a file reference you must either create one with a
 * create method or use one of the find methods.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.3 $ $Date: 2006-05-18 15:41:50 $
 */

public class FileReference
{
 
    // file type for gel images;
    public static final int TYPE_TRACE_FILE=1;
  
  
    // id for the file reference
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
      // the type of the file reference
    private int            m_fileType=-1;
  
    
  // the path relative to the repository base directory this file is contained in, excluding the name of the file.
    private String          m_localPath        = null;
     //The name of the file excluding any path information.
    private String          m_baseName = null;
     
    /**
     * Protected constructor, to get an instance of file Reference you must
     * use either one of the create or find methods.
     */
    
    
    public FileReference(int id, String fileName, int fileType,    String localPath) throws BecDatabaseException
    {
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("filereferenceid");
        else
            m_id = id;
       m_fileType = fileType;
        m_baseName = fileName;
        m_localPath = localPath;
    
    }
     
    
    public int          getId() {        return m_id;    }
    public int          getFileType() {        return m_fileType;    }
    public String       getBaseName() {        return m_baseName;    }
    public String       getLocalPath() {        return m_localPath;    }
    public void         setId(int i) {         m_id = i;    }
    public void         setFileType(int i) {         m_fileType = i;    }
    public void         setBaseName(String i) {         m_baseName = i;    }
    public void         setLocalPath(String i) {         m_localPath = i;    }
   
    
    public void insertDataIntoDatabase(Connection conn, int resultid) throws BecDatabaseException
    {
        Statement stmt = null;
        String sql =null;
        try
        {
            stmt = conn.createStatement();
              sql = "insert into filereference (filereferenceid,localpath,basename,filetype)"
            +"values("+m_id+",'"+m_localPath+"','"+m_baseName+"',"+m_fileType+")";
            stmt.executeUpdate(sql);
            sql = "insert into resultfilereference  (resultid, filereferenceid)"+
                " values("+resultid+","+m_id+")";
            stmt.executeUpdate(sql);
    
          
            
        } catch (SQLException sqlE)
        {
            System.out.println(sqlE.getMessage()+sql);
            throw new BecDatabaseException("Cannot insert filerefernce info "+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    /**
     * This method finds existing filereferences linked to a
     * container in the database
     *
     * @param container The container object
     * @return fileRefList The list of filereference objects
     */
    
    
    /*
    public static LinkedList findFile(Container container) throws BecDatabaseException
    {
        String sql = "SELECT * FROM filereference\n"+
        "WHERE containerid="+container.getId()+"\n"+
        "ORDER BY filetype, filereferenceid";
        FileReference fileRef = null;
        LinkedList fileRefList = new LinkedList();
        ResultSet rs = null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while (rs.next())
            {
                int   fileid = rs.getInt("FILEREFERENCEID");
                String fileType = rs.getString("FILETYPE");
                String localPath = rs.getString("LOCALPATH");
                String fileName = rs.getString("BASENAME");
                fileRef = new FileReference(fileid,fileName,fileType,localPath,container);
                fileRefList.add(fileRef);
            } // while
            
        } catch (SQLException sqlex)
        {
            throw new BecDatabaseException("Error occured while retrieving FileReferences with container id: "+
            container.getId()+"\n"+sqlex+"\nSQL: "+sql);
        }   finally
        {
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
    
    
    /*
    public static LinkedList findFile(Result result) throws BecDatabaseException
    {
        
        String sql = "SELECT f.filereferenceid,f.containerid,f.filetype,f.localpath,f.basename\n"+
        "FROM filereference f, resultfilereference rf\n"+
        "WHERE f.filereferenceid = rf.filereferenceid\n"+
        "AND rf.resultid="+result.getId()+"\n"+
        "ORDER BY filetype, f.filereferenceid";
        FileReference fileRef = null;
        LinkedList fileRefList = new LinkedList();
        ResultSet rs = null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while (rs.next())
            {
                int fileid = rs.getInt("FILEREFERENCEID");
                int containerid = rs.getInt("CONTAINERID");
                String fileType = rs.getString("FILETYPE");
                String localPath = rs.getString("LOCALPATH");
                String fileName = rs.getString("BASENAME");
                Container container = new Container(containerid);
                fileRef = new FileReference(fileid,fileName,fileType,localPath,container);
                fileRefList.add(fileRef);
            } // while
            
        } catch (SQLException sqlex)
        {
            throw new BecDatabaseException("Error occured while retrieving FileReferences with result id: "+
            result.getId()+"\n"+sqlex+"\nSQL: "+sql);
        } catch (FlexCoreException ex)
        {
            ex.printStackTrace();
        }finally
        {
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
    
    /*
    public static FileReference createFile(Connection conn, String fileName,
    String fileType, String localPath,
    Container container) throws BecDatabaseException
    {
        int fileRefId = BecIDGenerator.getID("FILEREFERENCEID");
        FileReference fileRef = new FileReference(fileRefId, fileName, fileType,
        localPath, container);
        
        // insert into the fileReference table.
        String sql = "insert into FILEREFERENCE " +
        "(FILEREFERENCEID, CONTAINERID, FILETYPE, LOCALPATH, BASENAME)" +
        " values(?,?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,fileRefId);
            ps.setInt(2,container.getId());
            ps.setString(3,fileType);
            ps.setString(4,localPath);
            ps.setString(5,fileName);
            
            DatabaseTransaction.executeUpdate(ps);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        }
        return fileRef;
    }
    
    
    /**
     * Creates a string representation of the file.
     *
     * @return basename of the file.
     */
    public String toString()    {        return this.getBaseName();    }
    
    /**
     * gets the full url to the file on the server.
     *
     * @Return url to file
     */
    /*
    public String getURL()
    {
        BecProperties props = BecProperties.getInstance();
        
        String url = props.getProperty("bec.repository.baseurl") +
        this.localPath+this.getBaseName();
        return url;
    }
    */
} // End class FileReference

