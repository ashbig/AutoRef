/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation;

/**
 *
 * @author htaycher
 */

import edu.harvard.med.hip.flex.util.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class PlasmidProperties  extends FlexProperties 
{
    private HashMap <String, FileForTransferDefinition >     m_files ;
    
    public final static String REPORT_COLUMN_ORDER_TYPE_FILE ="config/PlasmidSubmission.properties";

    private static PlasmidProperties pInstance = null;
     protected InputStream getInputStream() {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(REPORT_COLUMN_ORDER_TYPE_FILE));
    }
    
    /**
     * Gets the instance of systemproperties.
     *
     * @return the single SystemProperites instance.
     */
    public static PlasmidProperties getInstance() {
        if(pInstance == null) {
            pInstance = new PlasmidProperties();
            
        }
        return pInstance;
    } 
    
    public HashMap<String, FileForTransferDefinition >           getFilesProperties(){ return m_files;}
    public FileForTransferDefinition            getFilePropertiesByType(String v){ return (FileForTransferDefinition)m_files.get(v);}
    
    public   void            processProperties()
    {
        if ( m_files != null) return;
        m_files = new  HashMap <String, FileForTransferDefinition >  ( );
        //sort property keys to insure that file def together
        Enumeration <Object> keys = PlasmidProperties.getInstance().getKeys();
        List <String> elementList = new ArrayList();
        while (keys.hasMoreElements())
        {
                elementList.add((String)keys.nextElement());
        }
        Collections.sort(elementList);
        String pr_value;String file_key;FileForTransferDefinition fdef;
        for(String p_key : elementList)
        {
            pr_value = PlasmidProperties.getInstance().getProperty(p_key);
            file_key = p_key.substring(0, p_key.indexOf("_"));
            if ( m_files.get(file_key) == null )
            {
                fdef=new FileForTransferDefinition();fdef.setFileType(file_key);
                m_files.put(file_key, fdef);
            }
            else {fdef=m_files.get(file_key);}
           
            if ( p_key.equalsIgnoreCase( file_key+FileForTransferDefinition.FL_HEADER))
            {fdef.setHeader(pr_value);}
            else if  ( p_key.equalsIgnoreCase( file_key+FileForTransferDefinition.FL_FILE_NAME))
            {fdef.setFileName(pr_value);}
            else if  ( p_key.equalsIgnoreCase( file_key+FileForTransferDefinition.FL_COLUMN_HEADERS))
            {fdef.setColumnNames(pr_value);}
        }

    }
    
      public static void main(String [] args) 
    {
        try
        {
             FlexProperties sysProps =  PlasmidProperties.getInstance(  );
             ((PlasmidProperties)sysProps).processProperties();
             FileForTransferDefinition fd =  ((PlasmidProperties)sysProps).getFilePropertiesByType("AUTHOR");
       System.exit(0);
        }
        catch(Exception e){}
      }
}
    
