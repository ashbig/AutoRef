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

import sun.jdbc.rowset.*;


public class MgcContainerCollection {
    
    /** Creates a new instance of MgcContainerCollection */
    public MgcContainerCollection() {
    }
    
    public  Hashtable getNewPlates(java.lang.String fileName, Connection conn) 
            throws Exception
    {
        Hashtable labels = new Hashtable();
        String label = null;
        String orgContainerName = null;
        String sql = "select mc.oricontainer as orgcontainer, c.label as label \n " + 
        "from mgccontainer mc , containerheader c \n " +
        " where ( mc.mgccontainerid =  c.containerid  and " +
         "ms.filename = " + fileName + " and glycerolcontainerid <> 1)" ;        
       
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
                
                orgContainerName = crs.getString("ORGCONTAINER");
                label = crs.getString("LABEL");
                labels.put(label, orgContainerName);
              }
        } catch (Exception ex) 
        {
               throw new Exception("Error during extracting mgc labels");
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        return labels;
    }
    
}
