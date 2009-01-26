/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation;

/**
 *
 * @author htaycher
 */
public class FileForTransferDefinition {

    public final static String FL_HEADER="_HEADER";									
public final static String FL_COLUMN_HEADERS="_COLUMN_HEADERS";
public final static String FL_FILE_NAME="_FILE_NAME";

    private String m_header;
    private PlasmIDFileType m_file_type;
    private String  m_file_name;
    private String m_col_names;
    
    public FileForTransferDefinition(){}
    
    public String       getHeader(){ return m_header;}
    public String       getFileName(){ return m_file_name;}
    public PlasmIDFileType       getFileType(){ return m_file_type;}
    public String       getColumnNames(){ return m_col_names;}
    
    public void         setHeader(String v){ m_header = v;}
    public void         setFileName(String v){ m_file_name=v;}
    public void         setFileType(String v){ m_file_type=PlasmIDFileType.valueOf(v);}
    public void         setColumnNames(String v){ m_col_names=v;}
    
}
