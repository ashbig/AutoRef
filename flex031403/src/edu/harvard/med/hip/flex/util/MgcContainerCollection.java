/*
 * MgcContainerCollection.java
 *
 * Created on May 14, 2002, 1:15 PM
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  htaycher
 */

import java.util.*;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.MgcContainer;
import sun.jdbc.rowset.*;


public class MgcContainerCollection {
    
    /** Creates a new instance of MgcContainerCollection */
    public MgcContainerCollection() {
    }
    
    public  ArrayList getNewPlates(java.lang.String fileName) 
            throws Exception
    {
        ArrayList labels = new ArrayList();
        String label = null;
        String marker = null;
        String orgContainerName = null;
        int container_id = -1;
        String sql = "select c.containerid as id, mc.oricontainer as orgcontainer, mc.marker as marker, c.label as label \n " + 
        " from mgccontainer mc , containerheader c \n " +
        " where  mc.mgccontainerid =  c.containerid  and " +
         "mc.filename = '" + fileName + "' order by c.label" ;        
      
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
                
                orgContainerName = crs.getString("ORGCONTAINER");
                label = crs.getString("LABEL");
                marker = crs.getString("MARKER");
                container_id = crs.getInt("ID");
                    
                labels.add ( new MgcContainer(container_id, fileName, null, orgContainerName,  label, marker));
              }
        } catch (Exception ex) 
        {
               throw new Exception("Error during extracting mgc labels");
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return labels;
    }
    
    //**********************************TESTING***************************
      public static void main(String args[])
    {
       
       ArrayList labels =  new ArrayList(); 
        try{
             MgcContainerCollection mgcCol = new MgcContainerCollection();
             labels =  mgcCol.getNewPlates("mgc_plate_info.txt");
             System.out.print(labels.size());
        }catch(Exception e)
        {};
            
   
       
    }
 
}
