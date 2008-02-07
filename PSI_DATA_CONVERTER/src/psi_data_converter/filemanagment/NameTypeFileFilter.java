/*
 * NameTypeFileFilter.java
 *
 * Created on February 5, 2008, 9:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.filemanagment;

import java.io.*;
/**
 *
 * @author htaycher
 */
public class NameTypeFileFilter implements FileFilter
{
    private String m_file_name_type ;
    /** Creates a new instance of NameTypeFileFilter */
    public NameTypeFileFilter(String file_name_type )
    {
        m_file_name_type =  file_name_type;
    
    }
    public boolean accept (File curfile) 
    {
        if ( !curfile.isDirectory())
        {
            return curfile.getName().toLowerCase().contains(m_file_name_type.toLowerCase());
        }
        return false;
     }
  
    
}




