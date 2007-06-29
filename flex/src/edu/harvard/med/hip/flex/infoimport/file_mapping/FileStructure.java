/*
 * FileStructure.java
 *
 * Created on June 4, 2007, 10:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/*
 *[FILE_TYPE] [FILE_VERSION] [COLUMN NAME] 
 [TABLE] [TCOLUMN TYPE] [TCOLUMN NAME] [TCOLUMN VALUE]
 *
 *[TCOLUMN TYPE] - values 1,2,3
 * 1 - normal column
 *2 - name type - name value pair, where column header nametype
 * 3 - next column - name value, this column name type
 *
 *
 *[TCOLUMN NAME] - is requered
 *[TCOLUMN VALUE] - is requered for NAME tables where header of the column goes into NAMETYPE
 *field, moreover
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

import java.util.*;
import java.io.*;

import edu.harvard.med.hip.flex.util.*;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.infoimport.*;
/**
 *
 * @author htaycher
 */
public class FileStructure
{
    private int             m_type = FILE_TYPE_NOT_DEFINED;
    private int             m_max_number_of_column_per_property = 4;
   
    private HashMap         m_columns = null;
    private String          m_mapping_name = null;
    private HashMap         m_connections = null;
    
     
    public static final int FILE_TYPE_ONE_FILE_SUBMISSION = 0;
    public static final int FILE_TYPE_PLATE_MAPPING = 1;
    public static final int FILE_TYPE_NOT_DEFINED =2;
    public static final int FILE_TYPE_SEQUENCE_INFO =3;
    public static final int FILE_TYPE_GENE_INFO=4;
    public static final int FILE_TYPE_AUTHOR_INFO=5;
    
     public static final int FILE_TYPE_XML_DATAMAPPING_SCHEMA = -10;
   
    
    /** Creates a new instance of FileStructure */
    public FileStructure()     {    }

       
    public  int             getMaxNumberOfColumnsPerProperty (){ return m_max_number_of_column_per_property;}
    public int             getType(){ return m_type;}
     public HashMap         getColumns(){ return m_columns ;}
    public String          getSourceMappingName(){ return m_mapping_name ;}
     public HashMap         getConnections(){ return m_connections;}
    
    
     
      public  void             setMaxNumberOfColumnsPerProperty (int v){  m_max_number_of_column_per_property = v;}
      public void             setType(int v){  m_type = v;}
    public void            setColumns(HashMap v){  m_columns = v;}
    public void            addColumn(FileStructureColumn v){  if (m_columns == null) m_columns = new HashMap(); m_columns.put(v.getFileColumnName().toUpperCase(),v); }
    public void             setSourceMappingName(String v){  m_mapping_name = v;}
     public void         setConnections(HashMap v){  m_connections = v;}
     
    public static int             getFileType(String v)
    {
       for (int i_count = 0; i_count < ConstantsImport.FILE_TYPE.length; i_count++)
       {
           if (v.equalsIgnoreCase(ConstantsImport.FILE_TYPE[i_count]))
               return i_count;
       }
       return FILE_TYPE_NOT_DEFINED;
    }
    
    public void             addConnector(String column_header, int file_connect_to_type, String column_connect_to_name)
    {
        String[] con_info = { String.valueOf(file_connect_to_type),  column_connect_to_name};
        if ( m_connections == null) m_connections = new HashMap();
        m_connections.put(column_header, con_info);
    }
   
   /*
   public  String[]  getHeadersByObjectType( String object_type )
   {
       Iterator iter = m_columns.entrySet().iterator();
        FileStructureColumn column_def = null;
        ArrayList selected_columns = new ArrayList();
         while( iter.hasNext())
         {
            column_def = (FileStructureColumn ) iter.next();
            if (column_def.getObjectType() == object_type)
                selected_columns.add(column_def);
         }
        // sort by column order to serve those where property is combination of fields
        Collections.sort(selected_columns, new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    FileStructureColumn cl1 = (FileStructureColumn)o1;
                    FileStructureColumn cl2 = (FileStructureColumn)o2;
                    return cl1.getObjectPropertyOrder() - cl2.getObjectPropertyOrder();
                 }
                 public boolean equals(java.lang.Object obj)                {      return false;  }
           } );
        String[] arr_headers = String[selected_columns.size()];
        
        for (int count=0; count < selected_columns.size(); count++)
        {
               arr_headers[count] = ((FileStructureColumn)selected_columns.get(count)).getFileColumnName();
        }
       return arr_headers;
   }
       */
    public static void main(String[] args)
  {
   System.out.println(FileStructure.getFileType("FILE_TYPE_ONE_FILE_SUBMISSION"));
   System.exit(0);
   }
}
