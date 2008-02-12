/*
 * CVSMod.java
 *
 * Created on February 12, 2008, 12:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package psi_data_converter;
import psi_data_converter.filemanagment.*;
import psi_data_converter.util.*;
import psi_data_converter.verification.*;

import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class CVSMod {
    
    /** Creates a new instance of CVSMod */
    public CVSMod() {
    }
      public static void main(String[] args) 
      {
        try
        {
            String project_root="C:\\Projects\\FLEX";
            List replace_strings = new ArrayList();
            replace_strings.add(":ext:et15@orchestra.med.harvard.edu:/cvs/hip");
            String file_type = "Root";
            String dir_type = "CVS";
            ArrayList changed_files = new ArrayList();
         
            traverseAndChange(project_root,replace_strings,file_type, dir_type,changed_files);
              FileManager. writeFile(changed_files,  "c:\\tmp\\updatingCVS.txt",  null, false);
  
        }
        catch(Exception e)
        {}
      }
      
      public static void traverseAndChange(String project_root,
              List replace_strings,String file_type, String dir_type,
              ArrayList changed_files)throws Exception
      {
           try
          {
                File cur_dir = new File(project_root);
                File[] cur_dir_files = cur_dir.listFiles();
               for ( File cfile : cur_dir_files)
               {
                 if (cfile.isDirectory() && cfile.getName().equalsIgnoreCase(dir_type) )
                 {
                      processRootFile(  cfile,   changed_files, replace_strings,   "Root");
                 }
                 else if (cfile.isDirectory() )
                 {
                     traverseAndChange( cfile.getAbsolutePath(), replace_strings, file_type,  dir_type,changed_files);
                 }
               }
             
          }
          catch(Exception e)
          {
              throw new Exception (e.getMessage());
          }
            }
      
        private static File[]    getCVSDirectory(String project_root,
               String file_type)
        {
                File cur_dir = new File(project_root);
                NameTypeFileFilter ffilter = new NameTypeFileFilter(file_type);
                return cur_dir.listFiles(ffilter);
             
        }
      private static void    processRootFile(File cur_dir , List<String> changed_files,
              List<String> replace_strings, String file_type)throws Exception
      {
           File[] cur_dir_files = cur_dir.listFiles();
            for ( File cfile : cur_dir_files)
            {
                 if (! cfile.isDirectory() && cfile.getName().equalsIgnoreCase("Root") )
                 {
                   String file_name = cfile.getAbsolutePath() ;
                   File org_file = new File(file_name);
                   org_file.delete();
                   FileManager.writeFile(replace_strings,  file_name,  null, false);
                   changed_files.add(file_name);
                   return;
                 }
            }
      }
}
